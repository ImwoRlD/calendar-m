package cc.yy.calendar.activity

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import cc.yy.calendar.R
import cc.yy.calendar.adapter.OnItemClickListener
import cc.yy.calendar.adapter.SelectImageAdapter
import cc.yy.calendar.util.Constant
import kotlinx.android.synthetic.main.activity_select_image.*

/**
 * Created by zpy on 2018/4/16.
 */
class ImageSelectActivity : BaseActivity() {
    override fun getLayout(): Int = R.layout.activity_select_image
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        val gridLayoutManager = GridLayoutManager(this@ImageSelectActivity, 2)
        rv_images.layoutManager = gridLayoutManager
        val adapter = SelectImageAdapter(Constant.MAIN_BACKGROUND_RES)
        adapter.onItemClickListener = object : OnItemClickListener {
            override fun itemClick(position: Int, type: Int) {
                app.putSpValue(Constant.SP_MAIN_BACKGROUND_INDEX, position)
                this@ImageSelectActivity.finish()
            }
        }
        rv_images.adapter = adapter
    }

}