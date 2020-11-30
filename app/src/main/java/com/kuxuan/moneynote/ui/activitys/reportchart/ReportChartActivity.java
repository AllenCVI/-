package com.kuxuan.moneynote.ui.activitys.reportchart;

import android.os.Bundle;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseFragmentActivity;
import com.kuxuan.moneynote.ui.fragments.reportsingle.ReportSingleFragment;
import com.kuxuan.moneynote.ui.weight.ChartLayout;
import com.umeng.analytics.MobclickAgent;

/**
 * 圆环页
 * Created by xieshengqi on 2017/10/20.
 */

public class ReportChartActivity extends BaseFragmentActivity {

    public static final String CATROGY_ID = "catrogy_id";
    public static final String CHARTTYPE = "charttype";
    public static final String MONEY_TYPE = "money_type";
    public static final String TITLE = "title";
    public static final String RADIO_TYPE = "radio_type";

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_reportchart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getIntent().getIntExtra(CATROGY_ID, -1);
        int type = getIntent().getIntExtra(CHARTTYPE, ChartLayout.LINE);
        int money_type = getIntent().getIntExtra(MONEY_TYPE, 2);
        int radio_type = getIntent().getIntExtra(RADIO_TYPE, 1);
        String title = getIntent().getStringExtra(TITLE);
        ReportSingleFragment reportFragment;
        reportFragment = ReportSingleFragment.getInstanceForPage(id, type,money_type,title,radio_type);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_reportchart_content, reportFragment).commit();
        getSupportFragmentManager().beginTransaction().show(reportFragment).commit();
    }
}
