package cc.yy.calendar.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import cc.yy.calendar.R
import cc.yy.calendar.getScreenHeight
import kotlinx.android.synthetic.main.layout_select_image_item.view.*

/**
 * Created by zpy on 2018/4/16.
 */
class SelectImageAdapter(var data: MutableList<Int>) : RecyclerView.Adapter<SelectImageAdapter.ViewHolder>() {
    var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(parent.inflate(R.layout.layout_select_image_item))
        viewHolder.itemClickListen { position, type ->
            onItemClickListener?.itemClick(position, type)
        }
        return viewHolder
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    //滚动的时候快速设置值，不必每次都重新创建很多对象，提升性能。
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) = with(itemView) {
            val lp = iv_item.layoutParams as FrameLayout.LayoutParams
            lp.height = context.getScreenHeight() / 2
            iv_item.setImageResource(data[position])
        }
    }
}

interface OnItemClickListener {
    fun itemClick(position: Int, type: Int)
}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun <T : RecyclerView.ViewHolder> T.itemClickListen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(adapterPosition, itemViewType)
    }
    return this
}