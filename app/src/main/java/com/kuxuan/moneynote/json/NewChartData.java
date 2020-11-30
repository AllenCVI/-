package com.kuxuan.moneynote.json;

import java.io.Serializable;
import java.util.List;

/**
 * 新报表数据
 * Created by xieshengqi on 2018/3/6.
 */

public class NewChartData implements Serializable {


    public static final int MONTH = 2;
    public static final int YEAR = 3;
    public static final int WEEK = 1;

    private String key;
    private String year;
    private String month;

    /**
     * 统计维度 1 周 2 月 3 年
     */
    private int statistic_type;

    private List<String> time_range;

    private int db_year;
    private int db_month;


    public static int getMONTH() {
        return MONTH;
    }

    public static int getYEAR() {
        return YEAR;
    }

    public static int getWEEK() {
        return WEEK;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
        this.db_year = Integer.parseInt(year);
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
        this.db_month = Integer.parseInt(month);
    }

    public int getStatistic_type() {
        return statistic_type;
    }

    public void setStatistic_type(int statistic_type) {
        this.statistic_type = statistic_type;
    }

    public List<String> getTime_range() {
        return time_range;
    }

    public void setTime_range(List<String> time_range) {
        this.time_range = time_range;
    }
}
