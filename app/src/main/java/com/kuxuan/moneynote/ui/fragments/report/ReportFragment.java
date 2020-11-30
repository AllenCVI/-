package com.kuxuan.moneynote.ui.fragments.report;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseFragmentActivity;
import com.kuxuan.moneynote.base.mvpbase.MVPFragment;
import com.kuxuan.moneynote.json.ChartData;
import com.kuxuan.moneynote.ui.activitys.reportchart.ReportChartActivity;
import com.kuxuan.moneynote.ui.weight.ChartLayout;
import com.kuxuan.moneynote.ui.weight.MoneyChoosePop;
import com.kuxuan.moneynote.utils.DisplayUtil;
import com.kuxuan.moneynote.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 报表页面
 * Created by xieshengqi on 2017/10/19.
 */

public class ReportFragment extends MVPFragment<ReportPresent, ReportModel> implements ReportContract.RepView {
    @Bind(R.id.activity_baobiao_rabtn_week)
    RadioButton activityBaobiaoRabtnWeek;
    @Bind(R.id.activity_baobiao_rabtn_month)
    RadioButton activityBaobiaoRabtnMonth;
    @Bind(R.id.activity_baobiao_rabtn_year)
    RadioButton activityBaobiaoRabtnYear;
    @Bind(R.id.activity_baobiao_radiogroup)
    RadioGroup activityBaobiaoRadiogroup;
    @Bind(R.id.fragment_report_tablayout)
    TabLayout fragmentReportTablayout;
    @Bind(R.id.fragment_report_viewpager)
    ViewPager viewPager;

    private BaseFragmentActivity activity;

    public static ReportFragment getInstanceForPage() {
        Bundle bundle = new Bundle();
        bundle.putInt("type", ChartLayout.YUANHUAN);
        ReportFragment reportFragment = new ReportFragment();
        reportFragment.setArguments(bundle);
        return reportFragment;
    }

    private int chartType = ChartLayout.LINE;

    /**
     * 判断是收入还是支出
     * 收入1，支出2
     */
    private int type = 2;

    @Override
    public void initView() {
        activity = (BaseFragmentActivity) getActivity();
        getTitleView(1).setRightImage(R.mipmap.ic_launcher, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //切换圆环
                Intent intent = new Intent(getActivity(), ReportChartActivity.class);
                startActivity(intent);
            }
        });
        if (getArguments() != null) {
            getTitleView(1).setLeft_text("返回", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
            getTitleView(1).getRight_image().setVisibility(View.GONE);
            chartType = getArguments().getInt("type", ChartLayout.LINE);
        }
        getTitleView(1).setTitle("支出");
        Drawable drawable = getResources().getDrawable(R.mipmap.pull_down_selector_normal);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        getTitleView(1).getTitle_text().setCompoundDrawablePadding(DisplayUtil.dip2px(5));
        getTitleView(1).getTitle_text().setCompoundDrawables(null, null, drawable, null);
        getTitleView(1).getTitle_text().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPop();
            }
        });

        mPresenter.init(getActivity(),chartType);
        mPresenter.initRadioGroup(activityBaobiaoRadiogroup);
        mPresenter.initViewPagerTablyout(fragmentReportTablayout, viewPager, chartType);
        mPresenter.changeData(type, mPresenter.getType());
    }


    MoneyChoosePop popupWindow;

    private void showPop() {
        if (popupWindow == null) {
            popupWindow = new MoneyChoosePop(getActivity());
            popupWindow.setOnPopClickListener(new MoneyChoosePop.OnPopClickListener() {
                @Override
                public void onClick(int type) {
                    //todo 选择支出还是收入
                    switch (type) {
                        case MoneyChoosePop.ZHICHU:
                            //支出
                            getTitleView(1).setTitle("支出");
                            mPresenter.changeData(2, mPresenter.getType());
                            type = 2;
                            break;
                        case MoneyChoosePop.SHOURU:
                            //收入
                            getTitleView(1).setTitle("收入");
                            type = 1;
                            mPresenter.changeData(1, mPresenter.getType());
                            break;
                    }
                    showProgress();

                }
            });
        }
        popupWindow.showAsDropDown(findViewById(R.id.fragment_report_title_layout), 0, 0);
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_report;
    }


    @Override
    public void showProgress() {
        activity.showProgressDialog(getResources().getString(R.string.loadding));
    }

    @Override
    public void hideProgress() {
        activity.closeProgressDialog();
    }

    @Override
    public void showNoLogin() {

    }

    @Override
    public void showNoNetWork() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showErrorMsg(String msg) {
        ToastUtil.show(getActivity(), msg);
    }

    @Override
    public void getRadioData(int statisc_type) {
        boolean idNeed = mPresenter.jugeNeedGetData(type, statisc_type);
        if (idNeed) {
            showProgress();
            mPresenter.changeData(type, statisc_type);
        }
    }

    @Override
    public void refreshData(ArrayList<ChartData> chartDatas) {
        mPresenter.refreshTabLayout(fragmentReportTablayout, viewPager, chartDatas);
    }
}
