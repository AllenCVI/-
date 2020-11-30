package com.kuxuan.moneynote.ui.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseViewHolder;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class BillHolder extends BaseViewHolder {
    TextView stock_list_header_qi;
    TextView stock_list_header_price;
    TextView stock_list_header_last_lixi;
    TextView stock_list_header_price_change;
    public BillHolder(View itemView) {
        super(itemView);
        stock_list_header_qi = itemView.findViewById(R.id.stock_list_header_qi);
        stock_list_header_price = itemView.findViewById(R.id.stock_list_header_price);

        stock_list_header_last_lixi = itemView.findViewById(R.id.stock_list_header_last_lixi);
        stock_list_header_price_change = itemView.findViewById(R.id.stock_list_header_price_change);

    }
}

