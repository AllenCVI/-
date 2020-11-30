package com.kuxuan.moneynote.api;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by xieshengqi on 2017/4/13.
 */

public abstract class MyObsever<T> implements Observer<T> {


    @Override
    public void onSubscribe(Disposable d) {

    }


    @Override
    public void onNext(T t) {
        onSuccess(t);

    }

    @Override
    public void onError(Throwable e) {
        onError(ExceptionHandle.handleException(e));
    }

    @Override
    public void onComplete() {

    }


    public abstract void onError(ExceptionHandle.ResponeThrowable e);

    public abstract void onSuccess(T t);


}
