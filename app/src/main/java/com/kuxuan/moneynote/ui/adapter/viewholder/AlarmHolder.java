package com.kuxuan.moneynote.ui.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseViewHolder;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class AlarmHolder extends BaseViewHolder {
    ImageView category_img;
    TextView category_text;
    public AlarmHolder(View itemView) {
        super(itemView);
        category_img = itemView.findViewById(R.id.category_img);
        category_text = itemView.findViewById(R.id.category_text);

    }
}
