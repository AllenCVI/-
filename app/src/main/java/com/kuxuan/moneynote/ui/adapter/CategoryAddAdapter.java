package com.kuxuan.moneynote.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.json.CategoryList;
import com.kuxuan.moneynote.ui.adapter.viewholder.CategoryHolder;

import java.util.List;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class CategoryAddAdapter extends BaseItemDraggableAdapter<CategoryList,CategoryHolder> {

    public CategoryAddAdapter(int layoutResId, List<CategoryList> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(CategoryHolder helper, CategoryList item) {
        helper.setText(R.id.category_text, item.getName())
                .addOnClickListener(R.id.add_img);
        Glide.with(mContext).load(item.getIcon()).into((ImageView) helper.getView(R.id.category_img));




    }

}
