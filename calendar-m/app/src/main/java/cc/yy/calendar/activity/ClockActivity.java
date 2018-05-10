package cc.yy.calendar.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cc.yy.calendar.R;

public class ClockActivity extends Activity {

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        mediaPlayer = mediaPlayer.create(this,R.raw.ring);
        mediaPlayer.start();



        //创建一个闹钟提醒的对话框，点击关闭铃声与页面

        new AlertDialog.Builder(ClockActivity.this)
                .setTitle("闹钟").setMessage("主人到时间了~")
                .setPositiveButton("关闭闹钟", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mediaPlayer.stop();
                        ClockActivity.this.finish();
                    }
                }).show();

    }
}
