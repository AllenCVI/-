package com.kuxuan.moneynote.json.netbody;

import java.io.Serializable;

/**
 * Created by xieshengqi on 2017/10/24.
 */

public class LoginBody implements Serializable {
    private String mobile;

    private String password;

//    private String time;


    public LoginBody(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;

//        time = System.currentTimeMillis()/1000+"";
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
