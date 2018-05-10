package cc.yy.calendar.bean;

import java.util.List;

/**
 * Created by zpy on 2018/4/17.
 */

public class ConstellationBean {

    /**
     * status : 0
     * msg : ok
     * result : [{"astroid":"1","astroname":"白羊座","date":"3-21~4-19","pic":"http://api.jisuapi.com/astro/static/images/baiyang.png"},{"astroid":"2","astroname":"金牛座","date":"4-20~5-20","pic":"http://api.jisuapi.com/astro/static/images/jinniu.png"},{"astroid":"3","astroname":"双子座","date":"5-21~6-21","pic":"http://api.jisuapi.com/astro/static/images/shuangzi.png"},{"astroid":"4","astroname":"巨蟹座","date":"6-22~7-22","pic":"http://api.jisuapi.com/astro/static/images/juxie.png"},{"astroid":"5","astroname":"狮子座","date":"7-23~8-22","pic":"http://api.jisuapi.com/astro/static/images/shizi.png"},{"astroid":"6","astroname":"处女座","date":"8-23~9-22","pic":"http://api.jisuapi.com/astro/static/images/chunv.png"},{"astroid":"7","astroname":"天秤座","date":"9-23~10-23","pic":"http://api.jisuapi.com/astro/static/images/tianping.png"},{"astroid":"8","astroname":"天蝎座","date":"10-24~11-22","pic":"http://api.jisuapi.com/astro/static/images/tianxie.png"},{"astroid":"9","astroname":"射手座","date":"11-23~12-21","pic":"http://api.jisuapi.com/astro/static/images/sheshou.png"},{"astroid":"10","astroname":"摩羯座","date":"12-22~1-19","pic":"http://api.jisuapi.com/astro/static/images/mojie.png"},{"astroid":"11","astroname":"水瓶座","date":"1-20~2-18","pic":"http://api.jisuapi.com/astro/static/images/shuiping.png"},{"astroid":"12","astroname":"双鱼座","date":"2-19~3-20","pic":"http://api.jisuapi.com/astro/static/images/shuangyu.png"}]
     */

    private String status;
    private String msg;
    private List<ResultBean> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * astroid : 1
         * astroname : 白羊座
         * date : 3-21~4-19
         * pic : http://api.jisuapi.com/astro/static/images/baiyang.png
         */

        private String astroid;
        private String astroname;
        private String date;
        private String pic;

        public String getAstroid() {
            return astroid;
        }

        public void setAstroid(String astroid) {
            this.astroid = astroid;
        }

        public String getAstroname() {
            return astroname;
        }

        public void setAstroname(String astroname) {
            this.astroname = astroname;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
