package com.kuxuan.moneynote.ui.activitys.score;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的积分
 * Created by xieshengqi on 2018/4/23.
 */

public class UserScoreActivity extends BaseActivity {
    @Bind(R.id.score_text)
    TextView scoreText;
    @Bind(R.id.score_recyclerview)
    RecyclerView scoreRecyclerview;
    @Bind(R.id.score_swiperefreshlayout)
    SwipeRefreshLayout scoreSwiperefreshlayout;

    @Override
    public int getLayout() {
        return R.layout.activity_userscore_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleView(1).getTitle_text().setTextColor(Color.WHITE);
        getTitleView(1).getTitle_text().setText("我的积分");
        getTitleView(1).setLeftImage(R.drawable.toolbar_navigation_icon_normal, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick({R.id.score_exchange_text, R.id.score_detial_text, R.id.score_rule_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.score_exchange_text:
                // TODO: 2018/4/23 积分兑换
                break;
            case R.id.score_detial_text:
                // TODO: 2018/4/23 积分明细
                break;
            case R.id.score_rule_text:
                // TODO: 2018/4/23 积分规则
                break;
        }
    }
}
