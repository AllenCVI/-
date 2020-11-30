package com.kuxuan.moneynote.ui.activitys;

import android.os.Bundle;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseFragmentActivity;
import com.kuxuan.moneynote.ui.fragments.reportsingle.ReportSingleFragment;
import com.kuxuan.moneynote.ui.weight.ChartLayout;
import com.umeng.analytics.MobclickAgent;

/**
 * 图表类别详情
 * Created by xieshengqi on 2017/10/31.
 */

public class CatrogyDetialActivity extends BaseFragmentActivity {

    public static final String CATROGY_ID = "catrogy_id";
    public static final String CHARTTYPE = "charttype";
    public static final String MONEY_TYPE = "money_type";
    public static final String TITLE = "title";
    public static final String RADIO_TYPE = "radio_type";

    @Override
    public int getLayout() {
        return R.layout.fragment_reportchart;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReportSingleFragment reportFragment = ReportSingleFragment.getInstanceForPage(getIntent().getIntExtra(CATROGY_ID, -1), getIntent().getIntExtra(CHARTTYPE, ChartLayout.LINE),getIntent().getIntExtra(MONEY_TYPE,2),getIntent().getStringExtra(TITLE),getIntent().getIntExtra(RADIO_TYPE,1));
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_reportchart_content, reportFragment).commit();
        getSupportFragmentManager().beginTransaction().show(reportFragment).commit();
    }
}
