package com.kuxuan.moneynote.json;

import java.io.Serializable;
import java.util.List;

/**
 * 新版报表数据
 * Created by xieshengqi on 2018/3/6.
 */

public class NewCategoryJson implements Serializable {

    private String account;

    private List<TypeDataJson> category;

    private List<TypeDataJson> detail_data;

    public List<TypeDataJson> getDetial_data() {
        return detail_data;
    }

    public void setDetial_data(List<TypeDataJson> detial_data) {
        this.detail_data = detial_data;
    }

    private List<TimeDataJson> time_data;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<TypeDataJson> getCategory() {
        return category;
    }

    public void setCategory(List<TypeDataJson> category) {
        this.category = category;
    }

    public List<TimeDataJson> getTime_data() {
        return time_data;
    }

    public void setTime_data(List<TimeDataJson> time_data) {
        this.time_data = time_data;
    }
}
