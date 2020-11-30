package com.kuxuan.moneynote.ui.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseViewHolder;

/**
 * Created by xieshengqi on 2018/4/25.
 */

public class SearchViewHolder extends BaseViewHolder {
    public TextView money, type_name, tv_beizhu;
    public ImageView imageView;

    public SearchViewHolder(View itemView) {
        super(itemView);
        money = itemView.findViewById(R.id.item_search_mingxi_text);
        type_name = itemView.findViewById(R.id.item_search_name_text);
        tv_beizhu = itemView.findViewById(R.id.item_search_beizhu);
        imageView = itemView.findViewById(R.id.item_search_image);
    }
}
