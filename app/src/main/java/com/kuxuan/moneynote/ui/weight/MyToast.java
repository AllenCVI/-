package com.kuxuan.moneynote.ui.weight;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kuxuan.moneynote.R;

/**
 * Created by xieshengqi on 2017/11/3.
 */

public class MyToast {
    private Toast mToast;

    private MyToast(Context context, CharSequence text, int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.eplay_toast, null);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(v);
        setGravity(Gravity.CENTER, 0, 0);
    }

    private MyToast(Context context, int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.eplay_toast, null);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(v);
        setGravity(Gravity.CENTER, 0, 0);
    }

    private MyToast(Context context, String text, int duration, int gravity) {
        View v = LayoutInflater.from(context).inflate(R.layout.eplay_toast_small, null);
        TextView textView = v.findViewById(R.id.eplay_toast_text);
        textView.setText(text);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(v);
        setGravity(gravity, 0, 100);
    }

    public static MyToast makeText(Context context, CharSequence text, int duration) {
        return new MyToast(context, text, duration);
    }

    public static MyToast makeTextGravity(Context context, CharSequence text, int duration, int gravity) {
        return new MyToast(context, text.toString(), duration, gravity);
    }

    public static MyToast makeText(Context context, int duration) {
        return new MyToast(context, duration);
    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }
}