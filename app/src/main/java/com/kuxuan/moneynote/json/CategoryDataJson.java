package com.kuxuan.moneynote.json;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by xieshengqi on 2017/10/25.
 */

public class CategoryDataJson implements Serializable, Comparable<CategoryDataJson> {


    /**
     * category_account : -1000
     * name : 购物
     * small_icon : 127.0.0.1
     * data : [{"id":9,"category_id":2,"small_icon":"127.0.0.1","detail_icon":"127.0.0.1","name":"购物","type":2,"account":"-1000.00","demo":"购物","day":"2017-10-23"}]
     */


    //总支出或者总收入
    private double allAccount;

    private double category_account;
    private String name;
    private String small_icon;
    private List<TypeDataJson> data;


    public String getBaifenbi() {
        if (category_account < 1 && allAccount < 1) {
            category_account = category_account * 100;
            allAccount = allAccount * 100;
        }
        double i = category_account / allAccount;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(i * 100);
    }

    public double getCategory_account() {
        return category_account;
    }

    public void setCategory_account(double category_account) {
        this.category_account = category_account;
    }

    public double getAllAccount() {
        return allAccount;
    }

    public void setAllAccount(double allAccount) {
        this.allAccount = allAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmall_icon() {
        return small_icon;
    }

    public void setSmall_icon(String small_icon) {
        this.small_icon = small_icon;
    }

    public List<TypeDataJson> getData() {
        return data;
    }

    public void setData(List<TypeDataJson> data) {
        this.data = data;
    }


    @Override
    public int compareTo(@NonNull CategoryDataJson categoryDataJson) {
        if (this.getCategory_account() < categoryDataJson.getCategory_account())
            return 1;
        else if (this.getCategory_account() > categoryDataJson.getCategory_account())
            return -1;
        else
            return 0;
    }
}
