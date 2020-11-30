package com.kuxuan.moneynote.utils;

import java.text.DecimalFormat;

/**
 * Created by xieshengqi on 2017/10/20.
 */

public class JavaFormatUtils  {

    public static String getData(float d){
        DecimalFormat df = new DecimalFormat("0.00");
       return df.format(d);
    }
    public static String getData(double d){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }
    public static String getDataForTwo(float d){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }
    public static String formatFloatNumber(double value) {
//        if(value != 0.00){
//            java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
//            return df.format(value);
//        }else{
//            return "0.00";
//        }
       return String.format("%.2f", value);
    }
}
