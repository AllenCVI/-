package com.kuxuan.moneynote.utils;

import android.graphics.Color;
import android.widget.TextView;

/**
 * Created by xieshengqi on 2017/10/23.
 */

public class CountDownTimerUtil extends android.os.CountDownTimer {
    private TextView textView;
    private long millisInFuture;
    private long countDownInterval;


    public CountDownTimerUtil(long millisInFuture, long countDownInterval, TextView textView) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
    }

    @Override
    public void onTick(long l) {
        textView.setClickable(false);
        textView.setText(l / 1000 + "s重新获取");
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public long getMillisInFuture() {
        return millisInFuture;
    }

    public void setMillisInFuture(long millisInFuture) {
        this.millisInFuture = millisInFuture;
    }

    public long getCountDownInterval() {
        return countDownInterval;
    }

    public void setCountDownInterval(long countDownInterval) {
        this.countDownInterval = countDownInterval;
    }

    @Override
    public void onFinish() {
        textView.setText("重新获取");
        textView.setTextColor(Color.BLACK);
        textView.setClickable(true);
    }
}
