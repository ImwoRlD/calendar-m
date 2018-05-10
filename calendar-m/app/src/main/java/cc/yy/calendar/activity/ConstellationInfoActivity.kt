package cc.yy.calendar.activity

import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import cc.yy.calendar.R
import cc.yy.calendar.bean.ConstellactionInfo
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_constellaction_info.*
import org.jetbrains.anko.toast
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x
import java.util.*

/**
 * Created by zpy on 2018/4/18.
 */
class ConstellationInfoActivity : BaseActivity() {
    override fun getLayout(): Int = R.layout.activity_constellaction_info
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        show()
    }

    private fun show() {
        val astroid = intent.getStringExtra("astroid")
        val date = DateFormat.format("yyyy-MM-dd", Date()).toString()
        val params = RequestParams("http://api.jisuapi.com/astro/fortune?astroid=1&date=2016-01-19&appkey=d38a05e25a6e03be")
        params.addBodyParameter("astroid", astroid)
        params.addBodyParameter("date", date)
        params.addBodyParameter("appkey", "d38a05e25a6e03be")
        x.http().get(params, object : Callback.CacheCallback<String> {
            override fun onCache(result: String?): Boolean {
                parseJson(result)
                return true
            }

            override fun onFinished() {

            }

            override fun onSuccess(result: String?) {
                parseJson(result)
            }

            override fun onCancelled(cex: Callback.CancelledException?) {
            }

            override fun onError(ex: Throwable?, isOnCallback: Boolean) {
                toast("出错了")
                ll_main.visibility = View.INVISIBLE
            }
        })
    }

    private fun parseJson(result: String?) {
        ll_main.visibility = View.VISIBLE
        val gson = Gson()
        val constellactionInfo = gson.fromJson<ConstellactionInfo>(result, ConstellactionInfo::class.java)
        tv_constellation_name.text = String.format("星座：%s", constellactionInfo.result.astroname)
        tv_color.text = String.format("幸运色：%s", constellactionInfo.result.today.color)
        tv_num.text = String.format("幸运数字：%s", constellactionInfo.result.today.number)
        tv_money.text = String.format("财运指数：%s", constellactionInfo.result.today.money)
        tv_career.text = String.format("事业指数：%s", constellactionInfo.result.today.career)
        tv_love.text = String.format("爱情指数：%s", constellactionInfo.result.today.love)
        tv_health.text = String.format("健康指数：%s", constellactionInfo.result.today.health)
        tv_pre_summary.text = String.format("预测：%s", constellactionInfo.result.today.presummary)
    }
}