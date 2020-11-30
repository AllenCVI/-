package com.kuxuan.moneynote.ui.fragments.reportsingle;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.classic.common.MultipleStatusView;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseFragmentActivity;
import com.kuxuan.moneynote.base.mvpbase.MVPFragment;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.CategoryDataJson;
import com.kuxuan.moneynote.json.ChartData;
import com.kuxuan.moneynote.json.LineJson;
import com.kuxuan.moneynote.json.NewCategoryJson;
import com.kuxuan.moneynote.json.NewChartData;
import com.kuxuan.moneynote.json.PopCharData;
import com.kuxuan.moneynote.json.TypeDataJson;
import com.kuxuan.moneynote.ui.activitys.CatrogyDetialActivity;
import com.kuxuan.moneynote.ui.activitys.edit.EditBillActivity;
import com.kuxuan.moneynote.ui.activitys.eventbus.LoginEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.LoginOutEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.RefreshEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.SkinEvent;
import com.kuxuan.moneynote.ui.activitys.login.LoginActivity;
import com.kuxuan.moneynote.ui.activitys.login.PhoneLoginActivity;
import com.kuxuan.moneynote.ui.activitys.reportchart.ReportChartActivity;
import com.kuxuan.moneynote.ui.weight.ChartLayout;
import com.kuxuan.moneynote.ui.weight.ChartPop;
import com.kuxuan.moneynote.ui.weight.MoneyChoosePop;
import com.kuxuan.moneynote.utils.DisplayUtil;
import com.kuxuan.moneynote.utils.DrawableUtil;
import com.kuxuan.moneynote.utils.UIHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 报表页面
 * 逻辑：适配初始数据：加载周年月，点击radiobtn访问网络数据，
 * 获取到tablayout的数据，然后点击tab的时候加载每个tab的数据，
 * 每次加载数据只是改变图标数据的数据，adapter的数据，没有用viewpager+tablayout模式
 * 说明：category_id是用于是否是带类别参数加载当前页面，如果是会有相应逻辑改变
 * Created by xieshengqi on 2017/10/19.
 */

public class ReportSingleFragment extends MVPFragment<ReportSinglePresent, ReportSingleModel> implements ReportSingleContract.RepView {
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
    @Bind(R.id.fragment_report_detial_chartlayout)
    ChartLayout chartlayout;
    @Bind(R.id.fragment_report_detial_recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.fragment_reportsingle_layout)
    ViewGroup data_laout;
    @Bind(R.id.fragment_reportsingle_nodata_layout)
    ViewGroup nodata_layout;
    @Bind(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;
    @Bind(R.id.fragment_reportsingle_viewgroup_layout)
    LinearLayout viewGroupLinear;


    //无网络和无登录时候的布局
//    @Bind(R.id.layout_nologinornet_layout)
//    LinearLayout nologin_layout;
//    @Bind(R.id.nologinornet_textView)
//    TextView nol_text;
    private BaseFragmentActivity activity;

    @Bind(R.id.fragment_reportsingle)
    LinearLayout view_layout;

    /**
     * 类别id（不是必须）
     */
    private int category_id = -1;
    public static final String CATEGORY_ID = "category_id";

    /**
     * 收入还是支出
     */
    public static final String MONEY_TYPE = "money_tpe";


    /**
     * 约定的点击
     */
    public static final String TITLE = "title";

    /**
     * 约定的年月日
     */
    public static final String RADIOGROUP_TYPE = "radiogroup_type";

    private int radio_type = 1;
    private String title_text = "";

    public static ReportSingleFragment getInstanceForPage() {
        Bundle bundle = new Bundle();
        bundle.putInt("type", ChartLayout.YUANHUAN);
        ReportSingleFragment reportFragment = new ReportSingleFragment();
        reportFragment.setArguments(bundle);
        return reportFragment;
    }

    /**
     * 类别详情的时候调用
     *
     * @param category_id
     * @return
     */
    public static ReportSingleFragment getInstanceForPage(int category_id, int chartType) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", chartType);
        bundle.putInt(CATEGORY_ID, category_id);
        ReportSingleFragment reportFragment = new ReportSingleFragment();
        reportFragment.setArguments(bundle);
        return reportFragment;
    }

    public static ReportSingleFragment getInstanceForPage(int category_id, int chartType, int money_type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", chartType);
        bundle.putInt(CATEGORY_ID, category_id);
        bundle.putInt(MONEY_TYPE, money_type);
        ReportSingleFragment reportFragment = new ReportSingleFragment();
        reportFragment.setArguments(bundle);
        return reportFragment;
    }

