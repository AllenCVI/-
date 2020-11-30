package com.kuxuan.moneynote.ui.fragments.report;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.json.ChartData;
import com.kuxuan.moneynote.listener.MVPListener;
import com.kuxuan.moneynote.ui.adapter.ViewpagerChartAdapter;
import com.kuxuan.moneynote.ui.weight.ChartLayout;
import com.kuxuan.moneynote.ui.weight.TabLayoutOpertor;

import java.util.ArrayList;
import java.util.WeakHashMap;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public class ReportPresent extends ReportContract.RepPresent {
    /**
     * 1代表周，2代表月，3代表年
     */
    private int type = 1;

    /**
     * 图表还是圆环
     */
    private int charType = ChartLayout.LINE;

    public int getType() {
        return type;
    }


    public int getCharType() {
        return charType;
    }

    public void setCharType(int charType) {
        this.charType = charType;
    }

    private FragmentActivity mActivity;


    private WeakHashMap<Integer, ArrayList<ChartData>> mapLists;


    public void init(Context context, int charType) {
        mActivity = (FragmentActivity) context;
        this.charType = charType;
    }

    @Override
    void initRadioGroup(RadioGroup radioGroup) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.activity_baobiao_rabtn_week:
                        type = 1;
//                        view.showTypeText(checkedId, TimeUtlis.getCreateTime(System.currentTimeMillis()));
                        break;
                    case R.id.activity_baobiao_rabtn_month:
//选择月
                        type = 2;
//                        view.showTypeText(checkedId, "一月");
                        break;
                    case R.id.activity_baobiao_rabtn_year:
//选择周
                        type = 3;
//                        view.showWeekLayout();
                        break;
                }
                view.getRadioData(type);
            }
        });
    }

    private TabLayoutOpertor opertor;

    @Override
    void initTabLayout(Context context, TabLayout tabLayout) {
//        opertor = new TabLayoutOpertor(context, tabLayout, new TabLayoutOpertor.OnTabLayoutSelectListener() {
//            @Override
//            public void onTabSelect(TabLayout.Tab tab, int position) {
////选中状态(访问数据)
//                View customView = tab.getCustomView();
//                TextView textView = (TextView) customView.findViewById(R.id.item_schedule_text);
////                view.showMonthData(position, textView.getText().toString());
//            }
//        });
    }

    ViewpagerChartAdapter chartAdapter;


    @Override
    void initViewPagerTablyout(TabLayout tabLayout, ViewPager viewPager, int type) {
        FragmentManager supportFragmentManager = mActivity.getSupportFragmentManager();
//        chartAdapter = new ViewpagerChartAdapter(supportFragmentManager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        viewPager.setAdapter(chartAdapter);
//        tabLayout.setupWithViewPager(viewPager, true);
//        tabLayout.setTabsFromPagerAdapter(chartAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (chartAdapter != null)
                    chartAdapter.setCurrentPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initChartAdapter(ArrayList<ChartData> chartDatas) {
//        if (chartAdapter == null) {
//            FragmentManager supportFragmentManager = mActivity.getSupportFragmentManager();
//            chartAdapter = new ViewpagerChartAdapter(supportFragmentManager, type, chartDatas);
//
//        } else {
//            chartAdapter.setDataLists(chartDatas);
//        }
        view.refreshData(chartDatas);
    }

    @Override
    void setTabLayoutData(ArrayList<String> data) {
        if (opertor != null) {
            opertor.setDataLists(data);
        }
    }

    @Override
    void changeChart(int type) {
        chartAdapter.setType(type);
    }

    @Override
    void changeData(int type, final int statis_type) {
        mModel.getDataLists(type, statis_type, new MVPListener<ArrayList<ChartData>>() {
            @Override
            public void onSuccess(ArrayList<ChartData> content) {
                view.hideProgress();
                if (content != null) {
                    for (ChartData chartData : content) {
                        chartData.setStatistic_type(statis_type);
                    }
                }
                initChartAdapter(content);
            }

            @Override
            public void onFail(String msg) {
                view.hideProgress();
                view.showErrorMsg(msg);

            }
        });
    }


    @Override
    boolean jugeNeedGetData(int type, int statis_type) {
        boolean isNeedRefresh = false;
        if (mapLists == null) {
            mapLists = new WeakHashMap<>();
        }
        try {
            ArrayList<ChartData> chartDatas = mapLists.get(type);
            if (chartDatas != null) {
                //刷新adapter和tablayout
                isNeedRefresh = false;
                initChartAdapter(chartDatas);
            } else {
                isNeedRefresh = true;
            }
        } catch (Exception e) {
            //请求网络刷新layout
            isNeedRefresh = true;
        }
        return isNeedRefresh;
    }

    @Override
    void refreshTabLayout(TabLayout tabLayout, ViewPager viewPager, ArrayList<ChartData> datas) {
        FragmentManager supportFragmentManager = mActivity.getSupportFragmentManager();
        if (chartAdapter == null) {
            chartAdapter = new ViewpagerChartAdapter(supportFragmentManager, mActivity, charType,type, datas);
            viewPager.setAdapter(chartAdapter);
        } else {
            chartAdapter = new ViewpagerChartAdapter(supportFragmentManager, mActivity, charType,type, datas);
            viewPager.setAdapter(chartAdapter);
        }
        tabLayout.setupWithViewPager(viewPager, true);
//        tabLayout.setTabsFromPagerAdapter(chartAdapter);
    }
    @Override
    void clearMap() {
        if (mapLists != null)
            mapLists.clear();
    }
}
