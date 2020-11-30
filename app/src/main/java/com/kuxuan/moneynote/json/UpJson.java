package com.kuxuan.moneynote.json;

import java.io.Serializable;

/**
 * Created by xieshengqi on 2018/4/8.
 */

public class UpJson implements Serializable {


    /**
     * identification : 2018032914031975009630
     * status : 0
     * demo : 20
     * time : 1521475200
         * account : 10.0
     * category_name : 餐饮
     * category_id : 36
     * type : 2
     * user_id : 10047
     */

    private String identification;
    private String status;
    private String demo;
    private String time;
    private String account;
    private String category_name;
    private String category_id;
    private String type;
    private int user_id;
    private String created_at;
    private String updated_at;


    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
