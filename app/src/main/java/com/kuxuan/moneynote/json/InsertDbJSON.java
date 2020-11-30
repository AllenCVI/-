package com.kuxuan.moneynote.json;

/**
 * 插入数据库时的坐标
 * Created by xieshengqi on 2018/4/28.
 */

public class InsertDbJSON {

    /**
     * 用户id
     */
    private String user_id;

    /**
     * 用户拉去数据的值
     */
    private int maxId;


    public InsertDbJSON(String user_id, int maxId) {
        this.user_id = user_id;
        this.maxId = maxId;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getMaxId() {
        return maxId;
    }

    public void setMaxId(int maxId) {
        this.maxId = maxId;
    }
}
