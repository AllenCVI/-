package com.kuxuan.moneynote.json.netbody;

/**
 * 图表数据
 * Created by xieshengqi on 2017/10/25.
 */

public class ChartBody {

    private String type;
    private String statistic_type;

    private String category_id;


    public ChartBody(String type, String statistic_type, String category_id) {
        this.type = type;
        this.statistic_type = statistic_type;
        this.category_id = category_id;
    }

    public ChartBody(String type, String statistic_type) {
        this.type = type;
        this.statistic_type = statistic_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatistic_type() {
        return statistic_type;
    }

    public void setStatistic_type(String statistic_type) {
        this.statistic_type = statistic_type;
    }
}
