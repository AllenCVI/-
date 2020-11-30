package com.kuxuan.moneynote.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.kuxuan.moneynote.base.BaseFragment;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.ChartData;
import com.kuxuan.moneynote.json.DayJson;
import com.kuxuan.moneynote.ui.fragments.reportdetial.ReportDetialFragment;
import com.kuxuan.moneynote.ui.weight.ChartLayout;
import com.kuxuan.moneynote.utils.CalanderUtil;
import com.kuxuan.moneynote.utils.SPUtil;
import com.kuxuan.moneynote.utils.TimeUtlis;

import java.util.ArrayList;
import java.util.WeakHashMap;

/**
 * Created by xieshengqi on 2017/10/20.
 */

public class ViewpagerChartAdapter extends FragmentPagerAdapter {
    /**
     * 切换视图的type
     */
    private int type = ChartLayout.LINE;
    private ArrayList<String> titleLists;
    private ArrayList<ChartData> dataLists;
    /**
     * 有的数据源
     */
    private ArrayList<ChartData> havaLists;

    FragmentManager fm;


    /**
     * 判断是周，月，还是年
     * 1周，2月，3年
     */
    private int timeType = 1;


    private int startYear = 0;
    private int startMonth = 1;
    private int endYear = 0;
    private int endMonth = 1;


    /**
     * 当前position
     */
    private int currentPosition = 0;

    private WeakHashMap<Integer, ReportDetialFragment> maps;


    private Context mContext;


    public ViewpagerChartAdapter(FragmentManager fm, Context context, int type, int timeType, ArrayList<ChartData> dataLists) {
        super(fm);
        this.fm = fm;
        this.type = type;
        this.timeType = timeType;
        this.mContext = context;
        if (dataLists != null) {
            havaLists = dataLists;
        } else {
            havaLists = new ArrayList<>();
        }
        startYear = (int) SPUtil.get(mContext, Constant.ChartInfo.STARTYEAR, 2016);
        startMonth = (int) SPUtil.get(mContext, Constant.ChartInfo.STARTMONTH, 1);
        endYear = (int) SPUtil.get(mContext, Constant.ChartInfo.ENDYEAR, 2017);
        endMonth = (int) SPUtil.get(mContext, Constant.ChartInfo.ENDMONTH, 10);
        setTitleLists();
        maps = new WeakHashMap<>();
    }


    /**
     * 获取tab标签
     *
     * @return
     */
    private void getTitleData() {
        if (dataLists == null) {
            dataLists = new ArrayList<>();
        }
        String[] data = null;
        ArrayList<DayJson> weekData = null;
        int statistic_type = ChartData.WEEK;
        if (timeType == 1) {
            //算出有多少周
            weekData = getWeekData();
            statistic_type = ChartData.WEEK;
            data = new String[weekData.size()];
            for (int i = 0; i < weekData.size(); i++) {
                data[i] = weekData.get(i).getName();
            }
        } else if (timeType == 2) {
            //算出有多少月
            data = getMonthData();
            statistic_type = ChartData.MONTH;
        } else {
            //算出有多少年
            data = getYearData();
            statistic_type = ChartData.YEAR;
        }

        if (titleLists == null) {
            titleLists = new ArrayList<>();
        }
        this.titleLists.clear();
        dataLists.clear();
        for (int i = 0; i < data.length; i++) {
            titleLists.add(data[i]);
            ChartData haveData = isHaveData(data[i]);
            if (haveData != null) {
                dataLists.add(haveData);
            } else {
                ChartData chartData = new ChartData().setTime(data[i]);
                chartData.setStatistic_type(statistic_type);
                if (timeType == 1) {
                    //如果是周的话才存入
                    ArrayList<String> stringArrayList = TimeUtlis.convertWeekByDate(weekData.get(i).getData_time());
                    chartData.setDays(stringArrayList);
                }
                dataLists.add(chartData);
            }
        }
    }

