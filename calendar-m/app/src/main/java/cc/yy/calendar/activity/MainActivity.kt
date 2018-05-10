package cc.yy.calendar.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import cc.yy.calendar.R
import cc.yy.calendar.util.Constant
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import java.util.*


class MainActivity : BaseActivity() {
    override fun getLayout(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        initCalendarView()
        initListener()
        val intentFilterSetBackground = IntentFilter(Constant.SP_ACTION_SET_BACKGROUND)
        registerReceiver(broadcastReceiverChangeBackground, intentFilterSetBackground)
    }

    private fun initCalendarView() {
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.WEDNESDAY)
                .setMinimumDate(CalendarDay.from(2016, 4, 3))
                .setMaximumDate(CalendarDay.from(2020, 5, 12))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()
        calendarView.currentDate = CalendarDay.from(Calendar.getInstance())
        calendarView.selectedDate = CalendarDay.from(Calendar.getInstance())
        calendarView.setOnDateChangedListener { _, date, selected ->
            logUtil(DateFormat.format("yyyy-MM-dd HH:mm:ss", date.date).toString())
            if (selected) startActivity<EditNoteActivity>("date" to date.date)
        }
    }

    private fun initListener() {
        iv_setting.setOnClickListener {
            startActivity<SettingActivity>()
        }
    }

    private val broadcastReceiverChangeBackground = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val path = intent.getStringExtra("path")
            val drawable = BitmapDrawable.createFromPath(path)
            calendarView.backgroundDrawable = drawable
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Constant.REQUEST_CODE_FOR_SELECT_SEX == requestCode) {
            if (Constant.RESULT_CODE_OK == resultCode) {
                val sex = app.getSpValue(Constant.SP_SEX, 0)
                val index = app.getSpValue(Constant.SP_MAIN_BACKGROUND_INDEX, -1)
                main.backgroundResource = if (-1 == index) {
                    Constant.MAIN_BACKGROUND_RES[sex]
                } else {
                    Constant.MAIN_BACKGROUND_RES[index]
                }
            } else {
                this.finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val sex = app.getSpValue(Constant.SP_SEX, -1)
        if (sex == -1) {
            //去选择性别
            startActivityForResult<SelectSexActivity>(Constant.REQUEST_CODE_FOR_SELECT_SEX)
        } else {
            val index = app.getSpValue(Constant.SP_MAIN_BACKGROUND_INDEX, -1)
            main.backgroundResource = if (-1 == index) {
                Constant.MAIN_BACKGROUND_RES[sex]
            } else {
                Constant.MAIN_BACKGROUND_RES[index]
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiverChangeBackground)
    }
}

fun Context.logUtil(error: String) {
    Log.e(javaClass.canonicalName, error)
}



