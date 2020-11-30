package com.kuxuan.moneynote.listener;

/**
 * Created by xieshengqi on 2017/7/6.
 */

public interface MVPListener<E>{


    public void onSuccess(E content);


    public  void onFail(String msg);
}
