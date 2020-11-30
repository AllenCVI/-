package com.kuxuan.moneynote.json;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xieshengqi on 2017/10/24.
 */

public class UserAllBillJson implements Serializable{


    private ArrayList<BillData> bill_data;

    private String pay_account="00.00";

    private String income_account="00.00";
    private YearData start_date;
    private YearData end_date;


    public ArrayList<BillData> getBill_data() {
        return bill_data;
    }

    public void setBill_data(ArrayList<BillData> bill_data) {
        this.bill_data = bill_data;
    }

    public String getPay_account() {
        return pay_account;
    }

    public void setPay_account(String pay_account) {
        this.pay_account = pay_account;
    }

    public String getIncome_account() {
        return income_account;
    }

    public void setIncome_account(String income_account) {
        this.income_account = income_account;
    }

    public YearData getStart_data() {
        return start_date;
    }

    public void setStart_data(YearData start_data) {
        this.start_date = start_data;
    }

    public YearData getEnd_data() {
        return end_date;
    }

    public void setEnd_data(YearData end_data) {
        this.end_date = end_data;
    }
}
