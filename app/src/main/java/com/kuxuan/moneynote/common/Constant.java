package com.kuxuan.moneynote.common;

/**
 * Created by xieshengqi on 2017/10/18.
 */

public class Constant {

    //手机号正则，11位手机号
    public static final String REGEX_MOBILE = "[1][3,4,5,7,8][0-9]{9}$";
    //测试接口
//    public static final String BASE_URL = "http://new.bill.quyaqu.com/api/V1/";
    public static final String BASE_URL = "https://accountapi.726p.com/api/V1/";
    //获取token必须使用这个url
    public static final String TOKEN_BASE_URL = "https://apimoney.726p.com/api/";
//    public static final String TOKEN_BASE_URL = "http://bill.quyaqu.com/api/";
    //    public static final String BASE_URL = "http://bill.quyaqu.com/api/";
//    public static final String BASE_URL = "http://web6.money.726p.com/api/";
    //正式
//    public static final String BASE_URL = "https://apimoney.726p.com/api/";
    //    public static final String BASE_URL = "http://api.money.quyaqu.com/api/";
    public static final String MD5 = "b4b80c2676828f1df375684100f56d48";

    public static final long TOKEN_FAIL_DAY = 7 * 24 * 60 * 60 * 1000L;
//    public static final long TOKEN_FAIL_DAY = 60*1000L;

    public static class UserInfo {
        public static final String TOKEN = "token";
        public static final String USER_INFO = "user_info";
        public static final String MOBILE = "mobile";
        public static final String ISLOGIN = "islogin";
        public static final String PHONE = "phone";
        public static final String PWD = "pwd";
        public static final String USER_ID = "user_id";
    }

    public static class System {
        //预算
        public static final String BUGET_NUM = "buget_num";
        //预算开关
        public static final String SWITCH = "switch";
        //默认预算
        public static final long NORMAL_NUM = 3000;


    }

    /**
     * 数据库（存储主键）
     */
    public static class DbInfo {
        public static final String DB_ID_COUNT = "db_id_count";
        //插入到多少
        public static final String DB_DOWNLOAD_INDEX = "db_download_index";
        //每次获取多少数据
        public static final int DB_DOWNLOAD_MAX_COUNT = 100;
        /**
         * 用户集合
         */
        public static final String DB_USER_LISTS = "db_user_lists";

    }

    public static class Time {
        public static final String SEND_TIME = "send_time";
        //token失效时间
        public static final String TOKEN_FAIL = "token_fail";
    }


    public static class IsFirstWEiChatLogin {

        public static final String ISFIRSTWEICHATLOGIN = "isfirstweichatlogin";

        public static final String NEWUSER = "new_user";

        public static final String ISWEICHATLOGIN = "isWeiChatLogin";

        public static final String IsLoginOut = "isloginout";

        public static final String ISWECHATNEWUSERFIRSTLOGIN = "iswechatnewuserfirstlogin";

    }


    public static class Online_OR_Offline {

        public static final int ONLINE = 0;
        public static final int OFFLINE = 1;

    }


    public static class CategoreyDBConstant {

        public static final String ISINSERT = "isInsert";


    }


    /**
     * 皮肤
     */
    public static class Skin {

        public static final String CHECKED = "checked";

        public static final String LOADED = "loaded";
        //选中的颜色
        public static final String COLOR_SELECT = "select_color";
        //默认颜色
        public static final String COLOR_NORMAL = "fed953";

        public static final String HOMEPICNAME = "HOME";

        public static final String MINE = "mine";

        public static final String SKINICON = "skinicon";


        public static final String CUTPIC = "cutpic";

    }


    public static class Code_Request {
        public static final int SUCCESS_CODE = 0;
        /**
         * 请求码
         */
        public final static int REQUESTCODE = 0x11;
        /**
         * 响应码
         */
        public final static int RESULTCODE = 0x10;
    }

    /**
     * 图表数据
     */
    public static class ChartInfo {
        public static final String STARTYEAR = "startYear";
        public static final String STARTMONTH = "startMonth";
        public static final String ENDYEAR = "endYear";
        public static final String ENDMONTH = "endMonth";
    }

}
