package com.kuxuan.moneynote.ui.weight;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.lcodecore.tkrefreshlayout.IBottomView;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public class LoadBottomView extends FrameLayout implements IBottomView {


    private TextView textView;
    private ProgressBar progressBar;
    private Context context;

    private String pullUptext ="上拉查看上月数据";
    private String pullUpreleasetext ="松开可查看上月数据";

    public LoadBottomView(Context context) {
        super(context);

        initView(context);
    }

    public LoadBottomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadBottomView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        View rootView = LayoutInflater.from(context).inflate(R.layout.loadding_view, null);
        textView = rootView.findViewById(R.id.loadding_text);
        progressBar = rootView.findViewById(R.id.loadding_progress);
        addView(rootView);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingUp(float fraction, float maxBottomHeight, float bottomHeight) {

        if (fraction < 1f) {
            textView.setText(pullUptext);
        }
        if (fraction > -1f) {
            textView.setText(pullUpreleasetext);
        }

        Log.e("上拉",fraction+" "+maxBottomHeight+" "+bottomHeight);
        if (fraction < 1f) {
            textView.setText(pullUptext);
        }
        if (fraction > -1f) {
            textView.setText(pullUpreleasetext);
        }

    }


    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < -1f) {
            textView.setText(pullUptext);
            if (textView.getVisibility() == GONE) {
                textView.setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
            }
        }
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        textView.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
    }


    @Override
    public void reset() {
        textView.setVisibility(VISIBLE);
        progressBar.setVisibility(View.GONE);
        textView.setText(pullUptext);
    }
}
