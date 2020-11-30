package com.kuxuan.moneynote.json.netbody;

/**
 * Created by xieshengqi on 2017/10/24.
 */

public class UserAllBillBody {
    private String year;
    private String month;


    public UserAllBillBody(String year, String month) {
        this.year = year;
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
