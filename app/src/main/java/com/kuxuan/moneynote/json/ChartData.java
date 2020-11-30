package com.kuxuan.moneynote.json;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xieshengqi on 2017/10/25.
 */

public class ChartData implements Serializable {

    /**
     * account : -1000
     * time : 本周
     * days : ["2017-10-23","2017-10-24","2017-10-25","2017-10-26","2017-10-27","2017-10-28","2017-10-29"]
     */

    public static final int MONTH = 2;
    public static final int YEAR = 3;
    public static final int WEEK = 1;


    /**
     * 是自己填充的还是网络上的
     */
    private boolean isTrueData;


    public boolean isTrueData() {
        return isTrueData;
    }

    public void setTrueData(boolean trueData) {
        isTrueData = trueData;
    }

    public void setAccount(float account) {
        this.account = account;
    }

    /**
     * 统计维度 1 周 2 月 3 年
     */
    private int statistic_type;
    private float account;
    private String time;
    private List<String> days;
    private List<TimeDataJson> time_data;
    private List<CategoryDataJson> category_data;

    public int getStatistic_type() {
        return statistic_type;
    }

    public void setStatistic_type(int statistic_type) {
        this.statistic_type = statistic_type;
    }

    public List<TimeDataJson> getTime_data() {
        return time_data;
    }

    public void setTime_data(List<TimeDataJson> time_data) {
        this.time_data = time_data;
    }

    public List<CategoryDataJson> getCategory_data() {
        return category_data;
    }

    public void setCategory_data(List<CategoryDataJson> category_data) {
        this.category_data = category_data;
    }

    public float getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getTime() {
        return time;
    }

    public ChartData setTime(String time) {
        this.time = time;
        return this;
    }

    public List<String> getDays() {
        return days;
    }

    public ChartData setDays(List<String> days) {
        this.days = days;
        return this;
    }
}
