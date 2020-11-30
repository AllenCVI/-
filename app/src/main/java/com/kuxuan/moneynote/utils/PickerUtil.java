package com.kuxuan.moneynote.utils;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;

import com.kuxuan.moneynote.R;

import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.picker.DatePicker;
import cn.addapp.pickers.picker.NumberPicker;
import cn.addapp.pickers.picker.TimePicker;

/**
 * 时间选择工具类
 * Created by xieshengqi on 2017/10/19.
 */

public class PickerUtil {

    private static LineConfig config;

    /**
     * 日期选择器
     *
     * @param context
     * @param listener
     */
    public static void onYearMonthPicker(Activity context, int startYear, int startMonth, int endYear, int endMonth, int chooseYear, int chooseMonth, DatePicker.OnYearMonthPickListener listener) {
        final DatePicker picker = new DatePicker(context, DatePicker.YEAR_MONTH);
        picker.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        picker.setWidth((int) (picker.getScreenWidthPixels()));
        picker.setRangeStart(startYear, startMonth, 1);
        picker.setRangeEnd(endYear, endMonth, 1);
        picker.setSelectedItem(chooseYear, chooseMonth);
        picker.setWeightEnable(true);
        picker.setCanLoop(false);
        picker.setWheelModeEnable(false);
        picker.setOnDatePickListener(listener);
        picker.show();
    }

    /**
     * 日期选择器
     *
     * @param context
     * @param listener
     */
    public static DatePicker getOnYearMonthPicker(Activity context, int startYear, int startMonth, int endYear, int endMonth, int chooseYear, int chooseMonth, DatePicker.OnYearMonthPickListener listener) {
        final DatePicker picker = new DatePicker(context, DatePicker.YEAR_MONTH);
        picker.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        picker.setWidth((int) (picker.getScreenWidthPixels()));
        picker.setRangeStart(startYear, startMonth, 1);
        picker.setRangeEnd(endYear, endMonth, 1);
        picker.setSelectedItem(chooseYear, chooseMonth);
        picker.setWeightEnable(true);
        picker.setCanLoop(false);
        picker.setWheelModeEnable(false);
        picker.setOnDatePickListener(listener);
        picker.show();
        return picker;
    }

    /**
     * 日期选择器
     *
     * @param context
     * @param listener
     */
    public static void onYearMonthDayPicker(String mTitle, int mYear, int mMonth, int mDay,
                                            Activity context, DatePicker.OnYearMonthDayPickListener listener) {
        final DatePicker picker = new DatePicker(context, DatePicker.YEAR_MONTH_DAY);
        picker.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        picker.setWidth((int) (picker.getScreenWidthPixels()));
        picker.setHeight(picker.getScreenHeightPixels() / 2 - 250);
        picker.setRangeStart(2010, 1, 1);
        picker.setTitleText(mTitle);
        picker.setCanLoop(false);
        picker.setRangeEnd(mYear, mMonth, mDay);
        picker.setSelectedItem(mYear, mMonth, mDay);
        picker.setWeightEnable(true);
        picker.setWheelModeEnable(true);
        picker.setOnDatePickListener(listener);
        picker.setSelectedTextColor(context.getResources().getColor(R.color.first_text));
        picker.setLineColor(context.getResources().getColor(R.color.textSecond));
        picker.setTextSize(16);
        picker.setCancelTextColor(context.getResources().getColor(R.color.first_text));
        picker.show();
    }


    /**
     * 时间选择
     *
     * @param activity
     * @param listener
     */
    public static void onTimePicker(Activity activity, TimePicker.OnTimePickListener listener) {
        TimePicker picker = new TimePicker(activity, TimePicker.HOUR_24);
        picker.setRangeStart(0, 0);//09:00
        picker.setRangeEnd(23, 0);//18:30
        picker.setTopLineVisible(false);
        picker.setCanLoop(false);
        picker.setLineVisible(false);
        picker.setWheelModeEnable(false);
        picker.setOnTimePickListener(listener);
        picker.show();
    }

    /**
     * 时间选择
     *
     * @param activity
     * @param listener
     */
    public static void onYearPicker(Activity activity, NumberPicker.OnNumberPickListener listener, int year) {
        config = new LineConfig();
        config.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        config.setColor(0x666666);
        NumberPicker picker = new NumberPicker(activity);
        picker.setTextSize(16);
        picker.setSelectedIndex(7);
        picker.setCanLoop(false);
        picker.setLineConfig(config);
        picker.setTitleText("选择年份");
        picker.setOnNumberPickListener(listener);
        picker.setSelectedItem(2017);
        picker.setRange(2010, 2020);
        picker.setSelectedTextColor(ContextCompat.getColor(activity, R.color.first_text));
        picker.show();
    }

    public static NumberPicker getOnYearPicker(Activity activity, NumberPicker.OnNumberPickListener listener, int year) {
        config = new LineConfig();
        config.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        config.setColor(0x666666);
        NumberPicker picker = new NumberPicker(activity);
        picker.setTextSize(16);
        picker.setSelectedIndex(7);
        picker.setCanLoop(true);
//        picker.setLineConfig(config);
        picker.setTitleText("选择年份");
        picker.setOnNumberPickListener(listener);
        picker.setRange(2010, year);
        picker.setSelectedItem(year);
        picker.setSelectedTextColor(ContextCompat.getColor(activity, R.color.first_text));
        return picker;
    }

}
