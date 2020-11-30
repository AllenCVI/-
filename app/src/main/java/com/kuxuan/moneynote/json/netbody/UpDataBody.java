package com.kuxuan.moneynote.json.netbody;

/**
 * Created by xieshengqi on 2018/4/8.
 */

public class UpDataBody {
    private int page;

    private String data;


    public UpDataBody(int page, String data) {
        this.page = page;
        this.data = data;
    }

    public UpDataBody(int page) {
        this.page = page;
    }
}
