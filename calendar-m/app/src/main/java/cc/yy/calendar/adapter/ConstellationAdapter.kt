package cc.yy.calendar.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import cc.yy.calendar.R
import cc.yy.calendar.bean.ConstellationBean
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.layout_constellation_item.view.*

/**
 * Created by zpy on 2018/4/16.
 */
class ConstellationAdapter(var data: MutableList<ConstellationBean.ResultBean>) : RecyclerView.Adapter<ConstellationAdapter.ViewHolder>() {
    var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(parent.inflate(R.layout.layout_constellation_item))//inflate是加载布局
        viewHolder.itemClickListen { position, type ->
            onItemClickListener?.itemClick(position, type)
        }
        return viewHolder
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) = with(itemView) {
            val resultBean = data[position]
            ImageLoader.getInstance().displayImage(resultBean.pic, iv_image)
            tv_date.text = resultBean.date
            tv_name.text = resultBean.astroname
        }
    }
}