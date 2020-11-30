package com.kuxuan.moneynote.json;

import java.util.List;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class BillJson {

    private String total_income;
    private String total_pay;
    private String total_balance;

    private List<BillJsonList> data;

    public String getTotal_income() {
        return total_income;
    }

    public void setTotal_income(String total_income) {
        this.total_income = total_income;
    }

    public String getTotal_pay() {
        return total_pay;
    }

    public void setTotal_pay(String total_pay) {
        this.total_pay = total_pay;
    }

    public String getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(String total_balance) {
        this.total_balance = total_balance;
    }

    public List<BillJsonList> getmBillJsonList() {
        return data;
    }

    public void setmBillJsonList(List<BillJsonList> mBillJsonList) {
        this.data = mBillJsonList;
    }
}
