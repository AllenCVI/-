package com.kuxuan.moneynote.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import com.kuxuan.moneynote.MyApplication;
import com.kuxuan.moneynote.common.Constant;


/**
 * Created by xieshengqi on 2018/4/26.
 */

public class DrawableUtil {


    /**
     * 登录按钮颜色
     *
     * @param context
     */
    public static GradientDrawable getShape(Context context) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(DisplayUtil.dip2px(10));
        shape.setColor(Color.parseColor("#"+(String) SPUtil.get(MyApplication.getInstance(), Constant.Skin.COLOR_SELECT, Constant.Skin.COLOR_NORMAL)));
        return shape;
    }

    public static int getSkinColor(Context context){
        String color = "#"+((String) SPUtil.get(MyApplication.getInstance(), Constant.Skin.COLOR_SELECT, Constant.Skin.COLOR_NORMAL));
    return  Color.parseColor(color);
    }
}
