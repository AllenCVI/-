package com.kuxuan.moneynote.json.netbody;

import java.io.Serializable;

/**
 * Created by xieshengqi on 2018/3/6.
 */

public class NewChartBody implements Serializable {


    private int statistic_type;

    private int type;
    private String year;
    private String month;
    private String start_date;

    private String end_date;

    private String category_id;


    public NewChartBody(int statistic_type, int type, String category_id) {
        this.statistic_type = statistic_type;
        this.type = type;
        this.category_id = category_id;
    }

    public NewChartBody(int statistic_type, int type, String year, String month, String start_date, String end_date, String category_id) {
        this.statistic_type = statistic_type;
        this.type = type;
        this.year = year;
        this.month = month;
        this.start_date = start_date;
        this.end_date = end_date;
        this.category_id = category_id;
    }
}
