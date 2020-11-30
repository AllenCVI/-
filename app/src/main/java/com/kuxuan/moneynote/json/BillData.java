package com.kuxuan.moneynote.json;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xieshengqi on 2017/10/24.
 */

public class BillData implements Serializable{


    /**
     * day_income_account : 0
     * day_pay_account : -1000
     * time : 10月23日  星期一
     * day_data : [{"id":9,"small_icon":"127.0.0.1","detail_icon":"127.0.0.1","name":"购物","type":2,"account":"-1000.00","demo":"购物"}]
     */

    private double day_income_account;
    private double day_pay_account;
    private String time;
    private List<TypeDataJson> day_data;

    public double getDay_income_account() {
        return day_income_account;
    }

    public void setDay_income_account(double day_income_account) {
        this.day_income_account = day_income_account;
    }

    public double getDay_pay_account() {
        return day_pay_account;
    }

    public void setDay_pay_account(double day_pay_account) {
        this.day_pay_account = day_pay_account;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<TypeDataJson> getDay_data() {
        return day_data;
    }

    public void setDay_data(List<TypeDataJson> day_data) {
        this.day_data = day_data;
    }


}
