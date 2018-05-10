package cc.yy.calendar.activity;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cc.yy.calendar.activity.NoteDB;
import cc.yy.calendar.activity.Note;

import cc.yy.calendar.R;
import cc.yy.calendar.db.DBHelper;
import cc.yy.calendar.adapter.MyAdapter;


public class MainActivity2 extends Activity {

    //布局控件
    private TextView mTitle;
    private TextView mNoteNum;
    private Button mWrite;
    private ListView mNoteListView;
    private Button mAbout;

    //数据库实例，数据源

    private List<Note> mNoteList = new ArrayList<Note>();
    private NoteDB mNoteDB;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);


        initView();
        new NewAsyncTask().execute();
        initEvent();
    }

    private void initEvent() {
        //新建一条备忘录
        mWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });


        //修改或查看一条已有的备忘录

        mNoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note note = (Note)adapterView.getItemAtPosition(i);
                Intent intent = new Intent(MainActivity2.this,UpdateOrReadActivity.class);
                intent.putExtra("note_id",note.getId());
                startActivity(intent);
            }
        });

        //ListView长按删除
        mNoteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Note note = (Note) parent.getItemAtPosition(position);

                //长按提示是否删除

                new AlertDialog.Builder(MainActivity2.this)
                        .setTitle("提示")
                        .setMessage("确定删除这条记录吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new DeleteAsyncTask(mNoteDB).execute();
                                new NewAsyncTask().execute();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
        });


        //关于自己
        mAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, AboutActivity.class);
                startActivity(intent);
            }
        });

    }


    public void initView() {
        //布局控件初始化

        mTitle = (TextView) findViewById(R.id.app_title);
        //画TextView文字下的下划线
        mNoteNum = (TextView) findViewById(R.id.note_num);
        mWrite = (Button) findViewById(R.id.write_btn);
        mNoteListView = (ListView) findViewById(R.id.listview);
        mAbout = (Button) findViewById(R.id.about_btn);

        //获取数据库实例
        mNoteDB = NoteDB.getInstance(this);

    }

        //异步加载备忘录
        class NewAsyncTask extends AsyncTask<Void, Void, List<Note>> {


            @Override
            protected List<Note> doInBackground(Void... voids) {
                mNoteList = mNoteDB.loadNotes();
                return mNoteList;
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);

                //设置适配器，绑定适配器

                MyAdapter myAdapter = new MyAdapter(MainActivity2.this, notes,
                        mNoteListView);
                mNoteListView.setAdapter(myAdapter);

                //更新呢备忘录数

                int temp = mNoteList.size();
                mNoteNum.setText("共" + temp + "条被备忘录");
            }
        }




    //当活动恢复时，刷新ListView和备忘录记录数


    @Override
    protected void onResume() {
        super.onResume();
        new NewAsyncTask().execute();
    }

}
