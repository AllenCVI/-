package com.kuxuan.moneynote.json;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xieshengqi on 2017/10/25.
 */

public class TimeDataJson implements Serializable{


    /**
     * account : 1000
     * day : 2017-10-23
     */

    private double account;
    private String time;
    private ArrayList<PopCharData> data;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<PopCharData> getData() {
        return data;
    }

    public void setData(ArrayList<PopCharData> data) {
        this.data = data;
    }

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }

    public String getDay() {
        return time;
    }

    public void setDay(String day) {
        this.time = day;
    }
}
