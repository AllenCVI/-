package com.kuxuan.moneynote.json.netbody;

/**
 * Created by xieshengqi on 2018/5/4.
 */

public class SendCodeBody {


   private String mobile;
    private String codeType;
    private String send_type;


    public SendCodeBody(String mobile, String codeType, String timestamps) {
        this.mobile = mobile;
        this.codeType = codeType;
        this.send_type = timestamps;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getTimestamps() {
        return send_type;
    }

    public void setTimestamps(String timestamps) {
        this.send_type = timestamps;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    @Override
    public String toString() {
        return "SendCodeBody{" +
                "mobile='" + mobile + '\'' +
                ", codeType='" + codeType + '\'' +
                ", send_type='" + send_type + '\'' +
                '}';
    }
}
