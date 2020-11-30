package com.kuxuan.moneynote.json;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public class DetialJson {

    private String name;
    private String data;


    /**
     * 判断是不是同一天的type
     */
    private int type =-1;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DetialJson() {
    }

    public DetialJson(String name) {
        this.name = name;
    }

    public DetialJson(String name, String data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
