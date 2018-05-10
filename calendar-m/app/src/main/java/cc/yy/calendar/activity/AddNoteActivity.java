package cc.yy.calendar.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import cc.yy.calendar.activity.Note;


import java.text.SimpleDateFormat;
import java.util.Date;

import cc.yy.calendar.R;

public class AddNoteActivity extends Activity {
    //布局控件
    private TextView mComplete;
    private ImageButton mBackBtn;
    private EditText mContent;

    //备忘录数据
    private String noteTime;
    private String noteContent;

    //数据库
    private NoteDB mNoteDB;
    private Note note;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.write_note);

        initView();
        initEvent();
    }


    private void initView(){
        //布局空间初始化
        mComplete=(TextView)findViewById(R.id.complete_btn);
        mBackBtn = (ImageButton)findViewById(R.id.back_btn);
        mContent = (EditText) findViewById(R.id.note_content);

        //获取数据库实例

        mNoteDB = NoteDB.getInstance(this);
    }


    //事件处理
    private void initEvent(){
        //返回上一级菜单，如果有内容，提示是否保存，
        //是就保存，销毁活动，否则直接销毁活动

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataOrNot();
            }
        });


        //完成按钮，保存备忘录到数据库

        mComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mContent.getText().toString().equals("")){
                    new AddAsyncTask().execute();
                    finish();
                }else {
                    finish();
                }
            }
        });
    }


    //根据是否与内容，提示保存
    private void saveDataOrNot(){
        if(!mContent.getText().toString().trim().equals("")){
            new AlertDialog.Builder(AddNoteActivity.this)
                    .setTitle("提示")
                    .setMessage("需要保存您编辑的内容吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new AddAsyncTask().execute();
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }else {
            finish();
        }
    }



    //添加数据到数据库
    class AddAsyncTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            mNoteDB.saveNote(note);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //记录数据
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");

            //获取系统当前时间
            Date date = new Date(System.currentTimeMillis());
            noteTime = sdf.format(date);
            noteContent = mContent.getText().toString();
            note = new Note();
            note.setTime(noteTime);
            note.setContent(noteContent);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(AddNoteActivity.this,"保存成功",Toast.LENGTH_SHORT).show();

        }
    }

    //按返回键，有内容时，提示保存


    @Override
    public void onBackPressed() {
        saveDataOrNot();
    }
}
