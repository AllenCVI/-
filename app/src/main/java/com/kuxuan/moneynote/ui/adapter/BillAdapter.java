package com.kuxuan.moneynote.ui.adapter;

import android.support.annotation.LayoutRes;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.json.BillJsonList;
import com.kuxuan.moneynote.ui.adapter.viewholder.BillHolder;
import com.kuxuan.moneynote.utils.TextSetUtil;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class BillAdapter extends BaseQuickAdapter<BillJsonList,BillHolder> {
    public BillAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }
    @Override
    protected void convert(BillHolder helper, BillJsonList item) {
        helper.setText(R.id.stock_list_header_qi,item.getMonth()+"月");
        TextSetUtil.setTextForMoey(item.getIncome(),(TextView)helper.getView(R.id.stock_list_header_price),18,12);
        TextSetUtil.setTextForMoey(item.getPay(),(TextView)helper.getView(R.id.stock_list_header_last_lixi),18,12);
        TextSetUtil.setTextForMoey(item.getBalance(),(TextView)helper.getView(R.id.stock_list_header_price_change),18,12);
    }


}
