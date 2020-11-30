package com.kuxuan.moneynote.ui.adapter;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.json.CategoryList;
import com.kuxuan.moneynote.ui.adapter.viewholder.AccountHolder;

import net.qiujuer.genius.ui.compat.UiCompat;

import java.util.List;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class AccountAdapter extends BaseQuickAdapter<CategoryList,AccountHolder> {


    public AccountAdapter(@LayoutRes int layoutResId, @Nullable List<CategoryList> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(AccountHolder helper, CategoryList item) {
        helper.setText(R.id.tv_title,item.getName());
        if(item.getId()=="0"){
            Glide.with(mContext).load(R.drawable.tallytype_set).into((ImageView) helper.getView(R.id.im_portrait));
            return;
        }
        if(item.isClick()==true){
            // 初始化背景
            Glide.with(mContext)
                    .load(item.getIcon())
                    .centerCrop() //居中剪切
                    .into(new ViewTarget<ImageView, GlideDrawable>((ImageView) helper.getView(R.id.im_portrait)) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            // 拿到glide的Drawable
                            Drawable drawable = resource.getCurrent();
                            // 使用适配类进行包装
                            drawable = DrawableCompat.wrap(drawable);
                            drawable.setColorFilter(UiCompat.getColor(mContext.getResources(), R.color.bg_title_gray),
                                    PorterDuff.Mode.MULTIPLY); // 设置着色的效果和颜色，蒙板模式
                            // 设置给ImageView
                            this.view.setImageDrawable(drawable);
                        }
                    });
        }else{
            Glide.with(mContext)
                    .load(item.getIcon())
                    .centerCrop() //居中剪切
                    .into(new ViewTarget<ImageView, GlideDrawable>((ImageView) helper.getView(R.id.im_portrait)) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            // 拿到glide的Drawable
                            Drawable drawable = resource.getCurrent();
                            // 使用适配类进行包装
                            drawable = DrawableCompat.wrap(drawable);
                            drawable.setColorFilter(UiCompat.getColor(mContext.getResources(), R.color.category_color),
                                    PorterDuff.Mode.MULTIPLY); // 设置着色的效果和颜色，蒙板模式
                            // 设置给ImageView
                            this.view.setImageDrawable(drawable);
                        }
                    });
        }

    }
}
