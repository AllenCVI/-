package com.kuxuan.moneynote.json;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by xieshengqi on 2017/10/20.
 */

public class ReportJson implements Serializable ,Comparable<ReportJson>{



    private String name;
    //收入
    private double inmoney;
    //支出
    private double outmoney;

    private double allMoney;
    private String url;
    //收入还是支出
    private boolean isOut;

    private TypeDataJson typeDataJson;


    public TypeDataJson getTypeDataJson() {
        return typeDataJson;
    }

    public void setTypeDataJson(TypeDataJson typeDataJson) {
        this.typeDataJson = typeDataJson;
    }

    private Object tag;
    public boolean isOut() {
        return isOut;
    }


    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getInmoney() {
        return inmoney;
    }

    public void setInmoney(double inmoney) {
        this.inmoney = inmoney;
    }

    public double getOutmoney() {
        return outmoney;
    }

    public void setOutmoney(double outmoney) {
        this.outmoney = outmoney;
    }

    public double getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(double allMoney) {
        this.allMoney = allMoney;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    @Override
    public int compareTo(@NonNull ReportJson reportJson) {
        if(isOut){
           if( this.outmoney>reportJson.outmoney) {
               return -1;
           } else if(this.outmoney<reportJson.outmoney) {
               return 1;
           }else{
               return 0;
           }
        }else{
            if( this.inmoney>reportJson.inmoney) {
                return -1;
            } else if(this.inmoney<reportJson.inmoney) {
                return 1;
            }else{
                return 0;
            }
        }
    }
}
