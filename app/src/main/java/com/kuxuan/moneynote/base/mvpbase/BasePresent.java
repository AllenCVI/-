package com.kuxuan.moneynote.base.mvpbase;

import java.lang.ref.WeakReference;

/**
 * Created by xieshengqi on 2017/7/3.
 */

public abstract class BasePresent<M, V> {

    public V view;
    public M mModel;
    public WeakReference<V> mViewRef;


    public void attachModelView(M pModel, V pView) {

        mViewRef = new WeakReference<V>(pView);
        this.view = pView;
        this.mModel = pModel;

    }


    public V getView() {
        if (isAttach()) {
            return mViewRef.get();
        } else {
            return null;
        }
    }

    public boolean isAttach() {
        return null != mViewRef && null != mViewRef.get();
    }


    public void onDettach() {
        if (null != mViewRef) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

}
