package com.kuxuan.moneynote.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kuxuan.moneynote.json.ReportJson;
import com.kuxuan.moneynote.json.TypeDataJson;
import com.kuxuan.moneynote.ui.adapter.viewholder.ReportViewHolder;
import com.kuxuan.moneynote.utils.GlideUtil;
import com.kuxuan.moneynote.utils.JavaFormatUtils;

import java.util.List;

/**
 * Created by xieshengqi on 2017/10/20.
 */

public class ReportAdapter extends BaseQuickAdapter<ReportJson, ReportViewHolder> {
    //判断是收入还是支出（默认支出）
    private int type = 1;

    /**
     * 是不是详情页的adapter
     */
    private boolean isDetial = false;


    public boolean isDetial() {
        return isDetial;
    }

    public void setDetial(boolean detial) {
        isDetial = detial;
    }

    public ReportAdapter(@LayoutRes int layoutResId, @Nullable List<ReportJson> data) {
        super(layoutResId, data);
    }

    public ReportAdapter(@Nullable List<ReportJson> data) {
        super(data);
    }

    public ReportAdapter(@LayoutRes int layoutResId, boolean isDetial) {
        super(layoutResId);
        this.isDetial = isDetial;
    }


    @Override
    protected void convert(ReportViewHolder helper, ReportJson item) {
        if (!isDetial) {
            helper.time_text.setVisibility(View.GONE);
            String data = "";
//            CategoryDataJson json = (CategoryDataJson) item.getTag();
            TypeDataJson typeDataJson = (TypeDataJson) item.getTag();
            if (typeDataJson == null) {
                return;
            }
//            List<TypeDataJson> data1 = json.getData();
            GlideUtil.setImageWithNoCache(mContext, typeDataJson.getSmall_icon(), helper.imageView);
            //判断是收入还是支出
//            if (data1 != null && data1.size() != 0) {
//                type = data1.get(0).getType();
//            }
            type = typeDataJson.getType();
            switch (type) {
                case 2:
                    double x= item.getAllMoney()*100;
                   double fenzi =  item.getOutmoney()*100;
                    data = JavaFormatUtils.getData((fenzi/x)*100);
                    helper.money_text.setText(changeFloatData(Double.parseDouble(typeDataJson.getAccount())) + "");
                    helper.progressBar.setProgress((int) Math.round(item.getOutmoney() * 100 / item.getAllMoney()));
                    break;
                case 1:
                    double x1= item.getAllMoney()*100 ;
                    double fenz = item.getInmoney()*100;
                    data = JavaFormatUtils.getData((fenz/x1)*100);
//                    JavaFormatUtils.formatFloatNumber(Float.parseFloat(typeDataJson.getAccount())
                    helper.money_text.setText(changeFloatData(Double.parseDouble(typeDataJson.getAccount())) + "");
                    helper.progressBar.setProgress((int) Math.round(item.getInmoney() * 100 / item.getAllMoney()));
                    break;
            }
            helper.name_text.setText(typeDataJson.getName() + "  " + data + "%");
            GlideUtil.setImageWithNoCache(mContext, typeDataJson.getSmall_icon(), helper.imageView);
        } else {
            helper.time_text.setVisibility(View.VISIBLE);
            String data = "";
            TypeDataJson typeDataJson = (TypeDataJson) item.getTag();
            type = typeDataJson.getType();
            switch (type) {
                case 2:
                    double f = item.getOutmoney() / item.getAllMoney();
                    data = JavaFormatUtils.getData(f * 100);
                    helper.money_text.setText(changeFloatData(Double.parseDouble(typeDataJson.getAccount())) + "");
                    helper.progressBar.setProgress((int) Math.round(item.getOutmoney() * 100 / item.getAllMoney()));
                    break;
                case 1:
                    data = JavaFormatUtils.getData(item.getInmoney() / item.getAllMoney() * 100);
                    helper.money_text.setText(changeFloatData(Double.parseDouble(typeDataJson.getAccount())) + "");
                    helper.progressBar.setProgress((int) Math.round(item.getInmoney() * 100 / item.getAllMoney()));
                    break;
            }
            String day = typeDataJson.getDay();
            String[] split = day.split("-");
            GlideUtil.setImageWithNoCache(mContext, typeDataJson.getSmall_icon(), helper.imageView);
            helper.time_text.setText(split[0] + "年" + split[1] + "月" + split[2] + "日");
//            String demo = typeDataJson.getDemo();
//            if (TextUtils.isEmpty(demo))
                helper.name_text.setText(typeDataJson.getName() + "  " + data + "%");
//            else
//                helper.name_text.setText(demo + "  " + data + "%");
        }

        int layoutPosition = helper.getLayoutPosition();
        if (layoutPosition == getData().size() - 1) {
            helper.line_View.setVisibility(View.GONE);
        } else {
            helper.line_View.setVisibility(View.VISIBLE);
        }
    }


    private String changeFloatData(double account) {
        return String.format("%.2f", account);
//        java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
//        return df.format(account);
    }
}
