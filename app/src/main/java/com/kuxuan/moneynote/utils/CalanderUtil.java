package com.kuxuan.moneynote.utils;

import com.kuxuan.moneynote.json.TimeJson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * Created by xieshengqi on 2017/10/25.
 */

public class CalanderUtil {


    public static void main(String[] args) {
        ArrayList<String> weekTab = getWeekTab(2015, 12, 31, 2016, 1, 5);
//        ArrayList<String> monthTab = getMonthTab(2017, 11, 2018, 3);
        System.out.println(weekTab.toString());
//        ArrayList<String> yearTab = getYearTab(2014, 2018);
//        System.out.print(yearTab.toString());
//        String[] weekAllDay = getWeekAllDay(2016, 53);
//        for (int i = 0; i < weekAllDay.length; i++)
//            System.out.print(weekAllDay[i] + "  ");
    }

    public static int getMonthDay(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1); // 设置日期
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    public static int getCurrentYear() {
        return TimeUtlis.getCurrentTime().getYear();
    }

    /**
     * 得到某一天是这一年的第几周
     *
     * @param date
     * @return
     */
    public static int getWeek(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        return week;
    }

    /**
     * 获取月
     *
     * @param date
     * @return
     */
    public static int getMonth(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int month = cal.get(Calendar.MONTH);
        return month;
    }

