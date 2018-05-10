package cc.yy.calendar.util

import cc.yy.calendar.BuildConfig
import cc.yy.calendar.R

/**
 * Created by zpy on 2018/3/14.
 */
class Constant {
    companion object {
        const val SP_PATH_FOR_BACKGROUND = "pathForBackground"
        const val SP_ACTION_SET_BACKGROUND = "actionSetBackground"
        const val SP_SEX = "sex" //0代表男，1代表女
        const val SP_MAIN_BACKGROUND_INDEX = "main_background_index"

        const val RESULT_CODE_OK = 200
        const val REQUEST_CODE_FOR_SELECT_SEX = 1
        val MAIN_BACKGROUND_RES = mutableListOf(R.drawable.bg_0, R.drawable.bg_1, R.drawable.bg_2, R.drawable.bg_3, R.drawable.bg_4, R.drawable.bg_5)
        const val MESSENGER_INTENT_KEY = "${BuildConfig.APPLICATION_ID}.MESSENGER_INTENT_KEY"
        const val WORK_DURATION_KEY = "${BuildConfig.APPLICATION_ID}.WORK_DURATION_KEY"
        const val CONSTELLATION_JSON = "{\"status\":\"0\",\"msg\":\"ok\",\"result\":[{\"astroid\":\"1\",\"astroname\":\"白羊座\",\"date\":\"3-21~4-19\",\"pic\":\"http://api.jisuapi.com/astro/static/images/baiyang.png\"},{\"astroid\":\"2\",\"astroname\":\"金牛座\",\"date\":\"4-20~5-20\",\"pic\":\"http://api.jisuapi.com/astro/static/images/jinniu.png\"},{\"astroid\":\"3\",\"astroname\":\"双子座\",\"date\":\"5-21~6-21\",\"pic\":\"http://api.jisuapi.com/astro/static/images/shuangzi.png\"},{\"astroid\":\"4\",\"astroname\":\"巨蟹座\",\"date\":\"6-22~7-22\",\"pic\":\"http://api.jisuapi.com/astro/static/images/juxie.png\"},{\"astroid\":\"5\",\"astroname\":\"狮子座\",\"date\":\"7-23~8-22\",\"pic\":\"http://api.jisuapi.com/astro/static/images/shizi.png\"},{\"astroid\":\"6\",\"astroname\":\"处女座\",\"date\":\"8-23~9-22\",\"pic\":\"http://api.jisuapi.com/astro/static/images/chunv.png\"},{\"astroid\":\"7\",\"astroname\":\"天秤座\",\"date\":\"9-23~10-23\",\"pic\":\"http://api.jisuapi.com/astro/static/images/tianping.png\"},{\"astroid\":\"8\",\"astroname\":\"天蝎座\",\"date\":\"10-24~11-22\",\"pic\":\"http://api.jisuapi.com/astro/static/images/tianxie.png\"},{\"astroid\":\"9\",\"astroname\":\"射手座\",\"date\":\"11-23~12-21\",\"pic\":\"http://api.jisuapi.com/astro/static/images/sheshou.png\"},{\"astroid\":\"10\",\"astroname\":\"摩羯座\",\"date\":\"12-22~1-19\",\"pic\":\"http://api.jisuapi.com/astro/static/images/mojie.png\"},{\"astroid\":\"11\",\"astroname\":\"水瓶座\",\"date\":\"1-20~2-18\",\"pic\":\"http://api.jisuapi.com/astro/static/images/shuiping.png\"},{\"astroid\":\"12\",\"astroname\":\"双鱼座\",\"date\":\"2-19~3-20\",\"pic\":\"http://api.jisuapi.com/astro/static/images/shuangyu.png\"}]}"
    }
}