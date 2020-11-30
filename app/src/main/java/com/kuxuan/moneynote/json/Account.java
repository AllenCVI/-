package com.kuxuan.moneynote.json;

import android.support.annotation.DrawableRes;

import java.io.Serializable;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 * @factory 记账界面的数据
 */
public class Account implements Serializable {
    private @DrawableRes int imageResId ;

    private String name;

    private boolean isClick;

    public boolean getIsClick() {
        return isClick;
    }

    public void setIsClick(boolean isClick) {
        this.isClick = isClick;
    }

    public Account(int imageResId, String name) {
        this.imageResId = imageResId;
        this.name = name;
        this.isClick = false;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
