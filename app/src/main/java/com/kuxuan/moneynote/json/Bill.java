package com.kuxuan.moneynote.json;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class Bill {
    private String month;
    private String income;
    private String outcome;
    private String balance;

    public Bill(String month, String income, String outcome, String balance) {
        this.month = month;
        this.income = income;
        this.outcome = outcome;
        this.balance = balance;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
