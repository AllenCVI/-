package com.kuxuan.moneynote.ui.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseViewHolder;

/**
 * Created by xieshengqi on 2017/10/20.
 */

public class ReportViewHolder extends BaseViewHolder {


    public ImageView imageView;
    public TextView  name_text,money_text,time_text;
    public ProgressBar progressBar;
    public View line_View;
    public ReportViewHolder(View itemView) {
        super(itemView);
        time_text = itemView.findViewById(R.id.item_report_time_text);
        imageView = itemView.findViewById(R.id.item_report_image);
        name_text = itemView.findViewById(R.id.item_report_name_text);
        money_text = itemView.findViewById(R.id.item_report_mingxi_text);
        progressBar = itemView.findViewById(R.id.item_report_progressbar);
        line_View = itemView.findViewById(R.id.item_detial_view2);
    }
}