    /**
     * 判断真实数据里面有没有
     *
     * @param key
     * @return
     */
    private ChartData isHaveData(String key) {
        ChartData chart1 = null;
        for (ChartData chartData : havaLists) {

            if (chartData.getTime().equals(key)) {
                chart1 = chartData;
                break;
            }
        }
        return chart1;
    }

    /**
     * 获取周的title
     *
     * @return
     */
    private ArrayList<DayJson> getWeekData() {
        //周次
        int weekForDay = CalanderUtil.getWeekForDay(startYear, startMonth, endYear, endMonth);
        int allDatCount = CalanderUtil.getAllDayCount(startYear, startMonth, endYear, endMonth);
        int firstDayCount = CalanderUtil.getAllDayCount(startYear, startMonth, startYear, 12);
        String[] data = new String[weekForDay];
        ArrayList<DayJson> list = new ArrayList<>();
        int week = 1;
        int yearCount = allDatCount / 365;
        for (int i = 0; i < weekForDay; i++) {
            DayJson dayJson = null;
            String yyyyMMdd = null;
            if (yearCount == 0) {
                if (i == weekForDay - 1) {
                    data[i] = "本周";
                } else if (i == weekForDay - 2) {
                    data[i] = "上周";
                } else
                    data[i] = (i + 1) + "周";

                if (firstDayCount >= allDatCount) {
                    //在一年内
                    int dayForYear = getDayForYear(startYear);
                    dayJson = setTitleRealyData(startYear, dayForYear, dayForYear - firstDayCount + (i * 7));
                } else {
                    //跨年中
                    if (i * 7 <= firstDayCount) {
                        dayJson = setTitleRealyData(startYear, getDayForYear(startYear), getDayForYear(startYear) - firstDayCount + (i * 7));
                    } else {
                        dayJson = setTitleRealyData(startYear + 1, i * 7, 0);
                    }

                }

            } else if (yearCount > 0) {
                int day = (i) * 7;
                //当前开始年份
                int currentYear = 0;
                //年份间隔
                int avage = yearCount - (allDatCount - day) / 365;
                //当前年
                currentYear = startYear + avage;
                //那一周所在年之前年的天数总和
                int countday = 0;
                for (int k = 0; k < avage - 1; k++) {
                    if (k == 0) {
                        //首年
                        countday += firstDayCount;
                    } else {
                        countday += getDayForYear(startYear + k);
                    }
                }
                dayJson = setTitleRealyData(currentYear, day, countday);
            }

            list.add(dayJson);

        }
        return list;
    }

    /**
     * 填充真实的时间数据
     *
     * @param year
     * @param day      走过的天数
     * @param countday 那一周所在年之前年的天数总和
     * @return
     */
    private DayJson setTitleRealyData(int year, int day, int countday) {
        String time_data = TimeUtlis.getData(year, day - countday);
        int week1 = CalanderUtil.getWeek(time_data);
        DayJson dayJson = new DayJson();
        String data = null;
        if (week1 < 10) {
            data = year + "-0" + week1 + "周";
        } else {
            data = year + "-" + week1 + "周";
        }
        dayJson.setName(data);
        dayJson.setData_time(time_data);
        return dayJson;
    }

    /**
     * 获取月份的title
     *
     * @return
     */
    private String[] getMonthData() {
        int allMonthCount = CalanderUtil.getCountForMonth(startYear, startMonth, endYear, endMonth);
        int stYear = startYear;
        int stMonth = startMonth;
        String[] data = new String[allMonthCount];
        for (int i = 0; i < allMonthCount; i++) {
            if(stMonth<10){
            data[i] = stYear + "-0" + stMonth + "月";
            }else{
                data[i] = stYear + "-" + stMonth + "月";
            }
            if (stMonth == 12) {
                stMonth = 1;
                stYear++;
            } else
                stMonth++;
        }
        return data;

    }

