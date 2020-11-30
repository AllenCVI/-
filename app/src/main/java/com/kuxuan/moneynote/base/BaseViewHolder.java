package com.kuxuan.moneynote.base;

import android.view.View;

import com.zhy.autolayout.utils.AutoUtils;

/**
 *
 * Created by xieshengqi on 2017/4/10.
 */

public class BaseViewHolder extends com.chad.library.adapter.base.BaseViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
        AutoUtils.autoSize(itemView);
    }
}
