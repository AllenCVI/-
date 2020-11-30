package com.kuxuan.moneynote.ui.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseViewHolder;

/**
 * Created by xieshengqi on 2017/10/31.
 */

public class FindViewHolder extends BaseViewHolder {
    public ImageView imageView;
    public TextView textView;
    public FindViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.item_find_imageView);
        textView = itemView.findViewById(R.id.item_find_text);
    }
}
