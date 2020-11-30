package com.kuxuan.moneynote.db;

import java.io.Serializable;

/**
 * 存储数据表数据模型
 * Created by xieshengqi on 2018/3/27.
 */

public class CategoryDBbean implements Serializable {


    private String bill_id;


    /**
     * 账单备注
     */
    private String demo;

    /**
     * 账单名字
     */
    private String name;


    /**
     * 账单类型（收入还是支出）
     */
    private int type;


    /**
     * 类型图片路径
     */
    private String type_imagepath;
    /**
     * 账单金额
     */
    private double account;

    /**
     * 类别id
     */
    private int category_id;

    /**
     * 账单年
     */
    private int year;
    /**
     * 账单月
     */
    private int month;
    /**
     * 账单日
     */
    private int day;


    /**
     * 是否删除(0未删除，1删除)
     */
    private int status =0;

    /**
     * 账单用户id(默认为-1)
     */
    private int user_id = -1;

    private long createTime;
    private long updateTime;
    /**
     * 用户账单时间
     */
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType_imagepath() {
        return type_imagepath;
    }

    public void setType_imagepath(String type_imagepath) {
        this.type_imagepath = type_imagepath;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
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

    public double getAccount() {
        if (account != 0.00) {
            java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
            return Double.parseDouble(df.format(account));
        } else {
            return 0.00;
        }
    }

    public void setAccount(double account) {
        this.account = account;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "CategoryDBbean{" +
                "bill_id='" + bill_id + '\'' +
                ", demo='" + demo + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", type_imagepath='" + type_imagepath + '\'' +
                ", account=" + account +
                ", category_id=" + category_id +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", status=" + status +
                ", user_id=" + user_id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
