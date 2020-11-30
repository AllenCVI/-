package com.kuxuan.moneynote.ui.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.kuxuan.moneynote.R;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
@SuppressLint("AppCompatCustomView")
public class MoneyTextView extends TextView {
    private static final int DEFAULT_TEXT_SIZE = 10; //sp;
    private int mFirstSize = sp2px(DEFAULT_TEXT_SIZE);
    private int mSecondSize = sp2px(DEFAULT_TEXT_SIZE);
    private String text = "";

    public MoneyTextView(Context context) {
        this(context,null);
    }

    public MoneyTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public MoneyTextView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(attrs);

    }

    /**
     * 获取自定义属性
     * @param attrs
     */
    public void obtainStyledAttrs( AttributeSet attrs){
        TypedArray ta = getContext().obtainStyledAttributes(attrs,
                R.styleable.MoneyTextView);
        mFirstSize = (int) ta.getDimension(R.styleable.MoneyTextView_firstTextSize,mFirstSize);
        mSecondSize = (int) ta.getDimension(R.styleable.MoneyTextView_secondTextSize,mSecondSize);
        Log.e("aaaa",mFirstSize+"");
        Log.e("aaa",mSecondSize+"");

        ta.recycle();


    }

    private int sp2px(int spVal){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal,getResources().getDisplayMetrics());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!text.equals(""))
        {
            return;
        }
        text= getText().toString();

        String a[] = text.split("\\.");
        if(a.length<2){
            setText(text);
            return;
        }
        int firstLength = a[0].length();
        int secondLength = a[1].length();
        SpannableString spannableString = new SpannableString(text);
        RelativeSizeSpan sizeSpanBig = new RelativeSizeSpan(0.6f);
        RelativeSizeSpan sizeSpanSmall = new RelativeSizeSpan(1.4f);
        spannableString.setSpan(sizeSpanSmall, 0, firstLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(sizeSpanBig, firstLength+1, firstLength+1+secondLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        setText(spannableString);

        Log.e("aaaa","我一直在执行");
    }


}
