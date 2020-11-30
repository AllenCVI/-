package com.kuxuan.moneynote.json;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xieshengqi on 2017/10/20.
 */

public class LineJson implements Serializable{

    private String xAixs;

    private float yAixs;
    //总的值
    private int allValues;

    private float xPoint;

    private float yPoint;


    private Object tag;



    private ArrayList<PopCharData> popData;


    public ArrayList<PopCharData> getPopData() {
        return popData;
    }

    public LineJson setPopData(ArrayList<PopCharData> popData) {
        this.popData = popData;
        return this;
    }

    public Object getTag() {
        return tag;
    }

    public LineJson setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public int getAllValues() {
        return allValues;
    }

    public void setAllValues(int allValues) {
        this.allValues = allValues;
    }

    public LineJson(String xAixs, float yAixs) {
        this.xAixs = xAixs;
        String da = yAixs+"";
        if(da.startsWith("-")){
            this.yAixs = Integer.parseInt(da.replace("-",""));
        }else{
        this.yAixs = yAixs;
        }
    }

    public float getxPoint() {
        return xPoint;
    }

    public void setxPoint(float xPoint) {
        this.xPoint = xPoint;
    }

    public float getyPoint() {
        return yPoint;
    }

    public void setyPoint(float yPoint) {
        this.yPoint = yPoint;
    }

    public String getxAixs() {
        return xAixs;
    }

    public void setxAixs(String xAixs) {
        this.xAixs = xAixs;
    }

    public float getyAixs() {
        return yAixs;
    }

    public void setyAixs(float yAixs) {
        this.yAixs = yAixs;
    }
}
