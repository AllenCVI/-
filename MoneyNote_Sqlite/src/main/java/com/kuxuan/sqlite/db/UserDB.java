package com.kuxuan.sqlite.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 用户表，用于存储用户id
 * Created by xieshengqi on 2018/4/2.
 */
@Entity
public class UserDB {


    /**
     * 用户id
     */
    @Unique
    private int user_id;

    /**
     * 同步时间
     */
    private long syncTime;

    @Generated(hash = 1168103406)
    public UserDB(int user_id, long syncTime) {
        this.user_id = user_id;
        this.syncTime = syncTime;
    }

    @Generated(hash = 1312299826)
    public UserDB() {
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public long getSyncTime() {
        return this.syncTime;
    }

    public void setSyncTime(long syncTime) {
        this.syncTime = syncTime;
    }



}
