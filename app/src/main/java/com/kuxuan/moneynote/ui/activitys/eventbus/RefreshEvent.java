package com.kuxuan.moneynote.ui.activitys.eventbus;

/**
 * Created by xieshengqi on 2017/10/25.
 */

public class RefreshEvent {

    private boolean isNeedRefresh;


    private Object tag;


    public boolean isNeedRefresh() {
        return isNeedRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
        isNeedRefresh = needRefresh;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
