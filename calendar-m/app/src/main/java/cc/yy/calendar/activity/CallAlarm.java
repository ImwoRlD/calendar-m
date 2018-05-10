package cc.yy.calendar.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;


import cc.yy.calendar.db.DBOper;
import cc.yy.calendar.util.Logger;
import cc.yy.calendar.util.Util;

public class CallAlarm extends BroadcastReceiver{

    private Logger log;
    private DBOper db;


    @Override
    public void onReceive(Context context, Intent intent) {
        db = new DBOper(context);
        log = new Logger(CallAlarm.class);
        String id = intent.getStringExtra(DBOper.CLOCK_ID);
        log.info("id==================="+id);
        Cursor cursor = db.getNote(id);
        Cursor cursor1 = db.getClock(id);
        if(!cursor.moveToFirst()||!cursor1.moveToFirst()) return;
        String text = cursor.getString(cursor.getColumnIndex(DBOper.FIELD_CONTENT));
        String uri = cursor1.getString(cursor1.getColumnIndex(DBOper.CLOCK_URI));
        String isVabrate = cursor1.getString(cursor1.getColumnIndex(DBOper.CLOCK_ISVABRATE));
        String date = cursor1.getString(cursor1.getColumnIndex(DBOper.CLOCK_DATE));
        String time = cursor1.getString(cursor1.getColumnIndex(DBOper.CLOCK_TIME));
        if(date!=null&&!date.equals("")){
            if(!date.equals(Util.getDateString()))return;
        }
        Intent i = new Intent(context,AlarmAlert.class);
        i.putExtra(DBOper.CLOCK_ID, id);
        i.putExtra(DBOper.FIELD_CONTENT, text);
        i.putExtra(DBOper.CLOCK_ISVABRATE, isVabrate);
        i.putExtra(DBOper.CLOCK_URI, uri);
        i.putExtra(DBOper.CLOCK_TIME, time);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}

