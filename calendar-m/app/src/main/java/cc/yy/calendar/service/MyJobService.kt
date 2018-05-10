package cc.yy.calendar.service

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.os.Messenger
import android.support.annotation.RequiresApi
import cc.yy.calendar.App
import cc.yy.calendar.activity.logUtil
import cc.yy.calendar.util.AlarmManagerUtil
import cc.yy.calendar.util.Constant.Companion.MESSENGER_INTENT_KEY

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
/**
 * Created by zpy on 2018/4/17.
 */
class MyJobService : JobService() {

    private var activityMessenger: Messenger? = null

    /**
     * When the app's MainActivity is created, it starts this service. This is so that the
     * activity and this service can communicate back and forth. See "setUiCallback()"
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        activityMessenger = intent.getParcelableExtra(MESSENGER_INTENT_KEY)
        return Service.START_NOT_STICKY
    }

    override fun onStartJob(params: JobParameters): Boolean {
        logUtil("start")
        val id = params.extras.getLong("id")
        val app = App.instance
        val note = app.noteDao.load(id)
        AlarmManagerUtil.notifyPlan(baseContext,note)
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        // Stop tracking these job parameters, as we've 'finished' executing.
        logUtil("stop")
        // Return false to drop the job.
        return false
    }
}