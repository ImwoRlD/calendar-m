package cc.yy.calendar.activity

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import cc.yy.calendar.R
import cc.yy.calendar.adapter.ConstellationAdapter
import cc.yy.calendar.adapter.OnItemClickListener
import cc.yy.calendar.bean.ConstellationBean
import cc.yy.calendar.util.Constant
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_constellation.*
import org.jetbrains.anko.startActivity

/**
 * Created by zpy on 2018/4/17.
 */
class ConstellationActivity : BaseActivity() {
    override fun getLayout(): Int = R.layout.activity_constellation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        val gridLayoutManager = GridLayoutManager(this@ConstellationActivity, 3)
        rv_constellation.layoutManager = gridLayoutManager
        val gson = Gson()
        val constellationBean = gson.fromJson<ConstellationBean>(Constant.CONSTELLATION_JSON, ConstellationBean::class.java)
        val adapter = ConstellationAdapter(constellationBean.result)
        adapter.onItemClickListener = object : OnItemClickListener {
            override fun itemClick(position: Int, type: Int) {
                val astroid = constellationBean.result[position].astroid
                startActivity<ConstellationInfoActivity>("astroid" to astroid)
            }
        }
        rv_constellation.adapter = adapter
    }
}