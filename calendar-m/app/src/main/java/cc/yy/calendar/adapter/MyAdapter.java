package cc.yy.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cc.yy.calendar.R;
import cc.yy.calendar.activity.Note;

public class MyAdapter extends BaseAdapter {

    private List<Note> noteList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int index;


    public MyAdapter(Context context, List<Note> noteList, ListView listView){
        this.mInflater=LayoutInflater.from(context);
        this.noteList = noteList;
        this.mContext=context;
    }

    public int getCount(){
        return noteList.size();
    }

    @Override
    public Object getItem(int i) {
        return noteList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(convertView ==null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item,null);
            viewHolder.mTime = (TextView)convertView.findViewById(R.id.show_time);
            viewHolder.mContent = (TextView)convertView.findViewById(R.id.show_content);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();

        }
        viewHolder.mTime.setText(noteList.get(i).getTime());
        viewHolder.mContent.setText(noteList.get(i).getContent());

        index = i;

        return convertView;
    }


    //使用ViewHolder，来使ListView滚动时不必每次重建对象，提升性能
    class ViewHolder{
        public TextView mTime;
        public TextView mContent;
    }

}
