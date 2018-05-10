package cc.yy.calendar.receiver

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.WakefulBroadcastReceiver
import cc.yy.calendar.App
import cc.yy.calendar.activity.logUtil
import cc.yy.calendar.util.AlarmManagerUtil
import java.util.*

/**
 * Created by zpy on 2017/5/3.
 */

class PlanRemindReceiver : WakefulBroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            handlePushReceiverAbove19(context, intent)
        } else {
            handlePushReceiver(context, intent)
        }
    }

    private fun handlePushReceiver(context: Context, intent: Intent) {
        //action是plan的id
        val action = intent.action
        context.logUtil("action=$action")
        val app = App.instance
        val note = app.noteDao.load(java.lang.Long.parseLong(action))

        val deadlineCal = Calendar.getInstance()
        deadlineCal.time = note.date
        //推送会有延时，所以向后推5分钟
        deadlineCal.add(Calendar.MINUTE, 5)
        val nowCal = Calendar.getInstance()
        //检查是否deadline,如果deadline，就cancel
        if (deadlineCal.before(nowCal)) {
            //过期了
            AlarmManagerUtil.cancelAlarm(context, note.id)
            return
        }
        //没有deadline,可以推送
        AlarmManagerUtil.notifyPlan(context, note)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun handlePushReceiverAbove19(context: Context, intent: Intent) {
        //action是plan的id
        val action = intent.action
        val app = App.instance
        val newPlanDao = app.noteDao
        val note = newPlanDao.load(java.lang.Long.parseLong(action)) ?: return
        val deadlineCal = Calendar.getInstance()
        val date = note.date
        deadlineCal.time = date
        //推送会有延时，所以向后推5分钟
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MINUTE, 5)
        val nowCal = Calendar.getInstance()
        //检查是否deadline,如果deadline，就cancel
        if (cal.before(nowCal)) {
            //过期了
            AlarmManagerUtil.cancelAlarm(context, note.id)
            return
        }
        val intentRemindReceiver = Intent(context, PlanRemindReceiver::class.java)
        intentRemindReceiver.action = note.id.toString()
        AlarmManagerUtil.notifyPlan(context, note)
    }
}
