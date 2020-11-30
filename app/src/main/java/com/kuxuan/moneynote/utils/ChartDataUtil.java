package com.kuxuan.moneynote.utils;

import android.util.Log;

import com.kuxuan.moneynote.json.ChartData;
import com.kuxuan.moneynote.json.DayJson;
import com.kuxuan.moneynote.json.TimeJson;

import java.util.ArrayList;

/**
 * Created by xieshengqi on 2017/10/27.
 */

public class ChartDataUtil {


    /**
     * 获取tab标签
     *
     * @return
     */
    public static ArrayList<DayJson> getTitleData(int timeType, int startYear, int startMonth, int endYear, int endMonth) {
        ArrayList<DayJson> titleLists = new ArrayList<>();
        ArrayList<DayJson> weekData = null;
        int statistic_type = ChartData.WEEK;
        if (timeType == 1) {
            //算出有多少周
            weekData = getWeekData(startYear, startMonth, endYear, endMonth);
            statistic_type = ChartData.WEEK;
            for (int i = 0; i < weekData.size(); i++) {
                weekData.get(i).getName();
                weekData.get(i).setDayTimeLists(TimeUtlis.convertWeekByDate(weekData.get(i).getData_time()));
                weekData.get(i).setStatistic_type(statistic_type);
                titleLists.add(weekData.get(i));
            }
        } else if (timeType == 2) {
            //算出有多少月
            String[] data = getMonthData(startYear, startMonth, endYear, endMonth);
            statistic_type = ChartData.MONTH;
            for (int i = 0; i < data.length; i++) {
                DayJson dayJson = new DayJson();
                dayJson.setName(data[i]);
                dayJson.setStatistic_type(statistic_type);
                titleLists.add(dayJson);
            }


        } else {
            //算出有多少年
            String[] yearData = getYearData(startYear, endYear);
            statistic_type = ChartData.YEAR;
            for (int i = 0; i < yearData.length; i++) {
                DayJson dayJson = new DayJson();
                dayJson.setName(yearData[i]);
                dayJson.setStatistic_type(statistic_type);
                titleLists.add(dayJson);
            }
        }

        return titleLists;
    }


    /**
     * 设置空数组对象
     *
     * @param dayJson
     * @return
     */
    public static void setNullData(DayJson dayJson) {
        ChartData chartData = new ChartData().setTime(dayJson.getName());
        chartData.setStatistic_type(dayJson.getStatistic_type());
        if (dayJson.getStatistic_type() == 1){
            chartData.setDays(dayJson.getDayTimeLists());
        }
        chartData.setTrueData(false);
        dayJson.setData(chartData);

    }

    /**
     * 判断真实数据里面有没有
     *
     * @param key
     * @return
     */
    public static ChartData isHaveData(String key, ArrayList<ChartData> havaLists) {
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
    private static ArrayList<DayJson> getWeekData(int startYear, int startMonth, int endYear, int endMonth) {
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
                Log.e("时间：", yearCount + " : " + allDatCount + ":  " + day);
                //年份间隔
                int avage = 0;
                if (day <= firstDayCount) {
                    avage = 0;
                } else {
                    avage = yearCount - (allDatCount - day) / 365;
                }
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
    private static DayJson setTitleRealyData(int year, int day, int countday) {
        String time_data = TimeUtlis.getData(year, day - countday);
        int week1 = CalanderUtil.getWeek(time_data);
        DayJson dayJson = new DayJson();
        String data = null;
        boolean currentYear = CalanderUtil.isCurrentYear(year);
        TimeJson currentTime = TimeUtlis.getCurrentTime();
        int currentWeek = CalanderUtil.getWeek(currentTime + "-" + currentTime.getMonth() + "-" + currentTime.getDay());
        if (week1 < 10) {
            if (currentYear) {
                data = "0" + week1 + "周";
                if (currentWeek == week1) {
                    data = "本周";
                } else if (currentWeek - week1 == 1) {
                    data = "上周";
                }
            } else {
                data = year + "-0" + week1 + "周";
            }
        } else {
            if (currentYear) {
                data = week1 + "周";
                if (currentWeek == week1) {
                    data = "本周";
                } else if (currentWeek - week1 == 1) {
                    data = "上周";
                }
            } else {
                data = year + "-" + week1 + "周";
            }
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
    private static String[] getMonthData(int startYear, int startMonth, int endYear, int endMonth) {
        int allMonthCount = CalanderUtil.getCountForMonth(startYear, startMonth, endYear, endMonth);
        int stYear = startYear;
        int stMonth = startMonth;
        TimeJson currentTime = TimeUtlis.getCurrentTime();
        int currentYear = currentTime.getYear();
        int currentMonth = currentTime.getMonth();
        String[] data = new String[allMonthCount];
        for (int i = 0; i < allMonthCount; i++) {
            if (stMonth < 10) {
                if (stYear == currentYear) {
                    data[i] = "0" + stMonth + "月";
                    if (currentMonth == stMonth) {
                        data[i] = "本月";
                    } else if (currentMonth - stMonth == 1) {
                        data[i] = "上月";
                    }


                } else {
                    data[i] = stYear + "-0" + stMonth + "月";
                }
            } else {
                if (stYear == currentYear) {
                    data[i] = stMonth + "月";
                    if (currentMonth == stMonth) {
                        data[i] = "本月";
                    } else if (currentMonth - stMonth == 1) {
                        data[i] = "上月";
                    }
                } else {
                    data[i] = stYear + "-" + stMonth + "月";
                }


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
    private static String[] getYearData(int startYear, int endYear) {
        int count = endYear - startYear;
        String[] data;
        TimeJson currentTime = TimeUtlis.getCurrentTime();
        int currentYear = currentTime.getYear();
        if (count == 0) {
            data = new String[1];
            if (currentYear == startYear) {
                data[0] = startYear + "年";
            } else {
                data[0] = "今年";
            }
        } else {
            data = new String[count + 1];
            for (int i = 0; i < count + 1; i++) {
                data[i] = i + startYear + "年";
                if (currentYear == (i + startYear)) {
                    data[i] = "今年";
                } else if (currentYear - (i + startYear) == 1) {
                    data[i] = "去年";
                }
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
    private static int getDayForYear(int year) {
        int allyearDay = 365;
        boolean leapYear = TimeUtlis.isLeapYear(year);
        if (leapYear) {
            allyearDay = 366;
        }
        return allyearDay;


    }

}
