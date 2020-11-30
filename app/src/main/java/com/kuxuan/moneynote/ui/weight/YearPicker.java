package com.kuxuan.moneynote.ui.weight;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;

import com.kuxuan.moneynote.R;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public  class YearPicker extends Dialog implements View.OnClickListener{

private Activity activity;
private NumberPicker numberPicker;
private YearChangeListener mYearChangeListener;

private String[] city = {"2017年","2018年","2019年","2020年","2021年","2022年"};
public YearPicker(Activity activity) {
        super(activity, R.style.MyDialogTheme);
        this.activity = activity;
        mYearChangeListener =(YearChangeListener)activity;
        }

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picklayout);
        numberPicker = findViewById(R.id.numberPicker);

        numberPicker.setDisplayedValues(city);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(city.length - 1);
        numberPicker.setValue(4);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){

                @Override
                public void onValueChange(NumberPicker picker, int oldVal,
                                          int newVal) {

                }

        });
        setViewLocation();
        setCanceledOnTouchOutside(true);//外部点击取消
        }


/**
 * 设置dialog位于屏幕底部
 */
private void setViewLocation(){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;

        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;
        lp.y = height;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        onWindowAttributesChanged(lp);
        }


        @Override
        public void onClick(View v) {
          switch (v.getId()){
                  case R.id.cancel:
                          this.dismiss();
                          break;
                  case R.id.sure:
                          this.dismiss();
                          mYearChangeListener.yearChange(city[numberPicker.getValue()]);
                          break;
                  default:
                          break;
          }

      }

      public interface YearChangeListener{
              void yearChange(String year);
      }

   }
