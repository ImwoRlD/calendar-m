package cc.yy.calendar.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.content.FileProvider
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import calendar.yy.cc.greendaolib.bean.Note
import calendar.yy.cc.greendaolib.bean.NoteDao
import cc.yy.calendar.R
import cc.yy.calendar.service.MyJobService
import cc.yy.calendar.util.Constant
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.weather.LocalWeatherForecastResult
import com.amap.api.services.weather.LocalWeatherLiveResult
import com.amap.api.services.weather.WeatherSearch
import com.amap.api.services.weather.WeatherSearchQuery
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import irdc.ex06_10.EX06_10
import kotlinx.android.synthetic.main.activity_edit_note.*
import org.jetbrains.anko.*
import permissions.dispatcher.*
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.properties.Delegates


/**
 * Created by zpy on 2018/3/13.
 */
@RuntimePermissions
class EditNoteActivity : BaseActivity() {
    override fun getLayout(): Int = R.layout.activity_edit_note
    private var date: Date by Delegates.notNull()
    private var isFinish = false
    var keyboardShowChangeListener = KeyboardShowChangeListener()
    private var globalListener by Delegates.notNull<ViewTreeObserver.OnGlobalLayoutListener>()
    private val minKeyboardHeightPx = 150
    var decorView: View? = null
    private var note: Note? = null
    private val requestImageFromAlbum = 111
    private val requestImageFromCamera = 222
    private lateinit var serviceComponent: ComponentName
    private var mLocationClient: AMapLocationClient by Delegates.notNull()
    private var mLocationListener: AMapLocationListener by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        initListener()
        date = intent.getSerializableExtra("date") as Date
        iv_time.text = DateFormat.format("yyyy-MM-dd", date).toString()
        val loadAll = app.noteDao.loadAll()
        loadAll.forEach {
            logUtil(it.toString())
        }
        note = app.noteDao.queryBuilder().where(NoteDao.Properties.Date.eq(date)).unique()
        if (null == note) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            inputStatus()
        } else {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            finishStatus()
            et_note_content.setText(note!!.text.toString())
        }
        decorView = window.decorView
        globalListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            private val windowVisibleDisplayFrame = Rect()
            private var lastVisibleDecorViewHeight: Int = 0

            override fun onGlobalLayout() {
                decorView?.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame)
                val visibleDecorViewHeight = windowVisibleDisplayFrame.height()
                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + minKeyboardHeightPx) {
                        keyboardShowChangeListener.keyboardShow()
                    } else if (lastVisibleDecorViewHeight + minKeyboardHeightPx < visibleDecorViewHeight) {
                        keyboardShowChangeListener.keyboardHidden()
                    }
                }
                lastVisibleDecorViewHeight = visibleDecorViewHeight
            }
        }
        decorView?.viewTreeObserver?.addOnGlobalLayoutListener(globalListener)
        val path = app.getSpValue(Constant.SP_PATH_FOR_BACKGROUND, "")
        if (!TextUtils.isEmpty(path)) {
            val drawable = BitmapDrawable.createFromPath(path)
            sv_main.background = drawable
        }
        serviceComponent = ComponentName(this, MyJobService::class.java)
        mLocationClient = AMapLocationClient(applicationContext)
        mLocationListener = AMapLocationListener {
            val mquery = WeatherSearchQuery(it.city, WeatherSearchQuery.WEATHER_TYPE_LIVE)
            val search = WeatherSearch(this)
            search.setOnWeatherSearchListener(object : WeatherSearch.OnWeatherSearchListener {
                override fun onWeatherLiveSearched(weatherLiveResult: LocalWeatherLiveResult?, p1: Int) {
                    if (p1 == 1000) {
                        if (weatherLiveResult?.liveResult != null) {
                            val weatherlive = weatherLiveResult.liveResult
                            val weather = String.format("%s：%s，温度：%s度", weatherlive.city, weatherlive.weather, weatherlive.temperature)
                            tv_today_weather.text = weather
                        } else {
                            tv_today_weather.text = "无结果"
                        }
                    } else {
                        tv_today_weather.text = "查询天气失败"
                    }
                }

                override fun onWeatherForecastSearched(p0: LocalWeatherForecastResult?, p1: Int) {
                }

            })
            search.query = mquery
            search.searchWeatherAsyn() //异步搜索
        }
        val aMapLocationClientOption = AMapLocationClientOption()
        //设置定位间隔,单位毫秒,默认为2000ms
        aMapLocationClientOption.interval = 60 * 1000
        //设置定位参数
        mLocationClient.setLocationOption(aMapLocationClientOption)
        mLocationClient.setLocationListener(mLocationListener)
        mLocationClient.startLocation()
    }

    private fun initListener() {
        iv_back.setOnClickListener {
            saveNote()
            this@EditNoteActivity.finish()
        }
        iv_submit_or_delete.setOnClickListener {
            if (isFinish) {
                //删除
                logUtil("删除了")
                deleteNote()
                this@EditNoteActivity.finish()
            } else {
                finishStatus()
                //保存
                saveNote()
            }
        }
        tv_change_background.setOnClickListener {
            val options = listOf("从手机相册选择", "拍一张")
            selector(null, options) { _, items ->
                when (items) {
                    0 -> {
                        openAlbumWithPermissionCheck()
                    }
                    1 -> {
                        openCameraWithPermissionCheck()
                    }
                }

            }
        }
        tv_change_font_color.setOnClickListener {
            ColorPickerDialogBuilder
                    .with(this@EditNoteActivity)
                    .setTitle("选择颜色")
                    .initialColor(Color.GRAY)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .noSliders()
                    .setOnColorSelectedListener { selectedColor -> et_note_content.textColor = selectedColor }
                    .setPositiveButton("确定") { _, selectedColor, _ -> et_note_content.textColor = selectedColor }
                    .build()
                    .show()
        }
        tv_set_remind.setOnClickListener {

//            val calendar = Calendar.getInstance()
//            val timePickerDialog = TimePickerDialog(this@EditNoteActivity, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
//                saveNote()
//                if (TextUtils.isEmpty(note?.text)) {
//                    toast("日记内容为空")
//                    return@OnTimeSetListener
//                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    finishJob(note?.id?.toInt() ?: 0)
//                }
//                note?.let {
//
//
//                    val calNow = Calendar.getInstance()
//                    val deadline = Calendar.getInstance()
//                    deadline[Calendar.HOUR_OF_DAY] = hour
//                    deadline[Calendar.MINUTE] = minute
//                    deadline[Calendar.SECOND] = 0
//                    deadline[Calendar.MILLISECOND] = 0
//                    logUtil(DateFormat.format("yyyy-MM-dd HH:mm:ss", deadline).toString())
//                    val diff = deadline.timeInMillis - calNow.timeInMillis
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        scheduleJob(it.id.toInt(), diff, it.id)
//                        toast("提醒设置成功")
//                    }
//                }
//            }, calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], true)
//            timePickerDialog.show()


            tv_set_remind.setOnClickListener{
                startActivity<EX06_10>()
        }
        }
    }

    private fun saveNote() {
        if (null == note) {
            note = Note()
            note!!.text = et_note_content.text.toString()
            note!!.date = date
            val id = app.noteDao.insert(note)
            note!!.id = id
        } else {
            note!!.text = et_note_content.text.toString()
            app.noteDao.update(note)
        }
    }

    private fun deleteNote() {
        note?.let {
            app.noteDao.deleteByKey(it.id)
        }
    }

    inner class KeyboardShowChangeListener : IKeyboardShowChangeListener {
        override fun keyboardShow() {
            inputStatus()
        }

        override fun keyboardHidden() {
            finishStatus()
        }
    }

    fun inputStatus() {
        isFinish = false
        iv_submit_or_delete.imageResource = R.drawable.ic_edit_submit
    }

    fun finishStatus() {
        isFinish = true
        iv_submit_or_delete.imageResource = R.drawable.ic_memo_delete
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val v = currentFocus
        if (v != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {

            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(this, "cc.yy.calendar.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, requestImageFromCamera)
            }
        }
    }

    @OnShowRationale(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showRationaleForOpenCamera(request: PermissionRequest) {
        request.proceed()
    }

    @OnPermissionDenied(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onOpenCameraDenied() {
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onOpenCameraNeverAskAgain() {
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun openAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, requestImageFromAlbum)
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun showRationaleForOpenAlbum(request: PermissionRequest) {
        request.proceed()
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun onOpenAlbumDenied() {

    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun onOpenAlbumNeverAskAgain() {

    }


    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private var currentPhotoPath: String? = null

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = DateFormat.format("yyyyMMdd_HHmmss", Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
        currentPhotoPath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestImageFromAlbum && Activity.RESULT_OK == resultCode) {
            if (data != null) {
                try {
                    val path = getImageAbsolutePath(this@EditNoteActivity, data.data)
                    logUtil(path ?: "fuck album")
                    path?.let {
                        app.putSpValue(Constant.SP_PATH_FOR_BACKGROUND, it)
                        val drawable = BitmapDrawable.createFromPath(it)
                        sv_main.background = drawable
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        if (requestCode == requestImageFromCamera && Activity.RESULT_OK == resultCode) {
            if (data != null) {
                logUtil(currentPhotoPath ?: "fuck camera")
                currentPhotoPath?.let {
                    app.putSpValue(Constant.SP_PATH_FOR_BACKGROUND, it)
                    val drawable = BitmapDrawable.createFromPath(it)
                    sv_main.background = drawable
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun scheduleJob(jobId: Int, deadline: Long, noteId: Long) {
        logUtil(deadline.toString())
        val builder = JobInfo.Builder(jobId, serviceComponent)
        builder.setMinimumLatency(deadline)
        builder.setOverrideDeadline(deadline)
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        val extras = PersistableBundle()
        extras.putLong("id", noteId)
        builder.run {
            setRequiresDeviceIdle(false)
            setRequiresCharging(false)
            setExtras(extras)
        }

        (getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler).schedule(builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun finishJob(jobId: Int) {
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val allPendingJobs = jobScheduler.allPendingJobs
        if (allPendingJobs.size > 0) {
            // Finish the last one.
            // Example: If jobs a, b, and c are queued in that order, this method will cancel job c.
            jobScheduler.cancel(jobId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        decorView?.viewTreeObserver?.removeGlobalOnLayoutListener(globalListener)
    }

}

interface IKeyboardShowChangeListener {
    fun keyboardShow()
    fun keyboardHidden()
}