    public static ReportSingleFragment getInstanceForPage(int category_id, int chartType, int money_type, String title, int radio_type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", chartType);
        bundle.putInt(CATEGORY_ID, category_id);
        bundle.putInt(MONEY_TYPE, money_type);
        bundle.putInt(RADIOGROUP_TYPE, radio_type);
        bundle.putString(TITLE, title);
        ReportSingleFragment reportFragment = new ReportSingleFragment();
        reportFragment.setArguments(bundle);
        return reportFragment;
    }

    private int chartType = ChartLayout.LINE;

    /**
     * 判断是收入还是支出
     * 收入1，支出2
     */
    private int mType = 2;

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        activity = (BaseFragmentActivity) getActivity();
        getTitleView(1).setRightImage(R.mipmap.icon_yuanhuan, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //切换圆登录的时候能进去
//                if (mPresenter.isLogin()) {
                if (chartType == ChartLayout.LINE) {
                    Intent intent = new Intent(getActivity(), ReportChartActivity.class);
                    intent.putExtra(ReportChartActivity.CATROGY_ID, category_id);
                    intent.putExtra(ReportChartActivity.MONEY_TYPE, mType);
                    intent.putExtra(ReportChartActivity.TITLE, mPresenter.getTitle_text());
                    intent.putExtra(ReportChartActivity.RADIO_TYPE, mPresenter.getType());
                    intent.putExtra(ReportChartActivity.CHARTTYPE, ChartLayout.YUANHUAN);
                    startActivity(intent);
                }
//                }

            }
        });

        //这个时候所在的页是详情页
        if (getArguments() != null) {
            category_id = getArguments().getInt(CATEGORY_ID, -1);
            getTitleView(1).setLeft_text("返回", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
            chartType = getArguments().getInt("type", ChartLayout.LINE);
            if (category_id == -1) {
                getTitleView(1).getRight_image().setVisibility(View.GONE);
            }
            if (chartType == ChartLayout.YUANHUAN) {
                getTitleView(1).getRight_image().setVisibility(View.GONE);
            }
            //默认支出
            mType = getArguments().getInt(MONEY_TYPE, 2);
            title_text = getArguments().getString(TITLE, "");
            radio_type = getArguments().getInt(RADIOGROUP_TYPE, 1);
        }
        if (mType == 2) {
            getTitleView(1).setTitle("支出");
        } else {
            getTitleView(1).setTitle("收入");
        }

        Drawable drawable = getResources().getDrawable(R.mipmap.icon_sanjiao_white);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        getTitleView(1).getTitle_text().setCompoundDrawablePadding(DisplayUtil.dip2px(5));
        getTitleView(1).getTitle_text().setCompoundDrawables(null, null, drawable, null);
        getTitleView(1).getTitle_text().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPop();
            }
        });

        mPresenter.init(getActivity(), chartType, category_id, mType, radio_type);
        mPresenter.initRecyclerView(getActivity(), mRecyclerView);
        mPresenter.initRadioGroup(activityBaobiaoRadiogroup);
        mPresenter.initTabLayout(getActivity(), fragmentReportTablayout);
        chartlayout.setType(mType);
        changeSkinData();
        initCharPop();
        initMiluView();
        checkNet();
        getRadioData(radio_type);
    }


    /**
     * 更换皮肤颜色
     */
    private void changeSkinData() {
        int color = DrawableUtil.getSkinColor(getActivity());
        viewGroupLinear.setBackgroundColor(color);
        RelativeLayout head = getTitleView(1).getHead();
        if (head != null) {
            head.setBackgroundColor(color);
        }
        fragmentReportTablayout.setSelectedTabIndicatorColor(color);
        fragmentReportTablayout.setTabTextColors(getResources().getColor(R.color.gray_text),color);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SkinEvent skinBean) {
       if(skinBean.getCode()!=1000) {
           changeSkinData();
           int c = DrawableUtil.getSkinColor(getActivity());
           activityBaobiaoRabtnWeek.setTextColor(Color.WHITE);
           activityBaobiaoRabtnMonth.setTextColor(Color.WHITE);
           activityBaobiaoRabtnYear.setTextColor(Color.WHITE);
           switch (radio_type) {
               case 1:
                   activityBaobiaoRabtnWeek.setTextColor(c);
                   break;
               case 2:
                   activityBaobiaoRabtnMonth.setTextColor(c);
                   break;
               case 3:
                   activityBaobiaoRabtnYear.setTextColor(c);
                   break;

           }
       }
       }

    private void checkNet() {
//        boolean networkAvailable = NetWorkUtil.isNetworkAvailable(getActivity());
//        if (networkAvailable)
//            mPresenter.loginCheck();
//        else
//            multipleStatusView.showNoNetwork();
        synchronized (ReportSingleFragment.class) {
            mPresenter.loginCheck();
        }
    }

    /**
     * 初始化多布局
     */
    private void initMiluView() {
        multipleStatusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.error_retry_view:
                        //点击重试
                        getDataFirst();
                        break;
                    case R.id.no_network_retry_view:
                        checkNet();
                        break;
                }
            }
        });
    }

    MoneyChoosePop popupWindow;

    private void showPop() {
        if (popupWindow == null) {
            popupWindow = new MoneyChoosePop(getActivity());
            popupWindow.setType(mType);
            popupWindow.setOnPopClickListener(new MoneyChoosePop.OnPopClickListener() {
                @Override
                public void onClick(int type) {
                    //todo 选择支出还是收入
                    switch (type) {
                        case MoneyChoosePop.ZHICHU:
                            //支出
                            getTitleView(1).setTitle("支出");
                            mType = 2;
                            mPresenter.changeData(mType, mPresenter.getType(), category_id);
                            chartlayout.setType(mType);
                            textView.setText("支出排行榜");
                            break;
                        case MoneyChoosePop.SHOURU:
                            //收入
                            getTitleView(1).setTitle("收入");
                            mType = 1;
                            chartlayout.setType(mType);
                            mPresenter.changeData(mType, mPresenter.getType(), category_id);
                            textView.setText("收入排行榜");
                            break;
                    }
//                    showProgress();

                }
            });
        }
//        if (mPresenter.isLogin())
        popupWindow.showAsDropDown(findViewById(R.id.fragment_report_title_layout), 0, 0);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_reportsingle;
    }


    @Override
    public void showProgress() {
//        activity.showProgressDialog(getResources().getString(R.string.loadding));
    }

    @Override
    public void hideProgress() {
        activity.closeProgressDialog();
    }

    @Override
    public void showNoData() {
        nodata_layout.setVisibility(View.VISIBLE);
        data_laout.setVisibility(View.GONE);
//        fragmentReportTablayout.setVisibility(View.GONE);
    }

    /**
     * 完全没有数据的时候显示
     */
    @Override
    public void showNoDBdata() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.empty_view_nodata, null);
        multipleStatusView.showEmpty(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void showNoLogin() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.nologin_view, null);
        view.findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转登录
                Bundle bundle = new Bundle();
                bundle.putInt(PhoneLoginActivity.GOTYPE, 3);
                UIHelper.openActivityWithBundle(activity, LoginActivity.class, bundle);
            }
        });
        multipleStatusView.showEmpty(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void showData() {
        data_laout.setVisibility(View.VISIBLE);
        fragmentReportTablayout.setVisibility(View.VISIBLE);
        nodata_layout.setVisibility(View.GONE);

    }


    @Override
    public void showNoNetWork() {
        multipleStatusView.showNoNetwork();
    }

    @Override
    public void showLoadding() {
        multipleStatusView.showLoading();
    }

    @Override
    public void showError() {
        multipleStatusView.showNoNetwork();

    }

    @Override
    public void setLineData(ArrayList<LineJson> data) {
        mPresenter.setChartData(chartlayout, ChartLayout.LINE, data);
    }

    @Override
    public void setCircleData(ArrayList<CategoryDataJson> data) {
        chartlayout.addDataSet(data);
    }

    private void initCharPop() {
        chartlayout.setOnChartPopListener(new ChartLayout.OnChartPopListener() {


            @Override
            public void showPop(float x, float y, LineJson chartData) {
                showPopForWindows(x, y, chartData.getPopData());
            }

            @Override
            public void dismiss() {
                pop.dismiss();
            }

        });
    }


    ChartPop pop;

    private void showPopForWindows(float x, float y, ArrayList<PopCharData> list) {
        if (pop == null) {
            pop = new ChartPop(getActivity());
        }
        pop.setType(mPresenter.getType());
//        View childAt = view_layout.getChildAt(0);
//        int height = childAt.getLayoutParams().height + fragmentReportTablayout.getLayoutParams().height;
        int height = DisplayUtil.dip2px(165);

        pop.setParamsForLayout(height, x, y);
        if (pop.setData(list))
            pop.showAtLocation(findViewById(R.id.fragment_reportsingle), Gravity.NO_GRAVITY, 0, 0);
    }

    @Override
    public void showErrorMsg(String msg) {
//        ToastUtil.show(getActivity(), msg);
    }

    @Override
    public void getRadioData(int statisc_type) {
        int c = DrawableUtil.getSkinColor(getActivity());
        activityBaobiaoRabtnWeek.setTextColor(Color.WHITE);
        activityBaobiaoRabtnMonth.setTextColor(Color.WHITE);
        activityBaobiaoRabtnYear.setTextColor(Color.WHITE);
        switch (statisc_type) {
            case 1:
                activityBaobiaoRabtnWeek.setTextColor(c);
                break;
            case 2:
                activityBaobiaoRabtnMonth.setTextColor(c);
                break;
            case 3:
                activityBaobiaoRabtnYear.setTextColor(c);
                break;

        }
        boolean idNeed = mPresenter.jugeNeedGetData(mType, statisc_type);
        if (idNeed) {
            showProgress();
            mPresenter.changeData(mType, statisc_type, category_id);
        }
    }

    CategoryDaoOperator categoryDaoOperator;

    @Override
    public void getDataFirst() {
        showContentLayout();
        mPresenter.changeData(mType, mPresenter.getType(), category_id);
    }

    @Override
    public void setCharData(ChartData charData) {
        changeChart(chartType, charData);
    }

    @Override
    public void setChartNewData(NewChartData chartNewData, NewCategoryJson newCategoryJson) {
        changeNewChart(chartType, chartNewData, newCategoryJson);
    }

    @Override
    public void showContentLayout() {
        multipleStatusView.showContent();
    }

    /**
     * 改变视图
     *
     * @param type
     */
    private void changeChart(int type, ChartData data) {
        switch (type) {
            case ChartLayout.LINE:
                chartlayout.showLineLayout();
                mPresenter.setLineData(data);
                break;
            case ChartLayout.YUANHUAN:
                chartlayout.showYuanhuanLayout();
                mPresenter.setCircleData(data);
                break;
        }
    }

    /**
     * 改变视图
     *
     * @param type
     */
    private void changeNewChart(int type, NewChartData data, NewCategoryJson newCategoryJson) {
        switch (type) {
            case ChartLayout.LINE:
                chartlayout.showLineLayout();
                mPresenter.setLineData(data, newCategoryJson);
                break;
            case ChartLayout.YUANHUAN:
                chartlayout.showYuanhuanLayout();
                mPresenter.setCircleData(data, newCategoryJson);
                break;
        }
    }

    @Override
    public void refreshData(ArrayList<ChartData> chartDatas) {
//获取到数据
        //维度标识1代表周，2代表月，3代表年
        int type = mPresenter.getType();
        mPresenter.refreshTabLayout(fragmentReportTablayout, type, title_text, chartDatas);
    }

    @Override
    public void refreshNewData(ArrayList<NewChartData> chartDatas) {
        int type = mPresenter.getType();
        mPresenter.refreshNewTabLayout(getActivity(), fragmentReportTablayout, type, title_text, chartDatas);
    }

    @Override
    public void goToEdit(TypeDataJson typeDataJson) {
        Intent intent = new Intent(getActivity(), EditBillActivity.class);
        intent.putExtra(EditBillActivity.DATA, typeDataJson);
        startActivity(intent);
    }

    @Override
    public void goToCatrogyDetial(int catrogy_id) {
        Intent itnent = new Intent(getActivity(), CatrogyDetialActivity.class);
        itnent.putExtra(CatrogyDetialActivity.CATROGY_ID, catrogy_id);
        itnent.putExtra(CatrogyDetialActivity.TITLE, mPresenter.getTitle_text());
        itnent.putExtra(CatrogyDetialActivity.MONEY_TYPE, mType);
        itnent.putExtra(CatrogyDetialActivity.RADIO_TYPE, mPresenter.getType());
        startActivity(itnent);
    }


    /**
     * 是不是详情查看页
     */
    private boolean isCatorgType = false;
    /**
     * 详情页直接给数据
     */
    private ArrayList<ChartData> mData;

    /**
     * 登录之后刷新数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginEvent event) {
        checkNet();
    }

    /**
     * 登出后刷新
     *
     * @param loginOutEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginOutEvent loginOutEvent) {
        checkNet();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshEvent event) {
        checkNet();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
