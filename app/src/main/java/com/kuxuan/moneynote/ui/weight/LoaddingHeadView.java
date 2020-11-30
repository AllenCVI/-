package com.kuxuan.moneynote.ui.weight;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public class LoaddingHeadView extends FrameLayout implements IHeaderView {


    private String pullDowntext ="下拉查看下月数据";
    private String pullDownreleasetext ="松开可查看下月数据";
    private TextView textView;
    private ProgressBar progressBar;
    private Context context;

    public LoaddingHeadView(Context context) {
        super(context);

        initView(context);
    }

    public LoaddingHeadView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoaddingHeadView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
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
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) {
            textView.setText(pullDowntext);
        }
        if (fraction > 1f) {
            textView.setText(pullDownreleasetext);
        }
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) {
            textView.setText(pullDowntext);
            if (textView.getVisibility() == GONE) {
                textView.setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
            }
        }
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        textView.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onFinish(OnAnimEndListener animEndListener) {
        animEndListener.onAnimEnd();
    }

    @Override
    public void reset() {
        textView.setVisibility(VISIBLE);
        progressBar.setVisibility(View.GONE);
        textView.setText(pullDowntext);
    }
}
