package com.kuxuan.moneynote.ui.fragments.reportdetial;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.mvpbase.MVPFragment;
import com.kuxuan.moneynote.json.CategoryDataJson;
import com.kuxuan.moneynote.json.ChartData;
import com.kuxuan.moneynote.json.LineJson;
import com.kuxuan.moneynote.json.PopCharData;
import com.kuxuan.moneynote.ui.weight.ChartLayout;
import com.kuxuan.moneynote.ui.weight.ChartPop;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by xieshengqi on 2017/10/20.
 */

public class ReportDetialFragment extends MVPFragment<ReportDetialPresent, ReportDetialModel> implements ReportDetialContract.RepDeView {


    private int type = ChartLayout.LINE;
    @Bind(R.id.fragment_report_detial_chartlayout)
    ChartLayout chartlayout;
    @Bind(R.id.fragment_report_detial_recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.textView)
    TextView textView;
    /**
     * 图表数据
     */
    private ChartData mData;

    private static final String TYPE = "chart_type";
    private static final String DATA = "chart_data";


    public static ReportDetialFragment getInstance(int type, ChartData chartData) {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        bundle.putSerializable(DATA, chartData);
        ReportDetialFragment reportDetialFragment = new ReportDetialFragment();
        reportDetialFragment.setArguments(bundle);
        return reportDetialFragment;
    }


    private void initCharPop() {
        chartlayout.setOnChartPopListener(new ChartLayout.OnChartPopListener() {
//            @Override
//            public void showPop(float x, float y) {
//                showPopForWindows(x, y);
//            }



            @Override
            public void showPop(float x, float y, LineJson lineJson) {
                showPopForWindows(x,y,lineJson.getPopData());
            }

            @Override
            public void dismiss() {
                pop.dismiss();
            }
        });
    }

    ChartPop pop;

    private void showPopForWindows(float x, float y, ArrayList<PopCharData> datas) {
        if (pop == null) {
            pop = new ChartPop(getActivity());
        }
        pop.setParamsForLayout(10,x, y);
        pop.setData(datas);
        pop.showAtLocation(findViewById(R.id.fragment_report_detial_layout), Gravity.NO_GRAVITY, 0, 0);
    }

    /**
     * 当前属于第几条
     */
    private int position;


    @Override
    public void initView() {
        //视图切换
        type = getArguments().getInt(TYPE, ChartLayout.LINE);
        mData = (ChartData) getArguments().getSerializable(DATA);
        textView.setText(mData.getTime());
        mPresenter.initRecyclerView(getActivity(), mRecyclerView);
        changeChart(type);
        initCharPop();

    }

//<<<<<<< HEAD
//    private void testChart() {
//        ArrayList<LineJson> data = new ArrayList<>();
//        String[] data1 = {"10-16", "10-17", "10-18", "10-19", "今天", "10-12", "10-22",};
//        for (int i = 0; i < 7; i++) {
//            int y = 0;
//            if (i == 2 || i == 6) {
//                y = i * 10;
//            } else {
//                y = 0;
//            }
//
//            y = i * 10;
//            LineJson lineJson = new LineJson(data1[i], y);
//            data.add(lineJson);
//        }
//        mPresenter.setChartData(chartlayout, ChartLayout.LINE, data);
//        chartlayout.addDataSet(new ArrayList<LineJson>());


    public void setData(ChartData chartData) {
        mData = chartData;
        changeChart(type);

    }

//    private void testChart() {
//        ArrayList<LineJson> data = new ArrayList<>();
//        String[] data1 = {"10-16", "10-17", "10-18", "10-19", "今天", "10-12", "10-22",};
//        for (int i = 0; i < 7; i++) {
//            int y = 0;
//            if (i == 2 || i == 6) {
//                y = i * 10;
//            } else
//                y = 0;
//            y = i * 10;
//            LineJson lineJson = new LineJson(data1[i], y);
//            data.add(lineJson);
//        }
//        mPresenter.setChartData(chartlayout, ChartLayout.LINE, data);
//        chartlayout.addDataSet(new ArrayList<LineJson>());
//    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        changeChart(type);
    }

    /**
     * 改变视图
     *
     * @param type
     */
    private void changeChart(int type) {
        switch (type) {
            case ChartLayout.LINE:
                chartlayout.showLineLayout();
                mPresenter.setLineData(mData);
                break;
            case ChartLayout.YUANHUAN:
                chartlayout.showYuanhuanLayout();
                mPresenter.setCircleData(mData);
                break;
        }

    }


    @Override
    public int getLayout() {
        return R.layout.fragment_report_detial;
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }


    @Override
    public void setLineData(ArrayList<LineJson> data) {
        mPresenter.setChartData(chartlayout, ChartLayout.LINE, data);
    }

    @Override
    public void setCircleData(ArrayList<CategoryDataJson> data) {
        chartlayout.addDataSet(data);
    }
}