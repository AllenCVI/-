package com.kuxuan.moneynote.json;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xieshengqi on 2017/10/26.
 */

public class DayJson implements Serializable {

    private String name;
    private String data_time;

    private int statistic_type;

    private ArrayList<String> dayTimeLists;

    private ChartData data;
    private NewChartData newChartData;


    public NewChartData getNewChartData() {
        return newChartData;
    }

    public void setNewChartData(NewChartData newChartData) {
        this.newChartData = newChartData;
    }
    private ArrayList<NewChartData> chartData;

    public void setChartData(ArrayList<NewChartData> chartData) {
        this.chartData = chartData;
    }

    public ArrayList<NewChartData> getChartData() {
        return chartData;
    }

    public ChartData getData() {
        return data;
    }

    public void setData(ChartData data) {
        this.data = data;

    }

    public int getStatistic_type() {
        return statistic_type;
    }

    public void setStatistic_type(int statistic_type) {
        this.statistic_type = statistic_type;
    }

    public ArrayList<String> getDayTimeLists() {
        return dayTimeLists;
    }

    public void setDayTimeLists(ArrayList<String> dayTimeLists) {
        this.dayTimeLists = dayTimeLists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData_time() {
        return data_time;
    }

    public void setData_time(String data_time) {
        this.data_time = data_time;
    }
}
