package cc.yy.calendar.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import cc.yy.calendar.R
import cc.yy.calendar.util.Constant
import kotlinx.android.synthetic.main.activity_select_sex.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sp

/**
 * Created by zpy on 2018/4/16.
 */
class SelectSexActivity : BaseActivity() {
    override fun getLayout(): Int = R.layout.activity_select_sex
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        initListener()
        tv_title.text = "选择性别"
        cwt_male.txt = "男"
        cwt_male.bgColor = Color.BLUE
        cwt_male.backgroundColor = Color.TRANSPARENT
        cwt_male.textColor = Color.WHITE
        cwt_male.textSize = sp(18).toFloat()


        cwt_female.txt = "女"
        cwt_female.bgColor = Color.RED
        cwt_female.backgroundColor = Color.TRANSPARENT
        cwt_female.textColor = Color.WHITE
        cwt_female.textSize = sp(18).toFloat()
    }

    private fun initListener() {
        cwt_male.setOnClickListener {
            val sex = 0
            app.putSpValue(Constant.SP_SEX, sex)
            val index = app.getSpValue(Constant.SP_MAIN_BACKGROUND_INDEX, -1)
            if (-1 == index) {
                app.putSpValue(Constant.SP_MAIN_BACKGROUND_INDEX, sex)
            }
            val data = Intent()
            data.putExtra("sex", sex)
            setResult(Constant.RESULT_CODE_OK, data)
            this@SelectSexActivity.finish()
        }
        cwt_female.setOnClickListener {
            val sex = 1
            app.putSpValue(Constant.SP_SEX, sex)
            val index = app.getSpValue(Constant.SP_MAIN_BACKGROUND_INDEX, -1)
            if (-1 == index) {
                app.putSpValue(Constant.SP_MAIN_BACKGROUND_INDEX, sex)
            }
            val data = Intent()
            data.putExtra("sex", sex)
            setResult(Constant.RESULT_CODE_OK, data)
            this@SelectSexActivity.finish()
        }
    }
}