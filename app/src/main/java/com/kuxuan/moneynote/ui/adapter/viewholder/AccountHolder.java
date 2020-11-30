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
public class AccountHolder extends BaseViewHolder {
    ImageView im_portrait;
    TextView tv_title;

    public AccountHolder(View itemView) {
        super(itemView);
        im_portrait = itemView.findViewById(R.id.im_portrait);
        tv_title = itemView.findViewById(R.id.tv_title);

    }
}

