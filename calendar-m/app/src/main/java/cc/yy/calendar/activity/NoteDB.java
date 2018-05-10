package cc.yy.calendar.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cc.yy.calendar.activity.Note;
import cc.yy.calendar.db.DBHelper;
import cc.yy.calendar.db.DBOper;

public class NoteDB {
    public static final String DB_NAME="notepad";
    public static final int VERSION = 1;
    private static NoteDB mNoteDB;
    private SQLiteDatabase db;

    public NoteDB(Context context){
        DBHelper dbHelper = new DBHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
    }


    //获取NoteDB的实例
    public synchronized static NoteDB getInstance(Context context){
        if(mNoteDB == null){
            mNoteDB = new NoteDB(context);
        }
        return mNoteDB;
    }

    public void saveNote(Note note){
        if(note !=null){
            ContentValues values = new ContentValues();
            values.put("content",note.getContent());
            values.put("time",note.getTime());
            db.insert("Note",null,values);
        }
    }


    public List<Note> loadNotes(){
        List<Note> noteList = new ArrayList<Note>();

        //先按时间降序排列，再按id降序排列
        Cursor cursor = db.query("Note",null,null,null,
                null,null,"time desc,id desc");
        if(cursor.moveToNext()){
            do{
                Note note = new Note();
               note.setId(cursor.getInt(cursor.getColumnIndex("id")));
               note.setContent(cursor.getString(cursor.getColumnIndex("content")));
               note.setTime(cursor.getString(cursor.getColumnIndex("time")));
            }while (cursor.moveToNext());
        }
        return noteList;
    }


    public Note loadById(int id){
        Note note = null;
        Cursor cursor = db.query("Note",null,"id ="+id,
                null,null,null,null);
        if(cursor.moveToNext()){
            note = new Note();
            note.setContent(cursor.getString(cursor.getColumnIndex("content")));
            note.setTime(cursor.getString(cursor.getColumnIndex("time")));
        }
        return  note;
    }

    public void deleteById(Integer id){
        db.delete("Note","id="+id,null);
    }

    public void deleteAllNote(){
        db.delete("Note",null,null);
    }


    public void updateById(String noteTime,String noteContent,int noteId){
        ContentValues values = new ContentValues();
        values.put("content",noteContent);
        values.put("time",noteTime);
        db.update("Note",values,"id="+noteId,null);
    }


}
