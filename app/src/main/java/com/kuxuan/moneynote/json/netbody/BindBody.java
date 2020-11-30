package com.kuxuan.moneynote.json.netbody;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class BindBody {
    private String mobile;
    private String password;
    private String c_password;

    public BindBody(String mobile, String password, String c_password) {
        this.mobile = mobile;
        this.password = password;
        this.c_password = c_password;
    }
}
