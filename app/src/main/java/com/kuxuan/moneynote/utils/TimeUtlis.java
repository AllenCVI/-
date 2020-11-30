package com.kuxuan.moneynote.utils;

import android.text.TextUtils;

import com.kuxuan.moneynote.json.TimeJson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeUtlis {
    @SuppressWarnings("deprecation")


    public static void main(String [] args){
        String data = getData(1522512000000l);
        System.out.print(data);
    }

    public static String getData(long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT_ONE);
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    public static String getTime(long time) {
        Date currentTime = new Date(Long.valueOf(time) * 1000L);
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT_SEVEN);
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    public static String getTime(String time,String type) {
        Date currentTime = new Date(Long.valueOf(time) * 1000L);
        SimpleDateFormat formatter = new SimpleDateFormat(type);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    // 时间格式模板
    /**
     * yyyy-MM-dd
     */
    public static final String TIME_FORMAT_ONE = "yyyy-MM-dd";
    public static final String TIME_FORMAT_18= "yyyy年MM月dd日";
    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String TIME_FORMAT_TWO = "yyyy-MM-dd HH:mm";
    public static final String TIME_FORMAT_TWO1 = "yyyy/MM/dd HH:mm";
    /**
     * yyyy-MM-dd HH:mmZ
     */
    public static final String TIME_FORMAT_THREE = "yyyy-MM-dd HH:mmZ";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String TIME_FORMAT_FOUR = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT_FOUR1 = "yyyy/MM/dd hh:mm:ss";
    /**
     * yyyy-MM-dd HH:mm:ss.SSSZ
     */
    public static final String TIME_FORMAT_FIVE = "yyyy-MM-dd HH:mm:ss.SSSZ";
    /**
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ
     */
    public static final String TIME_FORMAT_SIX = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    /**
     * HH:mm:ss
     */
    public static final String TIME_FORMAT_SEVEN = "HH:mm:ss";
    /**
     * HH:mm:ss.SS
     */
    public static final String TIME_FORMAT_EIGHT = "HH:mm:ss.SS";
    /**
     * yyyy.MM.dd
     */
    public static final String TIME_FORMAT_9 = "yyyy.MM.dd HH:mm";
    /**
     * yyyy年MM月
     */
    public static final String TIME_FORMAT_10 = "yyyy年MM月";

    /**
     * MM月dd日 HH:mm
     */
    public static final String TIME_FORMAT_11 = "MM月dd日 HH:mm";
    /**
     * MM月dd日
     */
    public static final String TIME_FORMAT_55 = "MM月dd日 ";

    /**
     * yyyy年MM月dd日 HH:mm
     */
    public static final String TIME_FORMAT_13 = "yyyy年MM月dd日 HH:mm:ss";
    /**
     * yyyyMMddHHmmss
     */
    public static final String TIME_FORMAT_12 = "yyyyMMddHHmmss";

    /**
     * yyyy-MM
     */
    public static final String TIME_FORMAT_14 = "yyyy-MM";
    /**
     * yyyy/MM/dd
     */
    public static final String TIME_FORMAT_15 = "yyyy/MM/dd";
    public static final String TIME_FORMAT_16 = "MM-dd";

    /**
     * 根据时间格式获得当前时间
     */
    public static String getCurrentTime(String formater) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(formater,
                Locale.SIMPLIFIED_CHINESE);
        return dateFormat.format(date);
    }

    public static TimeJson getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT_ONE);
        String format = formatter.format(date);
        String[] split = format.split("-");
        return new TimeJson(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));

    }


    public static TimeJson getCurrentTimeForhhmmss() {
        long currentTimeMillis = System.currentTimeMillis();
        Date date = new Date(currentTimeMillis);
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT_FOUR);
        String format = formatter.format(date);
        String[] split = format.split(" ");
        String yearMonthDay = split[0];
        String hhmmss = split[1];
        String[] split1 = yearMonthDay.split("-");
        String[] split2 = hhmmss.split(":");
        return new TimeJson(Integer.parseInt(split1[0]), Integer.parseInt(split1[1]), Integer.parseInt(split1[2]),split2[0],split2[1],split2[2],currentTimeMillis);

    }
    /**
     * 时间戳变成日期
     * @param s
     * @return
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT_18);
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 日期变成时间戳
     * @param s
     * @return
     * @throws ParseException
     */
    public static String dateToStamp(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT_18);
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }



    /**
     * 格式化时间
     */
    public static String formatTime(long time, String format) {
        return new SimpleDateFormat(format).format(new Date(time));
    }

    /**
     * 判断是否是合法的时间
     */
    public static boolean isValidDate(String dateString, String format) {
        return parseTime(dateString, format) > -1;
    }

    /**
     * 日期转换
     */
    public static long parseTime(String dateString, String format) {
        if ((dateString == null) || (dateString.length() == 0)) {
            return -1;
        }
        try {
            return new SimpleDateFormat(format).parse(dateString).getTime();
        } catch (ParseException e) {

        }
        return -1;
    }


    /**
     * 获取时间戳 ，格式2010-1-4 16:21:4，如果是一位数的话，那么前面不加0
     */
    public static String getTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return (year + "-" + month + "-" + day + " " + hour + ":" + minute
                + ":" + second);
    }

    /**
     * 获取时间戳 ，格式2010-1-4 16:21:4，如果是一位数的话，那么前面不加0
     */
    public static String getTimeyyyyMMdd() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + day;
    }

    /**
     * Unix时间戳转换成日期
     */
    public static String TimeStamp2Date(String timestampString, String formater) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formater, Locale.SIMPLIFIED_CHINESE)
                .format(new Date(timestamp));
        return date;
    }

    public static long getTodayTimeMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 通过时间获取时间戳
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static long getTimeMillis(String dateStr, String format) {
        long timemillis = 0;
        if (!TextUtils.isEmpty(dateStr)) {
            try {
                DateFormat df = new SimpleDateFormat(format);
                Date date = df.parse(dateStr);
                timemillis = date.getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return timemillis;
    }

    public static long getTimeMilliss(String dateStr) {
        long timemillis = 0;
        if (!TextUtils.isEmpty(dateStr)) {
            try {
                DateFormat df = new SimpleDateFormat(TIME_FORMAT_ONE);
                Date date = df.parse(dateStr);
                timemillis = date.getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return timemillis;
    }

    public static String getSimpleDatetime(String d) {
        if (TextUtils.isEmpty(d)) {
            return "";
        }
        Date date = parseDatetime(d);
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT_11);
        return sdf.format(date);
    }

    public static Date parseDatetime(String d) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT_FOUR);
            return sdf.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFormatDateStr(Date date, String formater) {
        String dateStr = null;
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(formater);
            dateStr = sdf.format(date);
        }
        return dateStr;
    }

    public static String getFormatStr(String dateStr, String formater) {
        String datestr = null;
        if (dateStr != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(formater);
            datestr = sdf.format(dateStr);
        }
        return datestr;
    }

    public static Date getDateByStr(String dateStr) {
        Date date = null;
        try {
            DateFormat df = new SimpleDateFormat(TIME_FORMAT_ONE);
            date = df.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateByDateStr(String dateStr) {
        Date date = null;
        try {
            DateFormat df = new SimpleDateFormat(TIME_FORMAT_11);
            date = df.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static int diffDate(Date date1, Date date2) throws ParseException {
        if ((date1 == null) || (date2 == null)) {
            return 0;
        }

        long ca = date1.getTime() - date2.getTime();
        if (ca <= 0) {
            return 0;
        }

        return (int) (ca / 1000 / 60 / 60 / 24);
    }

    public static int countDays(Date date1, Date date2) {
        int result = 0;
        GregorianCalendar gc1 = new GregorianCalendar();
        GregorianCalendar gc2 = new GregorianCalendar();
        gc1.setTime(date1);
        gc2.setTime(date2);

        result = getDays(gc1, gc2);

        return result;
    }

    /**
     * 获取指定月的前一月（年）或后一月（年）
     *
     * @param addYear
     * @param addMonth
     * @param addDate
     * @return
     * @throws Exception
     */
    public static String getLastDay(Date date, int addYear, int addMonth,
                                    int addDate, String formater) throws Exception {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.YEAR, addYear);
            cal.add(Calendar.MONTH, addMonth);
            cal.add(Calendar.DATE, addDate);
            SimpleDateFormat returnSdf = new SimpleDateFormat(
                    formater);
            String dateTmp = returnSdf.format(cal.getTime());
            Date returnDate = returnSdf.parse(dateTmp);
            return dateTmp;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public static int getDays(GregorianCalendar g1, GregorianCalendar g2) {
        int elapsed = 0;
        GregorianCalendar gc1, gc2;

        if (g2.after(g1)) {
            gc2 = (GregorianCalendar) g2.clone();
            gc1 = (GregorianCalendar) g1.clone();
        } else {
            gc2 = (GregorianCalendar) g1.clone();
            gc1 = (GregorianCalendar) g2.clone();
        }

        gc1.clear(Calendar.MILLISECOND);
        gc1.clear(Calendar.SECOND);
        gc1.clear(Calendar.MINUTE);
        gc1.clear(Calendar.HOUR_OF_DAY);

        gc2.clear(Calendar.MILLISECOND);
        gc2.clear(Calendar.SECOND);
        gc2.clear(Calendar.MINUTE);
        gc2.clear(Calendar.HOUR_OF_DAY);

        while (gc1.before(gc2)) {
            gc1.add(Calendar.DATE, 1);
            elapsed++;
        }
        return elapsed;
    }

    public static String getNowTime(String create_time) {
        long nowTime = System.currentTimeMillis();
        Date date = parseDatetime(create_time);
        long createTime = date.getTime();
        long time = nowTime - createTime;
        long chaTime = time / 1000;// s
        if (chaTime < 60) {
            return chaTime + "秒前";
        } else if (chaTime >= 60 && chaTime < 3600) {
            return (chaTime / 60) + "分钟前";
        } else if (chaTime >= 3600 && chaTime < (24 * 3600)) {
            return (chaTime / 3600) + "小时前";
        } else {
            return (chaTime / (24 * 3600)) + "天前";
        }
    }

    /**
     * 获取时间yyyy/MM/dd
     *
     * @param creatTime
     * @return
     */
    public static String getCreateTime(long creatTime) {
        String time = null;
        Date date = new Date(creatTime);
        SimpleDateFormat sd = new SimpleDateFormat(TIME_FORMAT_15);
        time = sd.format(date);
        return time;
    }

    /**
     * 获取yyyy-hh-mm HH:ss
     *
     * @param creatTime
     * @return
     */
    public static String getyyyyddmmCreateTime(String creatTime) {
        String time = null;
        Date date = new Date(Long.parseLong(creatTime + "000"));
        SimpleDateFormat sd = new SimpleDateFormat(TIME_FORMAT_TWO);
        time = sd.format(date);
        return time;
    }

    /**
     * 秒转化为分钟
     *
     * @param second
     * @return
     */
    public static String getTimeForSecond(long second) {
        String time = null;
        if (second <= 60) {
            time = second + "秒";

        } else {
            long s = second / 60;
            time = s + "分" + (second - (s * 60)) + "秒";
        }
        return time;


    }

    /**
     * 获取yyyy/hh/mm HH:ss
     *
     * @param creatTime
     * @return
     */
    public static String getyyyyddmmCreateTime1(String creatTime) {
        String time = null;
        Date date = new Date(Long.parseLong(creatTime + "000"));
        SimpleDateFormat sd = new SimpleDateFormat(TIME_FORMAT_TWO1);
        time = sd.format(date);
        return time;
    }

    public static String getHH_mm_dd(String creatTime) {
        String time = "";
        Date date = new Date(Long.parseLong(creatTime + "000"));
        SimpleDateFormat sd = new SimpleDateFormat(TIME_FORMAT_SEVEN);
        time = sd.format(date);
        return time;
    }

    public static String getMMdd(String creatTime) {
        String time = "";
        Date date = new Date(Long.parseLong(creatTime));
        SimpleDateFormat sd = new SimpleDateFormat(TIME_FORMAT_55);
        time = sd.format(date);
        return time;
    }


    /**
     * 得到某一天
     *
     * @param year
     * @param lastDate
     * @return
     */
    public static String getData(int year, int lastDate) {
        long timeMilliss = getTimeMilliss(year + "-01-01");
        //过去的天数
        int day = lastDate;
        long dayTime = day * 24l * 60 * 60 * 1000;
        long time = timeMilliss + dayTime;
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat(TIME_FORMAT_ONE);
        String dataTime = sd.format(date);
        return dataTime;
    }

    public static boolean isLeapYear(int yearNum) {
        boolean isLeep = false;
        /** 判断是否为闰年，赋值给一标示符flag */
        if ((yearNum % 4 == 0) && (yearNum % 100 != 0)) {
            isLeep = true;
        } else if (yearNum % 400 == 0) {
            isLeep = true;
        } else {
            isLeep = false;
        }
        return isLeep;
    }

    /**
     * 获取mm-dd格式的时间字符串
     *
     * @return
     */
    public static String getMMdd() {
        String time = "";
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sd = new SimpleDateFormat(TIME_FORMAT_ONE);
        time = sd.format(date);
        return time;
    }
    private static void setToFirstDay(Calendar calendar) {
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }
    }

    private static String printDay(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      return  dateFormat.format(calendar.getTime());
    }
    /**
     * 获取一周内所有的天数的字符串
     *
     * @return
     */
    public  static ArrayList<String> convertWeekByDate(String stringTime) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        Date time = new Date(getTimeMilliss(stringTime));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        setToFirstDay(calendar);
        for (int i = 0; i < 7; i++) {
            stringArrayList.add(printDay(calendar));
            calendar.add(Calendar.DATE, 1);
        }
        return stringArrayList;
    }


}
