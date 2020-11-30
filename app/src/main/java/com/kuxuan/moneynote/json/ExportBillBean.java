package com.kuxuan.moneynote.json;

/**
 * Created by Allence on 2018/3/16 0016.
 */

public class ExportBillBean {


    String email;
    String start_time;
    String end_time;
//    String time;
//    String sign;

    public ExportBillBean(String email, String start_time, String end_time) {
        this.email = email;
        this.start_time = start_time;
        this.end_time = end_time;
//        this.time = time;
//        this.sign = sign;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public String getSign() {
//        return sign;
//    }
//
//    public void setSign(String sign) {
//        this.sign = sign;
//    }
}