    /**
     * 获取年的title
     *
     * @return
     */
    private String[] getYearData() {
        int count = endYear - startYear;
        String[] data;
        if (count == 0) {
            data = new String[1];
            data[0] = startYear+"年";
        } else {
            data = new String[count+1];
            for (int i = 0; i < count+1; i++) {
                data[i] = i + startYear + "年";
            }
        }

        return data;
    }


    /**
     * 获取一年中有多少天
     *
     * @param year
     * @return
     */
    private int getDayForYear(int year) {
        int allyearDay = 365;
        boolean leapYear = TimeUtlis.isLeapYear(year);
        if (leapYear) {
            allyearDay = 366;
        }
        return allyearDay;


    }

    /**
     * 更新数据
     *
     * @param type
     * @param lists
     */
    public void setDataLists(int type, ArrayList<ChartData> lists) {
        this.timeType = type;
        havaLists.clear();
        if (lists != null)
            havaLists.addAll(lists);
        setTitleLists();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        FragmentTransaction ft = fm.beginTransaction();
        for (int i = 0; i < getCount(); i++) {//通过遍历清除所有缓存
            final long itemId = getItemId(i);
            //得到缓存fragment的名字
            String name = makeFragmentName(container.getId(), itemId);
            //通过fragment名字找到该对象
            BaseFragment fragment = (BaseFragment) fm.findFragmentByTag(name);
            if (fragment != null) {
                //移除之前的fragment
                ft.remove(fragment);
            }
        }
        //重新添加新的fragment:最后记得commit
        ft.add(container.getId(), getItem(position)).attach(getItem(position)).commit();
        return getItem(position);
    }

    /**
     * 得到缓存fragment的名字
     *
     * @param viewId
     * @param id
     * @return
     */
    private String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    private void setTitleLists() {
        getTitleData();

    }

    @Override
    public Fragment getItem(int position) {
        ReportDetialFragment reportDetialFragment = null;
        try {
            reportDetialFragment = maps.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (reportDetialFragment == null) {
            reportDetialFragment = ReportDetialFragment.getInstance(type, dataLists.get(position));
            maps.put(position, reportDetialFragment);
        }
        return reportDetialFragment;
    }


    public int getType() {
        return type;
    }


    public int getCurrentPosition() {
        return currentPosition;
    }


    private void changeCurrentData() {
        ReportDetialFragment reportDetialFragment = maps.get(currentPosition);
        ReportDetialFragment leftreportDetialFragment = maps.get(currentPosition - 1);
        ReportDetialFragment rightreportDetialFragment = maps.get(currentPosition + 1);
        if (reportDetialFragment != null) {
            try {
                ChartData chartData = dataLists.get(currentPosition);
                reportDetialFragment.setData(chartData);
            } catch (Exception e) {
                //当前没有
            }

        }
        if (leftreportDetialFragment != null) {
            leftreportDetialFragment.setType(type);
        }
        if (rightreportDetialFragment != null) {
            rightreportDetialFragment.setType(type);
        }
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setType(int type) {
        this.type = type;
        ReportDetialFragment reportDetialFragment = maps.get(currentPosition);
        ReportDetialFragment leftreportDetialFragment = maps.get(currentPosition - 1);
        ReportDetialFragment rightreportDetialFragment = maps.get(currentPosition + 1);
        if (reportDetialFragment != null) {
            reportDetialFragment.setType(type);
        }
        if (leftreportDetialFragment != null) {
            leftreportDetialFragment.setType(type);
        }
        if (rightreportDetialFragment != null) {
            rightreportDetialFragment.setType(type);
        }

    }

    public ArrayList<String> getTitleLists() {
        return titleLists;
    }

    public void setTitleLists(ArrayList<String> titleLists) {
        this.titleLists = titleLists;
    }

    public WeakHashMap<Integer, ReportDetialFragment> getMaps() {
        return maps;
    }

    public void setMaps(WeakHashMap<Integer, ReportDetialFragment> maps) {
        this.maps = maps;
    }

    @Override
    public int getCount() {
        return titleLists.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleLists.get(position);
    }



}
