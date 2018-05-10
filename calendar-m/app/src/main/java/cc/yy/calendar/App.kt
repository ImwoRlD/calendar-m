package cc.yy.calendar

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.support.v4.app.NotificationCompat
import calendar.yy.cc.greendaolib.bean.DaoMaster
import calendar.yy.cc.greendaolib.bean.NoteDao
import calendar.yy.cc.greendaolib.helper.GreenDaoHelper
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder
import com.nostra13.universalimageloader.core.download.BaseImageDownloader
import com.nostra13.universalimageloader.utils.StorageUtils
import org.xutils.x
import kotlin.properties.Delegates


/**
 * Created by zpy on 2018/3/13.
 */
class App : Application() {
    var noteDao by Delegates.notNull<NoteDao>()
    var sp: SharedPreferences by Delegates.notNull()
    var alarmManager: AlarmManager by Delegates.notNull()
    var builder: NotificationCompat.Builder by Delegates.notNull()
    var notificationManager: NotificationManager by Delegates.notNull()

    override fun onCreate() {
        super.onCreate()
        instance = this
        sp = getSharedPreferences("memo-data", MODE_PRIVATE)
        x.Ext.init(this)
        x.Ext.setDebug(BuildConfig.DEBUG) // 是否输出debug日志, 开启debug会影响性能.
        //创建数据库
        initGreenDao()
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        initNotification()
        initImageLoaderConfig()
    }

    private fun initGreenDao() {
        if (isMainProcess(applicationContext)) {
            val helper = GreenDaoHelper(this, "calendar-db", DaoMaster.SCHEMA_VERSION)
            val db = helper.writableDb
            val daoSession = DaoMaster(db).newSession()
            noteDao = daoSession.noteDao
        }
    }

    private fun initNotification() {
        builder = NotificationCompat.Builder(this)
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
        builder.setAutoCancel(true)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun initImageLoaderConfig() {
        val cacheDir = StorageUtils.getCacheDirectory(this)
        val config = ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(HashCodeFileNameGenerator()) // default
                .imageDownloader(BaseImageDownloader(this)) // default
                .imageDecoder(BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build()
        ImageLoader.getInstance().init(config)
    }

    companion object {
        var instance: App by Delegates.notNull()
            private set
    }

    @SuppressLint("CommitPrefEdits")
    fun putSpValue(name: String, value: Any) = with(sp.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("exception")
        }.apply()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getSpValue(name: String, default: T): T = with(sp) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("exception")
        }
        res as T
    }
}

fun isMainProcess(context: Context): Boolean {
    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val processInfo = am.runningAppProcesses
    val mainProcessName = context.packageName
    val myPid = android.os.Process.myPid()
    if (null == processInfo) {
        return false
    }
    for (info in processInfo) {
        if (info.pid == myPid && mainProcessName == info.processName) {
            return true
        }
    }
    return false
}

fun Context.getScreenHeight(): Int = resources.displayMetrics.heightPixels
fun Context.getScreenWidth(): Int = resources.displayMetrics.widthPixels