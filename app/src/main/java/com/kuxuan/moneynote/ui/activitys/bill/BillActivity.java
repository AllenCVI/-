package com.kuxuan.moneynote.ui.activitys.bill;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.mvpbase.MVPFragmentActivity;
import com.kuxuan.moneynote.json.BillJson;
import com.kuxuan.moneynote.json.BillJsonList;
import com.kuxuan.moneynote.ui.activitys.eventbus.LoginOutEvent;
import com.kuxuan.moneynote.utils.DrawableUtil;
import com.kuxuan.moneynote.utils.PickerUtil;
import com.kuxuan.moneynote.utils.TextSetUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import cn.addapp.pickers.picker.NumberPicker;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class BillActivity extends MVPFragmentActivity<BillPresenter, BillModel> implements BillContract.BillView {
    @Bind(R.id.recycler)
    RecyclerView mRecyclerView;
    @Bind(R.id.balance_text)
    TextView mBalanceText;
    @Bind(R.id.income_text)
    TextView mIncomeText;
    @Bind(R.id.pay_text)
    TextView mPayText;
    Calendar cal;
    @Bind(R.id.activity_minebill_layout)
    LinearLayout all_layout;
    int year;
    private int finalyear;
    private static final String TAG = "BillActivity";

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {
        TextSetUtil.setTextForMoeyforNomal("0", mBalanceText);
        TextSetUtil.setTextForMoeyforNomal("0", mIncomeText);
        TextSetUtil.setTextForMoeyforNomal("0", mPayText);
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        finalyear = cal.get(Calendar.YEAR);
        final Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.pull_down_selector_normal);
        //设置title
        getTitleView(1).setTitle(getResources().getString(R.string.bill)).
                setTitleColor(this, R.color.white).
                setLeftImage(R.drawable.toolbar_navigation_icon_normal, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }).setLeft_text("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        })
//                .setRightImage(R.mipmap.pull_down_selector_normal,new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PickerUtil.onYearPicker(BillActivity.this, new NumberPicker.OnNumberPickListener() {
//                    @Override
//                    public void onNumberPicked(int i, Number number) {
//                        if (number.intValue()<=years) {
//                            mPresenter.getBillData(number.intValue());
//                            getTitleView(2).setRightText(number.intValue()+"");
//                            year = number.intValue();
//                        }else{
//                            Toast.makeText(BillActivity.this, "您选择的年不能大于当前年", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },year);
//            }
//        })
                .setRight_text(year + "", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPicker();
                    }
                });
//        mPresenter.getBillData(year);
        mPresenter.initRecyclerView(this, mRecyclerView);
        setOfflineBillData();
        all_layout.setBackgroundColor(DrawableUtil.getSkinColor(this));
    }


    NumberPicker picker;

    /**
     * 展示时间选择器
     */
    private void showPicker() {
        if (picker == null) {
            picker = PickerUtil.getOnYearPicker(BillActivity.this, new NumberPicker.OnNumberPickListener() {
                @Override
                public void onNumberPicked(int i, Number number) {
                    if (number.intValue() <= finalyear) {
//                                    mPresenter.getBillData(number.intValue());
                        getTitleView(1).setRight_text(number.intValue() + "");
                        year = number.intValue();
                        setOfflineBillData();
                    } else {
                        Toast.makeText(BillActivity.this, "您选择的年不能大于当前年", Toast.LENGTH_SHORT).show();
                    }
                }
            }, year);
        }
        picker.show();
    }

    private void setOfflineBillData() {

        mPresenter.getOfflineData(year);

    }

    @Override
    public int getLayout() {
        return R.layout.activity_bill;
    }

    public static void show(Context context) {
        Intent intent = new Intent(context, BillActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void setBillData(BillJson billJson) {
        TextSetUtil.setTextForMoeyforNomal(billJson.getTotal_balance(), mBalanceText);
        TextSetUtil.setTextForMoeyforNomal(billJson.getTotal_income(), mIncomeText);
        TextSetUtil.setTextForMoeyforNomal(billJson.getTotal_pay(), mPayText);

    }

    @Override
    public void setData() {
        TextSetUtil.setTextForMoeyforNomal("0", mBalanceText);
        TextSetUtil.setTextForMoeyforNomal("0", mIncomeText);
        TextSetUtil.setTextForMoeyforNomal("0", mPayText);
    }

    @Override
    public void setOffLineBillData(List<BillJsonList> billJsonLists) {

        double income_Sum = 0;
        double pay_Sum = 0;
        double balance_Sum = 0;

        for (int i = 0; i < billJsonLists.size(); i++) {
            income_Sum = Double.parseDouble(billJsonLists.get(i).getIncome()) + income_Sum;
            pay_Sum = Double.parseDouble(billJsonLists.get(i).getPay()) + pay_Sum;
        }
        balance_Sum = income_Sum - pay_Sum;

        TextSetUtil.setTextForMoeyforNomal(TextSetUtil.formatFloatNumber(balance_Sum), mBalanceText);
        TextSetUtil.setTextForMoeyforNomal(TextSetUtil.formatFloatNumber(income_Sum), mIncomeText);
        TextSetUtil.setTextForMoeyforNomal(TextSetUtil.formatFloatNumber(pay_Sum), mPayText);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent2(LoginOutEvent loginOutEvent) {
        finish();
    }
}
