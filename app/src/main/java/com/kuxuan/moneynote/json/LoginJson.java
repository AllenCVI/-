package com.kuxuan.moneynote.json;

import java.io.Serializable;

/**
 * 注册登录model
 * Created by xieshengqi on 2017/10/24.
 */

public class LoginJson implements Serializable {


    private String token;
    private String mobile;
    private int new_user;
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getNew_user() {
        return new_user;
    }

    public void setNew_user(int new_user) {
        this.new_user = new_user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return mobile;
    }

    public void setName(String name) {
        this.mobile = name;
    }
}
