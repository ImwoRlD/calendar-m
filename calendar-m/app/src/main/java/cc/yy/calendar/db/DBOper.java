package cc.yy.calendar.db;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.flask.colorpicker.Utils;

public class DBOper extends SQLiteOpenHelper{

    private final static String DATABASE_NAME = "note_db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "notepad";
    public final static String FIELD_ID = "_id";
    public final static String FIELD_CONTENT = "content";
    public final static String FIELD_CREATETIME = "createtime";

    public final static String CLOCK_TABLE_NAME = "clock";
    public final static String CLOCK_ID = "_id";
    public final static String CLOCK_ISOPEN = "isopen";
    public final static String CLOCK_DATE = "date";
    public final static String CLOCK_TIME = "time";
    public final static String CLOCK_ISREPEAT = "isrepeat";
    public final static String CLOCK_ISVABRATE = "isvabrate";
    public final static String CLOCK_RINGS = "rings";
    public final static String CLOCK_URI = "uri";

    public final static String LOGIN_TABLE_NAME = "login";
    public final static String LOGIN_USER = "admin";
    public final static String LOGIN_PWD = "password";

    public DBOper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }




    //创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table "+TABLE_NAME+" ("
                +FIELD_ID+" integer primary key autoincrement, "
                +FIELD_CONTENT+" text, "
                +FIELD_CREATETIME+" text )";
        db.execSQL(sql);
        sql = "create table "+CLOCK_TABLE_NAME+" ("
                +CLOCK_ID+" integer primary key, "
                +CLOCK_ISOPEN+" text, "
                +CLOCK_DATE+" text, "
                +CLOCK_TIME+" text, "
                +CLOCK_ISREPEAT+" text, "
                +CLOCK_ISVABRATE+" text, "
                +CLOCK_RINGS+" text, "
                +CLOCK_URI+" text )";
        db.execSQL(sql);

        sql = "create table "+LOGIN_TABLE_NAME+" ("
                +LOGIN_USER+" text, "
                +LOGIN_PWD+" text )";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists "+TABLE_NAME;
        db.execSQL(sql);//如果旧表存在，删除，所以数据会消失

        sql = "drop table if exists "+CLOCK_TABLE_NAME;
        db.execSQL(sql);

        sql = "drop table if exists "+LOGIN_TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);//再次创建表
    }


    public Cursor selectNotes(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public long insertNote(String text){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_CONTENT,text);
//        cv.put(FIELD_CREATETIME, U);
        return db.insert(TABLE_NAME,null,cv);
    }

    public void deleteNote(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = FIELD_ID+"=?";
        String[] whereValues = {id};
        db.delete(TABLE_NAME,where,whereValues);
    }


    public int updateNote(String id,String text){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = FIELD_ID+"=?";
        String[] whereValues = {id};
        ContentValues cv = new ContentValues();
        cv.put(FIELD_CONTENT, text);
        return db.update(TABLE_NAME, cv, where, whereValues);
    }


    public Cursor getNote(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String where = FIELD_ID+"=?";
        String[] whereValues = {id};
        Cursor cursor = db.query(TABLE_NAME, null, where, whereValues, null, null, null);
        return cursor;
    }


    public long insertClock(ContentValues cv){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(CLOCK_TABLE_NAME, null, cv);
    }

    public void deleteClock(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = CLOCK_ID+"=?";
        String[] whereValues = {id};
        db.delete(CLOCK_TABLE_NAME, where, whereValues);
    }

    public int updateClock(String id,ContentValues cv){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = CLOCK_ID+"=?";
        String[] whereValues = {id};
        return db.update(CLOCK_TABLE_NAME, cv, where, whereValues);
    }

    public Cursor getClock(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String where = CLOCK_ID+"=?";
        String[] whereValues = {id};
        Cursor cursor = db.query(CLOCK_TABLE_NAME, null, where, whereValues, null, null, null);
        return cursor;
    }

    public long insertPwd(String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LOGIN_USER, LOGIN_USER);
        cv.put(LOGIN_PWD, password);
        return db.insert(LOGIN_TABLE_NAME, null, cv);
    }

    public int updatePwd(String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = LOGIN_USER+"=?";
        String[] whereValues = {LOGIN_USER};
        ContentValues cv = new ContentValues();
        cv.put(LOGIN_PWD, password);
        return db.update(LOGIN_TABLE_NAME, cv, where, whereValues);
    }



    public String getPwd(){
        SQLiteDatabase db = this.getReadableDatabase();
        String where = LOGIN_USER+"=?";
        String[] whereValues = {LOGIN_USER};
        Cursor cursor = db.query(LOGIN_TABLE_NAME, null, where, whereValues, null, null, null);
        if(cursor.moveToFirst()){
            return cursor.getString(cursor.getColumnIndex(LOGIN_PWD));
        }else{
            return "";
        }
    }




}
