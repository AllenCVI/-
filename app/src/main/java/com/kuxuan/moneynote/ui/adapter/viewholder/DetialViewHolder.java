package com.kuxuan.moneynote.ui.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseViewHolder;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public class DetialViewHolder extends BaseViewHolder {

    public TextView time,money,type_name,mingxi_text,tv_beizhu;
    public ImageView imageView;
    public RelativeLayout title_layout;
    public View view1,view2;
    public View layout;

    public DetialViewHolder(View itemView) {
        super(itemView);
        time  = itemView.findViewById(R.id.item_detial_time_text);
        money  = itemView.findViewById(R.id.item_detial_money_text);
        type_name  = itemView.findViewById(R.id.item_detial_name_text);
        tv_beizhu = itemView.findViewById(R.id.tv_beizhu);
        mingxi_text  = itemView.findViewById(R.id.item_detial_mingxi_text);
        imageView = itemView.findViewById(R.id.item_detial_image);
        title_layout = itemView.findViewById(R.id.item_detial_title_layout);
        view1 = itemView.findViewById(R.id.item_detial_view1);
        view2 = itemView.findViewById(R.id.item_detial_view2);
        layout = itemView.findViewById(R.id.item_detial_layout);
    }
}
