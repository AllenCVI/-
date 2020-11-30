package com.kuxuan.moneynote.json;

import java.io.Serializable;

/**
 * Created by xieshengqi on 2017/10/31.
 */

public class FindJson implements Serializable{


    /**
     * id : 1
     * name : 理财
     * icon : 127.0.0.1
     * url : http://www.baidu.com
     * created_at : 0
     * updated_at : 0
     */

    private int id;
    private String name;
    private String icon;
    private String url;
    private int created_at;
    private int updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public int getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(int updated_at) {
        this.updated_at = updated_at;
    }
}
