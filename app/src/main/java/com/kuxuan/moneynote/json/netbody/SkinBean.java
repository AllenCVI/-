package com.kuxuan.moneynote.json.netbody;

import java.util.List;

/**
 * Created by Allence on 2018/4/25 0025.
 */

public class SkinBean {

//    {
//        "res": [
//        {
//               "banner": "http://adminsuishouji.oss-cn-beijing.aliyuncs.com/Uploads/20171228/1514446901392766.png",
//                "color": "0x578FFA ",
//                "mine_banner": "http://adminsuishouji.oss-cn-beijing.aliyuncs.com/Uploads/20171227/1514346854943974.png",
//                "preview_img": "http://adminsuishouji.oss-cn-beijing.aliyuncs.com/Uploads/20171228/1514448652986807.png"
//        }
//    ],
//        "message": [
//        "成功"
//    ],
//        "code": 0
//    }

    List<String> message;
    int code;
    List<RES> res;


    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<RES> getRes() {
        return res;
    }

    public void setRes(List<RES> res) {
        this.res = res;
    }
}
