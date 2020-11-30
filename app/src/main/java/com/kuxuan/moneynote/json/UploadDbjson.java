package com.kuxuan.moneynote.json;

import java.io.Serializable;

/**
 * 下载数据
 * Created by xieshengqi on 2018/4/8.
 */

public class UploadDbjson implements Serializable{


    /**
     * id : 28
     * user_id : 37
     * category_id : 23
     * category_name : 医疗
     * type : 2
     * account : 20.00
     * created_at : 0
     * updated_at : 0
     * demo : 26
     * time : 1522308774
     * status : 0
     * identification : 2018032914054653306953
     */

    private int id;
    private int user_id;
    private int category_id;
    private String category_name;
    private int type;
    private String account;
    private long created_at;
    private long updated_at;
    private String demo;
    private long time;
    private int status;
    private String identification;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
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

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

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

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }
}
