package com.kuxuan.moneynote.json;

import java.io.Serializable;

/**
 * 账单基本类型
 * Created by xieshengqi on 2017/10/25.
 */

public class TypeDataJson implements Serializable{



    /**
     * id : 9
     * small_icon : 127.0.0.1
     * detail_icon : 127.0.0.1
     * name : 购物
     * type : 2
     * account : -1000.00
     * demo : 购物
     */


    private String day;

    private int currentYear;
    private int currentMonth;
    private boolean isTrueData;
    //日期判断
    private String day_type;
private int category_id;
    private int id;
    private String small_icon;
    private String detail_icon;
    private String name;
    private int type;
    private String account;
    private String demo;

    //账单id
    private String bill_id;

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


    //数据库所需要的时间
    private int db_year;
    private int db_month;
    private int db_day;

    public int getDb_year() {
        return db_year;
    }

    public void setDb_year(int db_year) {
        this.db_year = db_year;
    }

    public int getDb_month() {
        return db_month;
    }

    public void setDb_month(int db_month) {
        this.db_month = db_month;
    }

    public int getDb_day() {
        return db_day;
    }

    public void setDb_day(int db_day) {
        this.db_day = db_day;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public boolean isTrueData() {
        return isTrueData;
    }

    public void setTrueData(boolean trueData) {
        isTrueData = trueData;
    }

    /**
     * 判断是不是需要显示标题
     */
    private boolean isFirst = false;


    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    private Object tag;

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public String getDay_type() {
        return day_type;
    }

    public void setDay_type(String day_type) {
        this.day_type = day_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSmall_icon() {
        return small_icon;
    }

    public void setSmall_icon(String small_icon) {
        this.small_icon = small_icon;
    }

    public String getDetail_icon() {
        return detail_icon;
    }

    public void setDetail_icon(String detail_icon) {
        this.detail_icon = detail_icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }
}
