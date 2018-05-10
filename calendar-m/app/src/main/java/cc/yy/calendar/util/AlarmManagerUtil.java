package cc.yy.calendar.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

import calendar.yy.cc.greendaolib.bean.Note;
import cc.yy.calendar.App;
import cc.yy.calendar.activity.MainActivity;
import cc.yy.calendar.receiver.PlanRemindReceiver;


/**
 * Created by zpy on 05/05/2017.
 */

public class AlarmManagerUtil {
    /**
     * 每天，工作日和周末都是每天都加入闹钟，推的时候再判断
     *
     * @param context
     * @param calDeadline
     * @param note
     */
    public static void alarmManagerSetRepeat(Context context, Calendar calDeadline, Note note) {
        Intent intent = new Intent(context, PlanRemindReceiver.class);
        intent.setAction(String.valueOf(note.getId()));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        App.Companion.getInstance().getAlarmManager().set(AlarmManager.RTC_WAKEUP, calDeadline.getTimeInMillis(), pendingIntent);
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    public static void alarmManagerSet(Context context, Calendar calDeadline, Note note) {
        Intent intent = new Intent(context, PlanRemindReceiver.class);
        intent.setAction(String.valueOf(note.getId()));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        App.Companion.getInstance().getAlarmManager().set(AlarmManager.RTC_WAKEUP, calDeadline.getTimeInMillis(), pendingIntent);
    }

    public static void cancelAlarm(Context context, long noteId) {
        Intent intent = new Intent(context, PlanRemindReceiver.class);
        intent.setAction(String.valueOf(noteId));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        App.Companion.getInstance().getAlarmManager().cancel(pendingIntent);
    }

    public static Integer[] getTimeSlot(Date startDate, Date endDate) {
        try {
            long diff = endDate.getTime() - startDate.getTime();
            if (diff > 0) {
                int diffSeconds = (int) (diff / 1000 % 60);
                int diffMinutes = (int) (diff / (60 * 1000) % 60);
                int diffHours = (int) (diff / (60 * 60 * 1000) % 24);
                int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
                return new Integer[]{
                        diffDays,
                        diffHours,
                        diffMinutes,
                        diffSeconds
                };
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void notifyPlan(Context context, Note note) {
        NotificationCompat.Builder builder = App.Companion.getInstance().getBuilder();
        builder.setContentTitle("日记提醒");
        builder.setContentText(note.getText());
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("planID", note.getId());
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, note.getId().intValue(), resultIntent, 0);
        builder.setContentIntent(resultPendingIntent);
        App.Companion.getInstance().getNotificationManager().notify(note.getId().intValue(), builder.build());
    }
}
