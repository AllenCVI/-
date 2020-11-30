package com.kuxuan.moneynote.ui.activitys.alarm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.mvpbase.MVPFragmentActivity;
import com.kuxuan.moneynote.json.Time;
import com.kuxuan.moneynote.utils.PickerUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;
import cn.addapp.pickers.picker.TimePicker;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class AlarmActivity extends MVPFragmentActivity<AlarmPresenter, AlarmModel> implements AlarmContract.AlarmView {
    private static final int INTERVAL = 1000 * 60 * 60 * 24;// 24h
    @Bind(R.id.recycler)
    RecyclerView mRecyclerView;

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

    public static void show(Context context) {
        Intent intent = new Intent(context, AlarmActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        //设置标题
        getTitleView(1).setTitle(getResources().getString(R.string.me_alarm)).
                setTitleColor(this, R.color.white).
                setLeftImage(R.drawable.toolbar_navigation_icon_normal, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        mPresenter.initRecyclerView(this, mRecyclerView);

    }

    @Override
    public int getLayout() {
        return R.layout.activity_alarm;
    }

    /**
     * 添加提醒的点击事件
     */
    @OnClick(R.id.addnotice)
    void noticeClick() {
        PickerUtil.onTimePicker(this, new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String s, String s1) {
                mPresenter.addData(new Time(s + ":" + s1));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.save();
    }

}
