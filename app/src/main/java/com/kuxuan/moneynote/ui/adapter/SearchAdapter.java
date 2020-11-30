package com.kuxuan.moneynote.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kuxuan.moneynote.json.TypeDataJson;
import com.kuxuan.moneynote.ui.adapter.viewholder.SearchViewHolder;
import com.kuxuan.moneynote.utils.GlideUtil;
import com.kuxuan.moneynote.utils.JavaFormatUtils;

import java.util.List;

/**
 * Created by xieshengqi on 2018/4/25.
 */

public class SearchAdapter extends BaseQuickAdapter<TypeDataJson, SearchViewHolder> {
    public SearchAdapter(int layoutResId, @Nullable List<TypeDataJson> data) {
        super(layoutResId, data);
    }

    public SearchAdapter(@Nullable List<TypeDataJson> data) {
        super(data);
    }

    public SearchAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(SearchViewHolder helper, TypeDataJson item) {
        String demo = item.getDemo();
        helper.type_name.setText(item.getName());
        if (demo != null && !demo.equals("")) {
            helper.tv_beizhu.setVisibility(View.VISIBLE);
            helper.tv_beizhu.setText(item.getDemo());
        }
        if (item.getType() == 2) {
            helper.money.setText("-" + JavaFormatUtils.formatFloatNumber(Double.parseDouble(item.getAccount())));
        } else {
            helper.money.setText(JavaFormatUtils.formatFloatNumber(Double.parseDouble(item.getAccount())));
        }
        GlideUtil.setImageWithCache(mContext, item.getSmall_icon(), helper.imageView);
    }
}
