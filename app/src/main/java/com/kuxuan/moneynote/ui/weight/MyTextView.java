package com.kuxuan.moneynote.ui.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 注意：spannableString 设置Spannable 的对象到spannableString中时，要用Spannable.SPAN_EXCLUSIVE_EXCLUSIVE的flag值，不然可能会会出现后面的衔接字符串不会显示
     */
    @Override
    protected void onDraw(Canvas canvas) {
        CharSequence charSequence = getText() ;
        int lastCharDown = getLayout().getLineVisibleEnd(0) ;
        if (charSequence.length() > lastCharDown){
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder() ;
            spannableStringBuilder.append(charSequence.subSequence(0,lastCharDown-2)).append("...") ;
            setText(spannableStringBuilder);
        }
        super.onDraw(canvas);
    }
}
