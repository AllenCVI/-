package com.kuxuan.moneynote.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kuxuan.moneynote.R;
import com.kuxuan.sqlite.db.BillCategoreDB;

import java.util.List;

/**
 * Created by Allence on 2018/4/2 0002.
 */

public class AccountAdapterOffline extends BaseQuickAdapter<BillCategoreDB,BaseViewHolder>{


    public AccountAdapterOffline(int layoutResId, @Nullable List<BillCategoreDB> data) {
        super(layoutResId, data);
    }

    public AccountAdapterOffline(@Nullable List<BillCategoreDB> data) {
        super(data);
    }

    public AccountAdapterOffline(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, BillCategoreDB item) {


        if(helper.getAdapterPosition()==mData.size()-1){
            Glide.with(mContext).load(R.drawable.tallytype_set).into((ImageView) helper.getView(R.id.im_portrait));
            helper.setText(R.id.tv_title,"设置");
            return;
        }

        helper.setText(R.id.tv_title,item.getName());
        ImageView imageView = helper.getView(R.id.im_portrait);
        if(item.isClick()){
            Glide.with(mContext).load(item.getSelected_icon()).into(imageView);
        }else {
            Glide.with(mContext).load(item.getIcon()).into(imageView);
        }

    }
}
