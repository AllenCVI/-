package com.kuxuan.moneynote.json;

import java.io.Serializable;

/**
 * Created by xieshengqi on 2017/10/30.
 */

public class PopCharData implements Serializable{


    /**
     * account : 1200.00
     * category_id : 29
     * month : 10
     * name : 旅行
     * small_icon : http://182.92.118.1:8090/Uploads/20171026/1509001737264769.png
     */

    private String account;
    private int category_id;
    private int month;
    private String name;
    private String small_icon;
    private String date;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmall_icon() {
        return small_icon;
    }

    public void setSmall_icon(String small_icon) {
        this.small_icon = small_icon;
    }
}
