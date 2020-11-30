package com.kuxuan.moneynote.json;

/**
 * Created by xieshengqi on 2018/4/8.
 */

public class BeanNewJson<T> {

    private T res;

    private String message;

    private int code;

    public T getRes() {
        return res;
    }

    public void setRes(T res) {
        this.res = res;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
