package com.kuxuan.moneynote.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kuxuan.moneynote.json.FindJson;
import com.kuxuan.moneynote.ui.adapter.viewholder.FindViewHolder;
import com.kuxuan.moneynote.utils.GlideUtil;

import java.util.List;

/**
 * Created by xieshengqi on 2017/10/31.
 */

public class FindAdapter extends BaseQuickAdapter<FindJson, FindViewHolder> {
    public FindAdapter(@LayoutRes int layoutResId, @Nullable List<FindJson> data) {
        super(layoutResId, data);
    }

    public FindAdapter(@Nullable List<FindJson> data) {
        super(data);
    }

    public FindAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(FindViewHolder helper, FindJson item) {
        GlideUtil.setImageWithCirlce(mContext, item.getIcon(), helper.imageView);
        helper.textView.setText(item.getName());
    }
}
