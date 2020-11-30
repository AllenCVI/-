package com.kuxuan.moneynote.ui.fragments.reportdetial;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.json.CategoryDataJson;
import com.kuxuan.moneynote.json.ChartData;
import com.kuxuan.moneynote.json.LineJson;
import com.kuxuan.moneynote.json.ReportJson;
import com.kuxuan.moneynote.json.TimeDataJson;
import com.kuxuan.moneynote.json.TimeJson;
import com.kuxuan.moneynote.ui.adapter.ReportAdapter;
import com.kuxuan.moneynote.ui.weight.ChartLayout;
import com.kuxuan.moneynote.utils.CalanderUtil;
import com.kuxuan.moneynote.utils.TimeUtlis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.kuxuan.moneynote.utils.CalanderUtil.getMonthDay;

/**
 * Created by xieshengqi on 2017/10/20.
 */

public class ReportDetialPresent extends ReportDetialContract.RepDePresent {

    ReportAdapter adapter;

    @Override
    void setChartData(ChartLayout chartLayout, int type, ArrayList<LineJson> datas) {
        chartLayout.setLineData( datas);
    }

    @Override
    void initRecyclerView(Context context, RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ReportAdapter(R.layout.item_report_layout,false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    void setLineData(ChartData chartData) {
        jugeDataType(chartData);
    }

    /**
     * 圆环数据分析
     *
     * @param chartData
     */
    @Override
    void setCircleData(ChartData chartData) {
        jugeDataCircleType(chartData);
    }


    /**
     * 数据分类
     *
     * @param chartData
     */
    private void jugeDataType(ChartData chartData) {
        switch (chartData.getStatistic_type()) {
            case ChartData.WEEK:
                //周
                setWeekData(chartData);
                break;
            case ChartData.YEAR:
                //年
                setYearData(chartData);
                break;
            case ChartData.MONTH:
                //月
                setMonth(chartData);
                break;
        }
        /**
         * 设置下面的数据
         */
        getAdapterLists(chartData);
    }

    /**
     * 圆环数据分类
     *
     * @param chartData
     */
    private void jugeDataCircleType(ChartData chartData) {
        if (chartData.getCategory_data() != null) {
            for (CategoryDataJson categoryDataJson : chartData.getCategory_data()) {
                categoryDataJson.setAllAccount(chartData.getAccount());
            }
        }
        view.setCircleData((ArrayList<CategoryDataJson>) chartData.getCategory_data());
    }

    /**
     * 设置周的图表
     *
     * @param chartData
     */
    private void setWeekData(ChartData chartData) {
        String time = chartData.getTime();
        List<String> days = chartData.getDays();
        String mMdd = TimeUtlis.getMMdd();
        String[] datas = new String[days.size()];
        ArrayList<LineJson> lineLists = new ArrayList<>();
        //设置x的值
        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).equals(mMdd)) {
                datas[i] = "今天";
            } else {
                datas[i] = days.get(i).substring(5, days.get(i).length());
            }
            lineLists.add(new LineJson(datas[i], getYvalue(days.get(i), chartData.getTime_data())));
        }
        view.setLineData(lineLists);
    }

    /**
     * 设置adapter
     *
     * @param chartData
     */
    private void getAdapterLists(ChartData chartData) {
        ArrayList<ReportJson> reportJsons = new ArrayList<>();
        List<CategoryDataJson> category_data = chartData.getCategory_data();
        if (category_data != null) {
            for (int i = 0; i < category_data.size(); i++) {
                ReportJson reportJson = new ReportJson();
                reportJson.setAllMoney((long) chartData.getAccount());
                reportJson.setTag(category_data.get(i));
                reportJsons.add(reportJson);
            }
        }
        adapter.setNewData(reportJsons);
    }


    /**
     * 设置月的时间
     *
     * @param chartData
     */
    private void setMonth(ChartData chartData) {
        //判断当月有多少天
        String time = chartData.getTime();
        TimeJson currentTime = TimeUtlis.getCurrentTime();
        int year = currentTime.getYear();
        int month = currentTime.getMonth();
        int dayCount = 0;
        if (time.equals("本月")) {
            dayCount = CalanderUtil.getMonthDay(year, month);
        } else if (time.equals("上月")) {
            if (month == 12) {
                month = 1;
                year--;
            } else {
                month--;
            }
            dayCount = getMonthDay(year, month);
        } else {
            String[] split = null;
            try {
                split = time.split("-");
                year = Integer.parseInt(split[0]);
                String m = split[1];
                if (m.startsWith("0")) {
                    month = Integer.parseInt(m.substring(1, m.length() - 1));
                } else {
                    month = Integer.parseInt(m.substring(0, m.length() - 1));
                }
                dayCount = CalanderUtil.getMonthDay(year, month);
            } catch (Exception e) {
                dayCount = CalanderUtil.getMonthDay(year, month);
            }
        }
        String[] datas = new String[dayCount];
        ArrayList<LineJson> lineLists = new ArrayList<>();
        for (int i = 0; i < dayCount; i++) {
            datas[i] = (i+1) + "";
            if(month<10)
            lineLists.add(new LineJson(datas[i], getYvalue(year + "-0" + month + "-" + (i+1), chartData.getTime_data())));
            else
                lineLists.add(new LineJson(datas[i], getYvalue(year + "-0" + month + "-" + (i+1), chartData.getTime_data())));
        }
        view.setLineData(lineLists);
    }

    /**
     * 设置年的值
     *
     * @param chartData
     */
    private void setYearData(ChartData chartData) {
        TimeJson currentTime = TimeUtlis.getCurrentTime();
        int year = currentTime.getYear();
        String time = chartData.getTime();
        if (time.equals("今年")) {

        } else if (time.equals("去年")) {
            year--;
        } else {
            year = Integer.parseInt(time.substring(0,time.length()-1));
        }
        String[] datas = new String[12];
        ArrayList<LineJson> lineLists = new ArrayList<>();
        for (int i = 0; i < datas.length; i++) {
            datas[i] = (i+1) + "月";
            if (i +1< 10) {
                lineLists.add(new LineJson(datas[i], getYvalueForYear(year + "-0" + i+1, chartData.getTime_data())));
            } else {
                lineLists.add(new LineJson(datas[i], getYvalueForYear(year + "-" + i+1, chartData.getTime_data())));
            }
        }
        view.setLineData(lineLists);

    }

    /**
     * 获取Y的值
     *
     * @param key
     * @param list
     * @return
     */
    private int getYvalue(String key, List<TimeDataJson> list) {
        int y = 0;
        if (list == null) {
            return 0;
        }
        for (TimeDataJson dataJson : list) {
            if (key.equals(dataJson.getDay())) {
                y = (int) dataJson.getAccount();
                break;
            }
        }
        return y;
    }

    /**
     * 获取Y的值(年)
     *
     * @param key
     * @param list
     * @return
     */
    private int getYvalueForYear(String key, List<TimeDataJson> list) {
        int y = 0;
        if (list == null) {
            return 0;
        }
        for (TimeDataJson dataJson : list) {
            if (dataJson.getDay().startsWith(key)) {
                y += dataJson.getAccount();
            }
        }
        return y;
    }

//    private void testChart() {
//        ArrayList<LineJson> data = new ArrayList<>();
//        String[] data1 = {"10-16", "10-17", "10-18", "10-19", "今天", "10-12", "10-22",};
//        for (int i = 0; i < 7; i++) {
//            int y = 0;
//            if (i == 2 || i == 6) {
//                y = i * 10;
//            } else
//                y = 0;
//
//            y = i * 10;
//            LineJson lineJson = new LineJson(data1[i], y);
//            data.add(lineJson);
//        }
//
//        chartlayout.addDataSet(new ArrayList<LineJson>());
//    }

    private void testAdapter() {
        ArrayList<ReportJson> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ReportJson reportJson = new ReportJson();
            reportJson.setName("报告" + (i + 1));
            int outMoney = i * 10;
            reportJson.setInmoney(100 - outMoney);
            reportJson.setOutmoney(outMoney);
            reportJson.setAllMoney(100);
            data.add(reportJson);
        }
        Collections.sort(data);
        adapter.setNewData(data);
    }
}