    /**
     * 得到某一天是这周的第几天
     *
     * @param date
     * @return
     */
    public static int getDayForWeek(String date) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week == 0) {
            week = 7;
        }
        return week;
    }


    /**
     * 获取周次
     *
     * @param startYear
     * @param startMonth
     * @param endYear
     * @param endMonth
     * @return
     */
    public static int getWeekForDay(int startYear, int startMonth, int endYear, int endMonth) {
        boolean isTrue = true;
        int dayCount = 0;
        while (isTrue) {
            if (startYear > endYear)
                break;
            else if (startYear == endYear) {
                if (startMonth > endMonth) {
                    break;
                }
            }
            int monthDay = getMonthDay(startYear, startMonth);
            dayCount += monthDay;
            if (startMonth == 12) {
                startMonth = 1;
                startYear++;
            } else {
                startMonth++;
            }
        }
        float count = dayCount / 7;
        return Math.round(count);
    }


    private static Calendar getCalendarFormYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        return cal;
    }

    public String getStartDayOfWeekNo(int year, int weekNo) {
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
                cal.get(Calendar.DAY_OF_MONTH);

    }

    /**
     * get the end day of given week no of a year.
     *
     * @param year
     * @param weekNo
     * @return
     */
    public String getEndDayOfWeekNo(int year, int weekNo) {
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
                cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据某一年的周获取这周的时间段
     *
     * @param startYear
     * @param week
     * @return
     */
    public static ArrayList<String> getWeekAllDay(int startYear, int week) {
        ArrayList<String> data = new ArrayList<String>();
        Calendar cal = getCalendarFormYear(startYear);
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int year = cal.get(Calendar.YEAR);
        int month = (cal.get(Calendar.MONTH) + 1);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int dayofMonth = 0;
        for (int i = 0; i < 7; i++) {
            data.add(year + "-" + month + "-" + day);
            dayofMonth = getMonthDay(year, month);
            day++;
            if (day > dayofMonth) {
                day = 1;
                month++;
                if (month > 12) {
                    month = 1;
                    year++;
                }
            }
        }
        return data;
    }

    /**
     * 获得某月的所有天
     *
     * @param year
     * @param month
     * @return
     */
    public static String[] getMonthAllDay(int year, int month) {
        int monthDay = getMonthDay(year, month);
        String[] data = new String[monthDay];
        for (int i = 0; i < monthDay; i++) {
            data[i] = (i + 1) + "";
        }
        return data;
    }


    /**
     * 获取所有月
     *
     * @return
     */
    public static String[] getYearAllMonth() {
        String[] data = new String[12];
        for (int i = 1; i <= 12; i++) {
            data[i - 1] = i + "月";
        }
        return data;
    }


    /**
     * 获取间隔天数
     *
     * @param startYear
     * @param startMonth
     * @param endYear
     * @param endMonth
     * @return
     */
    public static int getAllDayCount(int startYear, int startMonth, int endYear, int endMonth) {
        boolean isTrue = true;
        int dayCount = 0;
        while (isTrue) {
            if (startYear > endYear)
                break;
            else if (startYear == endYear) {
                if (startMonth > endMonth) {
                    break;
                }
            }
            int monthDay = getMonthDay(startYear, startMonth);
            dayCount += monthDay;
            if (startMonth == 12) {
                startMonth = 1;
                startYear++;
            } else {
                startMonth++;
            }
        }
        return dayCount;
    }


    /**
     * 判断是不是当前年
     *
     * @param year
     * @return
     */
    public static boolean isCurrentYear(int year) {
        return TimeUtlis.getCurrentTime().getYear() == year;
    }

    /**
     * 获取月次
     *
     * @param startYear
     * @param startMonth
     * @param endYear
     * @param endMonth
     * @return
     */
    public static int getCountForMonth(int startYear, int startMonth, int endYear, int endMonth) {
        int count = 0;
        while (true) {
            if (startYear > endYear)
                break;
            else if (startYear == endYear) {
                if (startMonth > endMonth) {
                    break;
                }
            }
            count++;
            if (startMonth == 12) {
                startMonth = 1;
                startYear++;
            } else {
                startMonth++;
            }

        }
        return count;
    }

    /**
     * 根据开始时间和结束时间获取tab的集合（周的）
     *
     * @param startYear
     * @param startMonth
     * @param startDay
     * @param endYear
     * @param endMonth
     * @param endDay
     * @return
     */
    public static ArrayList<String> getWeekTab(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        //从开始
        ArrayList<String> tabLists = new ArrayList<>();
        //开始月有多少天
        int startMonthDay = getMonthDay(startYear, startMonth);
        int endMonthDay = getMonthDay(endYear, endMonth);
        //间隔的天数
        int allDayCount = getAllDayCount(startYear, startMonth, endYear, endMonth) - startDay - (endMonthDay - endDay) + 1;
        int count = 0;
        //开始日的周几
        String data = startYear + "-" + startMonth + "-" + startDay;
        int year = startYear;
        int month = startMonth;
        int day = getDayForWeek(data);
        //先添加一个最开始的数据
//        tabLists.add(getData(year, month, startDay));
        count = 0;
        int indexCount = 7 - day;
        //从周一开始循环
        allDayCount = allDayCount + day;
//        day = startDay;
        int index = day;
        day = startDay;
        for (int i = index; i > 1; i--) {
            if (day == 1) {
                if (month == 1) {
                    month = 12;
                    day = 31;
                    year--;
                } else {
                    month--;
                    day = getMonthDay(year, month);
                }
            } else {
                day--;
            }
        }
        int c1 = 6;
        while (count < (allDayCount - 1)) {
            if (c1 == 6) {
                //算一个周期
                c1 = 0;
                tabLists.add(getData(year, month, day));
            } else {
                c1++;
            }
            //获取一个月有多少天
            int monthDay = getMonthDay(year, month);
            if (day == monthDay) {
                day = 1;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }
            } else {
                day++;
            }
            if (count == allDayCount - 1) {
                //如果为最后一位
                if (c1 > 0) {
//                    if (c1 >= 3) {
                    tabLists.add(getData(year, month, day));

//                    } else {
//                        tabLists.add(getData(year, month, day, c1));
//                    }
                }
            }
            count++;

        }
        return tabLists;
    }

    /**
     * 根据开始时间和结束时间获取tab的集合（月的）
     *
     * @param startYear
     * @param startMonth
     * @param endYear
     * @param endMonth
     * @return
     */
    public static ArrayList<String> getMonthTab(int startYear, int startMonth, int endYear, int endMonth) {
        ArrayList<String> tabLists = new ArrayList<>();
        int year = startYear;
        int month = startMonth;
        TimeJson currentTime = TimeUtlis.getCurrentTime();

        if (startYear == endYear) {
            for (int i = 0; i <= endMonth - startMonth; i++) {
                if (startYear == currentTime.getYear()) {
                    tabLists.add(month + "月");
                } else {
                    tabLists.add(startYear + "-" + month + "月");
                }
                month++;
            }
        } else {
            if (year == endYear) {
                if (month > endMonth) {
                } else {
                    tabLists.add(month + "月");
                }
            } else {
                tabLists.add(year + "-" + month + "月");
            }
            while (year <= endYear) {
                if (month == 12) {
                    month = 1;
                    year++;
                } else {
                    month++;
                }
                if (year == endYear) {
                    if (month > endMonth) {
                        break;
                    } else {
                        tabLists.add(month + "月");
                    }
                } else {
                    tabLists.add(year + "-" + month + "月");
                }
            }
        }
        return tabLists;
    }

    /**
     * 根据开始时间和结束时间获取tab的集合（年的）
     *
     * @param startYear
     * @param endYear
     * @return
     */
    public static ArrayList<String> getYearTab(int startYear, int endYear) {
        ArrayList<String> tabLists = new ArrayList<>();
        int year = startYear;
        for (int i = 0; i <= endYear - startYear; i++) {
            tabLists.add(year + "年");
            year++;
        }
        return tabLists;
    }

    /**
     * 获取tab数据
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    private static String getData(int year, int month, int day) {
        int startYear = year;
//        int week_day = getDayForWeek(year + "-" + month + "-" + day);
//        if (week_day == 7 && (day == 31 || day == 1) && (month == 12 || month == 10)) {
//
//        } else {
//            if (week_day > 3) {
//                for (int i = 3; i > 0; i--) {
//                    if (day == 1) {
//                        if (month == 1) {
//                            month = 12;
//                            day = 31;
//                            year--;
//                        } else {
//                            month--;
//                            day = CalanderUtil.getMonthDay(year, month);
//                        }
//                    } else {
//                        day--;
//                    }
//                }
//            }
//        }


        int week = getWeek(year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day));
        int mo = getMonth(year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day));
        if (mo >= 11 && week <= 1&&getDayForWeek(year+"-12"+"-31")==7) {
            week += 52;
        }else if(month==12&&week==1&&getDayForWeek(year+"-12"+"-31")!=7){
            year++;
        }
//        if (month == 12 && week == 1) {
//            year++;
//        }
        if (isCurrentYear(year)) {
            return String.format("%02d", week) + "周";
        }
//        if (month == 12 && week == 1) {
//            return (year + 1) + "-" + String.format("%02d", week) + "周";
//        }
        return year + "-" + String.format("%02d", week) + "周";
    }

    private static String getData(int year, int month, int day, int index) {
        for (int i = index; i > 1; i--) {
            if (day == 1) {
                if (month == 1) {
                    month = 12;
                    day = 31;
                    year--;
                } else {
                    month--;
                    day = CalanderUtil.getMonthDay(year, month);
                }
            } else {
                day--;
            }
        }
        int week = getWeek(year + "-" + String.format("%02d", month) + "-" + (String.format("%02d", day)));
        if (month >= 11 && week <= 1) {
            week += 52;
        }
        if (isCurrentYear(year)) {
            return String.format("%02d", week) + "周";
        }
        return year + "-" + String.format("%02d", week) + "周";
    }

    /**
     * 获取年次
     *
     * @param startYear
     * @param endYear
     * @return
     */
    public static int getCountForYear(int startYear, int endYear) {
        int count = 0;
        while (true) {

            if (startYear > endYear) {
                break;
            }
            count++;
            startYear++;

        }
        return count;
    }


    public static String getDayofweek(String date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
//   cal.setTime(new Date(System.currentTimeMillis()));
        if (date.equals("")) {
            cal.setTime(new Date(System.currentTimeMillis()));
        } else {
            cal.setTime(new Date(getDateByStr2(date, "yyyy年MM月dd日").getTime()));
        }
        return weekDays[cal.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public static String getDayofweek(String date, String format) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
//   cal.setTime(new Date(System.currentTimeMillis()));
        if (date.equals("")) {
            cal.setTime(new Date(System.currentTimeMillis()));
        } else {
            cal.setTime(new Date(getDateByStr2(date, format).getTime()));
        }
        return weekDays[cal.get(Calendar.DAY_OF_WEEK) - 1];
    }


    public static Date getDateByStr2(String dd, String format) {

        SimpleDateFormat sd = new SimpleDateFormat(format);
        Date date;
        try {
            date = sd.parse(dd);
        } catch (ParseException e) {
            date = null;
            e.printStackTrace();
        }
        return date;
    }
}
