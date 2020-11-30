package com.kuxuan.moneynote.json;

import java.io.Serializable;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public class TimeJson implements Serializable{


    private int year;
    private int month;
    private int day;
    private String hh;
    private String mm;
    private String ss;
    private long currentTime;


    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public TimeJson(int year, int month, int day, String hh, String mm, String ss, long currentTime) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.currentTime = currentTime;
        this.hh = hh;
        this.mm = mm;
        this.ss = ss;
    }

    public TimeJson(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }


    public String getHh() {
        return hh;
    }

    public void setHh(String hh) {
        this.hh = hh;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
