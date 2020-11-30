package com.kuxuan.moneynote.json;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public class BaseJson<T> implements Serializable {


    private ArrayList<String> message;


    public ArrayList<String> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<String> message) {
        this.message = message;
    }

    private int code;
    private T res;


    public ArrayList<String> getSuccess() {
        return message;
    }

    public void setSuccess(ArrayList<String> success) {
        this.message = success;
    }

    public ArrayList<String> getError() {
        return message;
    }

    public void setError(ArrayList<String> error) {
        this.message = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return res;
    }

    public void setData(T data) {
        this.res = data;
    }
}
