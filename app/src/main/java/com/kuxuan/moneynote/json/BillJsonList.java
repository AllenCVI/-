package com.kuxuan.moneynote.json;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class BillJsonList {
    //收入
    private String income;
    //支出
    private String pay;
    //结余
    private String balance;
    //月份
    private String month;

    public BillJsonList(String income, String pay, String balance, String month) {
        this.income = income;
        this.pay = pay;
        this.balance = balance;
        this.month = month;
    }

    public BillJsonList(){}


    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
