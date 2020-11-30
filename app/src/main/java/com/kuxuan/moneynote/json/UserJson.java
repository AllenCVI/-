package com.kuxuan.moneynote.json;

import java.io.Serializable;

/**
 * Created by xieshengqi on 2018/4/19.
 */

public class UserJson implements Serializable{


    private  String user_id;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
