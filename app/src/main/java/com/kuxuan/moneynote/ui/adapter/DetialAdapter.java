package com.kuxuan.moneynote.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kuxuan.moneynote.json.BillData;
import com.kuxuan.moneynote.json.TypeDataJson;
import com.kuxuan.moneynote.ui.adapter.viewholder.DetialViewHolder;
import com.kuxuan.moneynote.utils.GlideUtil;
import com.kuxuan.moneynote.utils.JavaFormatUtils;

import java.util.List;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public class DetialAdapter extends BaseQuickAdapter<TypeDataJson, DetialViewHolder> {


    public DetialAdapter(@LayoutRes int layoutResId, @Nullable List<TypeDataJson> data) {
        super(layoutResId, data);
    }


    public DetialAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(DetialViewHolder helper, TypeDataJson item) {
        int layoutPosition = helper.getLayoutPosition();
        helper.tv_beizhu.setText("");
        helper.tv_beizhu.setVisibility(View.GONE);
        if (item.isTrueData()) {
            helper.layout.setVisibility(View.VISIBLE);
            if (item.isFirst()) {
                helper.title_layout.setVisibility(View.VISIBLE);
                BillData billData = (BillData) item.getTag();
                helper.time.setText(item.getDay_type());
                if (billData.getDay_income_account() == 0) {
                    helper.money.setText("  支出:" + JavaFormatUtils.formatFloatNumber (billData.getDay_pay_account()));
                } else if (billData.getDay_pay_account() == 0) {
                    helper.money.setText("收入:" +  JavaFormatUtils.formatFloatNumber (billData.getDay_income_account()));
                } else if (billData.getDay_income_account() != 0 && billData.getDay_pay_account() != 0) {
                    helper.money.setText("收入:" +  JavaFormatUtils.formatFloatNumber (billData.getDay_income_account()) + "  支出:" +   JavaFormatUtils.formatFloatNumber (billData.getDay_pay_account()));
                }
                
//            helper.view1.setVisibility(View.VISIBLE);
            } else {
                helper.title_layout.setVisibility(View.GONE);
//            helper.view1.setVisibility(View.GONE);
            }
            if (layoutPosition + 1 < getItemCount()) {
                if (getData().get(layoutPosition + 1).isFirst()) {
                    helper.view2.setVisibility(View.GONE);
                } else {
                    helper.view2.setVisibility(View.VISIBLE);
                }
            } else {
                helper.view2.setVisibility(View.GONE);
            }
            String demo = item.getDemo();
            helper.type_name.setText(item.getName());

            if (demo!=null&&!demo.equals("")) {
                helper.tv_beizhu.setVisibility(View.VISIBLE);
                helper.tv_beizhu.setText(item.getDemo());
            }

            if (item.getType() == 2) {
                helper.mingxi_text.setText("-" + JavaFormatUtils.formatFloatNumber(Double.parseDouble(item.getAccount())));
            } else {
                helper.mingxi_text.setText(JavaFormatUtils.formatFloatNumber(Double.parseDouble(item.getAccount())));
            }
            GlideUtil.setImageWithCache(mContext, item.getSmall_icon(), helper.imageView);
        } else {
            helper.layout.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return getData().size();
    }


}
