package com.kuxuan.moneynote.json.netbody;

import java.io.Serializable;

/**
 * Created by xieshengqi on 2017/10/24.
 */

public class RegisterBody implements Serializable{


    private String mobile;
    private String password;
    private String c_password;
    private String code;

    public RegisterBody(String mobile, String password, String c_password, String code) {
        this.mobile = mobile;
        this.password = password;
        this.c_password = c_password;
        this.code = code;
    }

    public RegisterBody(String mobile, String password, String c_password) {
        this.mobile = mobile;
        this.password = password;
        this.c_password = c_password;
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

    public String getC_password() {
        return c_password;
    }

    public void setC_password(String c_password) {
        this.c_password = c_password;
    }
}
