/*here one-time clock is TimePickerDialog without layout xxx.xml.
 *     repeat-clock   is AlertDialog      with a  layout timeset.xml.

 qixy: requestCode is very important in that it can differentiate these two clocks,when you cancel either
clock, requestCode is the only thing to differentiate them.
       */

package irdc.ex06_10;

import java.util.Calendar;

//import net.blogjava.mobile.calendar.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import cc.yy.calendar.R;
import cc.yy.calendar.activity.CallAlarm;
import cc.yy.calendar.activity.ClockActivity;

public class EX06_10 extends Activity
{
    TextView setTime1;
    Button mButton1;
    Button mButton2;

    Calendar c=Calendar.getInstance();
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main3);
        /*the following "setTime1" is the first red font.*/
        setTime1=(TextView) findViewById(R.id.setTime1);
        mButton1=(Button)findViewById(R.id.mButton1);



        mButton1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
/*public void setTimeInMillis (long milliseconds)
Sets the time of this Calendar.
Parameters:
milliseconds  the time as the number of milliseconds since Jan. 1, 1970.
*/
                c.setTimeInMillis(System.currentTimeMillis());
                int mHour=c.get(Calendar.HOUR_OF_DAY);
                int mMinute=c.get(Calendar.MINUTE);
/*android.app.TimePickerDialog: A dialog that prompts the user for the time of day using a TimePicker.
public TimePickerDialog (Context context, TimePickerDialog.OnTimeSetListener callBack, int hourOfDay,
int minute, boolean is24HourView)
Parameters:
context  Parent.
callBack  How parent is notified.
hourOfDay  The initial hour.
minute  The initial minute.
is24HourView  Whether this is a 24 hour view, or AM/PM.
qixy: we don't need a xxx.xml to layout. We use TimePickerDialog's default layout.
*/
                new TimePickerDialog(EX06_10.this,
                        new TimePickerDialog.OnTimeSetListener()
                        {
                            /*The callback interface used to indicate the user is done filling in the time (they clicked on the
                            'Set' button).
                             public abstract void onTimeSet (TimePicker view, int hourOfDay, int minute)
                            Parameters
                            view  The view associated with this listener.
                            hourOfDay  The hour that was set.
                            minute  The minute that was set.
                             */
                            public void onTimeSet(TimePicker view,int hourOfDay,int minute)
                            {
                                c.setTimeInMillis(System.currentTimeMillis());
                                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                c.set(Calendar.MINUTE,minute);
                                c.set(Calendar.SECOND,0);
                                c.set(Calendar.MILLISECOND,0);
/*   background knowledge: public abstract void Context.sendBroadcast (Intent intent)
Broadcast the given intent to all interested BroadcastReceivers.

public static PendingIntent getBroadcast (Context context, int requestCode, Intent intent, int flags)
Retrieve a PendingIntent that will perform a broadcast, like calling Context.sendBroadcast().
qixy: Retrieve a PendingIntent that will perform a broadcast to broadcast the given intent to all
interested BroadcastReceivers. , like calling Context.sendBroadcast().

  qixy:here CallAlarm is a BroadReceiver.
 note that if we change the following to
 Intent intent = new Intent(EX06_10.this, AlertAlarm.class); then it can not work, because AlertAlarm is an
 ordinary activity. so the principle of alarm is like this: must use broadcastReceiver.

qixy: Intent intent = new Intent(EX06_10.this, CallAlarm.class); means intent is associated with a CallAlarm,
when there is an alarm in the future, PendingIntent associated with this ordinary intent will broadcast
this ordinary intent which will be received by CallAlarm which is a BroadcastReceiver.


for reference:  The intent which will be broadcast goes to a broadcast receiver that you registered with
registerReceiver(BroadcastReceiver, IntentFilter) or through the <receiver> tag in an
AndroidManifest.xml file.


public Intent (Context packageContext, Class<?> cls)
Create an intent for a specific component. All other fields (action, data, type, class) are null, though
they can be modified later with explicit calls. This provides a convenient way to create an intent that
is intended to execute a hard-coded class name, rather than relying on the system to find an appropriate
class for you;

Parameters
packageContext  A Context of the application package implementing this class.
cls  The component class that is to be used for the intent.
   */
                                Intent intent = new Intent(EX06_10.this, ClockActivity.class);
/* qixy: getBroadcast will generate an intent, but not ordinary intent, a pendingIntent. Remember sending SMS
 by broadcast with PendingIntent.then pendingIntent will broadcast, then CallAlarm will receive. this is
 strategy.

 PendingIntent android.app.PendingIntent.getBroadcast(Context context, int requestCode, Intent intent, int flags)

public static PendingIntent getBroadcast (Context context, int requestCode, Intent intent, int flags)
Retrieve a PendingIntent that will perform a broadcast, like calling Context.sendBroadcast().

Parameters
context  The Context in which this PendingIntent should perform the broadcast.
requestCode  Private request code for the sender
intent  The Intent to be broadcast.
flags  May be FLAG_ONE_SHOT, FLAG_NO_CREATE, FLAG_CANCEL_CURRENT, FLAG_UPDATE_CURRENT, or any of
the flags as supported by Intent.fillIn() to control which unspecified parts of the intent that
can be supplied when the actual send happens.

Returns
Returns an existing or new PendingIntent matching the given parameters. May
return null only if FLAG_NO_CREATE has been supplied.

qixy: requestCode is very important in that it can differentiate these two clocks,when you cancel either
clock, requestCode is the only thing to differentiate them.
*/
                                PendingIntent sender=PendingIntent.getBroadcast(EX06_10.this,
                                        0, intent, 0);
                                AlarmManager am;
                                am = (AlarmManager)getSystemService(ALARM_SERVICE);
/*void android.app.AlarmManager.set(int type, long triggerAtTime, PendingIntent operation)

public void set (int type, long triggerAtTime, PendingIntent operation)
Schedule an alarm. Note: for timing operations (ticks, timeouts, etc) it is easier and much more efficient
to use Handler. If there is already an alarm scheduled for the same IntentSender, it will first be canceled.

If the time occurs in the past, the alarm will be triggered immediately. If there is already an alarm for
this Intent scheduled (with the equality of two intents being defined by filterEquals(Intent)), then it
will be removed and replaced by this one.

Parameters
type  One of ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC or RTC_WAKEUP.
triggerAtTime  Time the alarm should go off, using the appropriate clock (depending on the alarm type).
operation  Action to perform when the alarm goes off; typically comes from IntentSender.getBroadcast().

时钟分为系统时钟和硬件时钟两种。硬件时钟可以依赖于持续电源（通常是钮扣电池）持续累计时间，而系统时间会再系统调电之后重置，
所以一般linux系统再开始时会做一个系统时钟于硬件时钟同步的动作（hwclodk -w）,这个我们在后面再详细解释！所谓的实时时钟简
单理解起来，就是一个设备掉电（poweroff）之后的系统时钟维持模块。

RTC的英文全称是Real-Time Clock,翻译过来是实时时钟芯片.

　　RTC是PC主板上的晶振及相关电路组成的时钟电路的生成脉冲，RTC经过8254电路的频产生一个频率较低一点的OS(系统)时钟TSC
这里有4种Alarm类型。你的选择将决定你在set方法中传递的时间值代表什么，是特定的时间或者是时间流逝：
RTC_WAKEUP
在指定的时刻（设置Alarm的时候），唤醒设备来触发Intent。
RTC
在一个显式的时间触发Intent，但不唤醒设备。
ELAPSED_REALTIME
从设备启动后，如果流逝的时间达到总时间，那么触发Intent，但不唤醒设备。流逝的时间包括设备睡眠的任何时间。注意一点的是，
时间流逝的计算点是自从它最后一次启动算起。
ELAPSED_REALTIME_WAKEUP
从设备启动后，达到流逝的总时间后，如果需要将唤醒设备并触发Intent。
qixy: we just use RTC_WAKEUP, not intensively researched.Allegedly,RTC_WAKEUP means 即使系统休眠时同样会
运行set. while RTC 系统休眠时不会运行set.
qixy:the following statement means when c.getTimeInMillis(), PendingIntent will broadcast intent,
CallAlarm will receive it,
*/
                                am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
                                String tmpS=format(hourOfDay)+": "+format(minute);
                                setTime1.setText(tmpS);
                                Toast.makeText(EX06_10.this,"设置闹钟时间为 "+tmpS,Toast.LENGTH_SHORT).show();
                            }
                        },mHour,mMinute,true).show();



            }
        });




        mButton2=(Button) findViewById(R.id.mButton2);
        mButton2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(EX06_10.this, CallAlarm.class);
                PendingIntent sender=PendingIntent.getBroadcast(EX06_10.this,
                        0, intent, 0);
                AlarmManager am;
                am =(AlarmManager)getSystemService(ALARM_SERVICE);
/*void android.app.AlarmManager.cancel(PendingIntent operation)
public void cancel (PendingIntent operation)
Remove any alarms with a matching Intent. Any alarm, of any type, whose Intent
matches this one (as defined by filterEquals(Intent)), will be canceled.
Parameters:
operation  IntentSender which matches a previously added IntentSender.
*/
                am.cancel(sender);
                Toast.makeText(EX06_10.this,"闹钟时间解除", Toast.LENGTH_SHORT).show();
                setTime1.setText("目前无设置");
            }
        });


    }

    private String format(int x)
    {
        String s=""+x;
        if(s.length()==1) s="0"+s;
        return s;
    }
}