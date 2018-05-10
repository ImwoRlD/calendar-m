package cc.yy.calendar.adapter;

import java.util.List;

//import net.blogjava.mobile.calendar.R;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import cc.yy.calendar.R;
import cc.yy.calendar.db.DBOper;

public class NoteAdapter extends BaseAdapter {
    private List<String> items;
    private List<String> times;
    private List<String> ids;
    private LayoutInflater inflater;
    private DBOper db;
    private Context context;

    public NoteAdapter(Context context, List<String> items, List<String> times,
                       List<String> ids) {
        inflater = LayoutInflater.from(context);
        db = new DBOper(context);
        this.items = items;
        this.times = times;
        this.ids = ids;
        this.context = context;
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup group) {
        ViewHolder holder = new ViewHolder();
        if (view == null) {
            // view = inflater.inflate(R.layout.note_list_item, null);
            view = inflater.inflate(R.layout.note_list_item, null);
            holder.title = (TextView) view.findViewById(R.id.note_title);
            holder.createtime = (TextView) view
                    .findViewById(R.id.note_createtime);
//            holder.clock = (ImageView) view.findViewById(R.id.note_clock);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String title = items.get(position);
        title = title.length() > 10 ? title.substring(0, 10) + "..." : title;
        holder.title.setText(title);
        holder.createtime.setText(times.get(position));
        Cursor cursor = db.getClock(ids.get(position));
        if (cursor.moveToFirst()) {
            String isOpen = cursor.getString(cursor
                    .getColumnIndex(DBOper.CLOCK_ISOPEN));
            if (isOpen.equals(context.getResources().getString(R.string.yes))) {
//                holder.clock.setImageResource(R.drawable.clock);
            }
        }

        return view;
    }

    private class ViewHolder {
        private TextView title;
        private TextView createtime;
        private ImageView clock;
    }

}
