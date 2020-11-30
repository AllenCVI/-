package com.kuxuan.moneynote.utils;

import android.graphics.Color;
import android.widget.TextView;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public class TextSetUtil {


    /**
     * 首页左上角数字显示
     *
     * @param text
     * @return
     */
    public static void setTextForMonth(String text, TextView textView) {
        TextParser textParser = new TextParser();
        textParser.append(text, DisplayUtil.dip2px(20), Color.WHITE);
        textParser.parse(textView);
    }
    public static void setTextForMonth(String text, TextView textView,int color) {
        TextParser textParser = new TextParser();
        textParser.append(text, DisplayUtil.dip2px(20), color);
        textParser.parse(textView);
    }
    /**
     * 首页左上角数字显示
     *
     * @param text
     * @return
     */
    public static void setTextForMonth(String text, TextView textView,int s1,int s2) {
        TextParser textParser = new TextParser();
        textParser.append(text, DisplayUtil.dip2px(s1), Color.BLACK);
        textParser.append("月", DisplayUtil.dip2px(s2), Color.BLACK);
        textParser.parse(textView);

    }
    /**
     * 钱(收入和支出显示)
     *
     * @param text
     * @param textView
     */
    public static void setTextForMoey(String text, TextView textView) {
        String[] split = null;
//        String replace = text.replace("-", "");
        try {
            split = text.split("\\.");
            TextParser textParser = new TextParser();
            textParser.append(split[0], DisplayUtil.dip2px(16), Color.WHITE);
            textParser.append("." + split[1], DisplayUtil.dip2px(16), Color.WHITE);
            textParser.parse(textView);
        } catch (Exception e) {
            e.printStackTrace();
            String oText = text + ".00";
            split = oText.split("\\.");
            TextParser textParser = new TextParser();
            textParser.append(split[0], DisplayUtil.dip2px(16), Color.WHITE);
            textParser.append("." + split[1], DisplayUtil.dip2px(16), Color.WHITE);
            textParser.parse(textView);
        }
    }
    public static void setTextForBigSize(String text, TextView textView) {
        String[] split = null;
//        String replace = text.replace("-", "");
        try {
            split = text.split("\\.");
            TextParser textParser = new TextParser();
            textParser.append(split[0], DisplayUtil.dip2px(36), Color.WHITE);
            textParser.append("." + split[1], DisplayUtil.dip2px(36), Color.WHITE);
            textParser.parse(textView);
        } catch (Exception e) {
            e.printStackTrace();
            String oText = text + ".00";
            split = oText.split("\\.");
            TextParser textParser = new TextParser();
            textParser.append(split[0], DisplayUtil.dip2px(36), Color.WHITE);
            textParser.append("." + split[1], DisplayUtil.dip2px(36), Color.WHITE);
            textParser.parse(textView);
        }
    }
    /**
     * 普通颜色
     * @param text
     * @param textView
     */
    public static void setTextForMoeyforNomal(String text, TextView textView) {
        String[] split = null;
//        String replace = text.replace("-", "");
        try {
            split = text.split("\\.");
            TextParser textParser = new TextParser();
            textParser.append(split[0], DisplayUtil.dip2px(16), Color.WHITE);
            textParser.append("." + split[1], DisplayUtil.dip2px(12),  Color.WHITE);
            textParser.parse(textView);
        } catch (Exception e) {
            e.printStackTrace();
            String oText = text + ".00";
            split = oText.split("\\.");
            TextParser textParser = new TextParser();
            textParser.append(split[0], DisplayUtil.dip2px(16), Color.WHITE);
            textParser.append("." + split[1], DisplayUtil.dip2px(12),  Color.WHITE);
            textParser.parse(textView);
        }
    }
    /**
     * 钱(收入和支出显示)
     *
     * @param text
     * @param textView
     */
    public static void setTextForMoey(String text, TextView textView,int s1,int s2) {
        String[] split = null;
//        String replace = text.replace("-", "");
        try {
            split = text.split("\\.");
            TextParser textParser = new TextParser();
            textParser.append(split[0], DisplayUtil.dip2px(s1), Color.BLACK);
            textParser.append("." + split[1], DisplayUtil.dip2px(s2), Color.parseColor("#262626"));
            textParser.parse(textView);
        } catch (Exception e) {
            e.printStackTrace();
            String oText = text + ".00";
            split = oText.split("\\.");
            TextParser textParser = new TextParser();
            textParser.append(split[0], DisplayUtil.dip2px(s1), Color.BLACK);
            textParser.append("." + split[1], DisplayUtil.dip2px(s2), Color.parseColor("#262626"));
            textParser.parse(textView);
        }
    }


    /**
     * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
     * @param value
     * @return Sting
     */
    public static String formatFloatNumber(double value) {
        if(value != 0.00){
            java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
            return df.format(value);
        }else{
            return "0.00";
        }

    }

}
