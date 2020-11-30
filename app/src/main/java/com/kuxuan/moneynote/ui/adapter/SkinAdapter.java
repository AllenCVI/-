package com.kuxuan.moneynote.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.netbody.RES;
import com.kuxuan.moneynote.utils.GlideRoundTransform;
import com.kuxuan.moneynote.utils.SPUtil;
import com.kuxuan.moneynote.utils.SkinUtil;

import java.util.List;

/**
 * Created by Allence on 2018/4/25 0025.
 */

public class SkinAdapter extends BaseQuickAdapter<RES,BaseViewHolder> {

    public SkinAdapter(int layoutResId, @Nullable List<RES> data) {
        super(layoutResId, data);
    }

    public SkinAdapter(@Nullable List<RES> data) {
        super(data);
    }

    public SkinAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, RES item) {

        helper.getView(R.id.view).setVisibility(View.VISIBLE);
        helper.getView(R.id.iv_check).setVisibility(View.GONE);
        helper.getView(R.id.progressbar).setVisibility(View.GONE);

       List<Integer> intArr = SkinUtil.getDownloadPicList();

       if(intArr.contains(helper.getLayoutPosition())){
           helper.getView(R.id.view).setVisibility(View.GONE);
       }

        int checked = (int) SPUtil.get(mContext, Constant.Skin.CHECKED,-1);
        if(helper.getLayoutPosition() == checked){
        helper.getView(R.id.iv_check).setVisibility(View.VISIBLE);
        }

        ImageView iv_skin = helper.getView(R.id.iv_skin);
//        GlideUtil.setImageWithNoCache(mContext,item.getPreview_img(),iv_skin,false);
        Glide.with(mContext).load(item.getPreview_img()).bitmapTransform(new GlideRoundTransform(mContext,4,true)).into(iv_skin);


    }
}
