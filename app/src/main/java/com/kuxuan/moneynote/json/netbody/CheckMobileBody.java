package com.kuxuan.moneynote.json.netbody;

import java.io.Serializable;

/**
 * Created by xieshengqi on 2017/10/31.
 */

public class CheckMobileBody implements Serializable{
    private String mobile;
    private String check_type;


    public CheckMobileBody(String mobile, String check_type) {
        this.mobile = mobile;
        this.check_type = check_type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCheck_type() {
        return check_type;
    }

    public void setCheck_type(String check_type) {
        this.check_type = check_type;
    }
}
