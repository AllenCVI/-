package com.kuxuan.moneynote.json;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xieshengqi on 2018/4/8.
 */

public class UploadBeanJson implements Serializable {


    private int number;
    private int maxId;


    public int getMaxId() {
        return maxId;
    }

    public void setMaxId(int maxId) {
        this.maxId = maxId;
    }

    private ArrayList<UploadDbjson> data;


    private int page;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArrayList<UploadDbjson> getData() {
        return data;
    }

    public void setData(ArrayList<UploadDbjson> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
