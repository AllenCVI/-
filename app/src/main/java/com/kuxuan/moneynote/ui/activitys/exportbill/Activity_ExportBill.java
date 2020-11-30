package com.kuxuan.moneynote.ui.activitys.exportbill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.mvpbase.MVPFragmentActivity;
import com.kuxuan.moneynote.ui.activitys.eventbus.LoginOutEvent;
import com.kuxuan.moneynote.ui.weight.MyLoadingView;
import com.kuxuan.moneynote.ui.weight.MyToast;
import com.kuxuan.moneynote.utils.DrawableUtil;
import com.kuxuan.moneynote.utils.NetWorkUtil;
import com.kuxuan.moneynote.utils.PickerUtil;
import com.kuxuan.moneynote.utils.TimeUtlis;
import com.kuxuan.moneynote.utils.ToastUtil;
import com.kuxuan.moneynote.utils.Validator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;
import cn.addapp.pickers.picker.DatePicker;

/**
 * Created by Allence on 2018/3/15 0015.
 */

public class Activity_ExportBill extends MVPFragmentActivity<Presenter_ExportBill, Model_ExportBill> implements Contract_ExpoetBill.ExportBillView {


    @Bind(R.id.header_back)
    TextView tv_header_back;

    @Bind(R.id.et_Email)
    EditText et_Email;

    @Bind(R.id.tv_export)
    TextView tv_export;

    String daytime;


    @Bind(R.id.tv_starttime)
    TextView tv_starttime;

    @Bind(R.id.tv_endtime)
    TextView tv_endtime;


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void initView() {

        initTitleView();
        initEditText();
        initDate();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    private String addZero(int time) {

        if (time >= 10) {
            return time + "";
        }
        return "0" + time;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initDate() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR); // 获取当前年份
        mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        daytime = mYear + "年" + addZero(mMonth) + "月" + addZero(mDay) + "日";

        startTime = daytime;
        endTime = daytime;
        tv_starttime.setText(startTime);
        tv_endtime.setText(endTime);


    }

    private void initEditText() {

        et_Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() != 0) {
                    tv_export.setBackground(DrawableUtil.getShape(Activity_ExportBill.this));
                } else {
                    tv_export.setBackgroundResource(R.color.bt_gray2);
                }

            }
        });


    }

    private void initTitleView() {

        getTitleView(1).setTitle(getResources().getString(R.string.bill_export)).setLeft_text("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        tv_header_back.setVisibility(View.VISIBLE);

    }


    @Override
    public int getLayout() {
        return R.layout.activity_export_bill;
    }

    public static void show(Context context) {

        Intent intent = new Intent(context, Activity_ExportBill.class);
        context.startActivity(intent);

    }


    int mYear;
    int mMonth;
    int mDay;

    String startTime;
    String endTime;


    @OnClick(R.id.tv_starttime)
    void starttime_Click(final TextView textView) {
        PickerUtil.onYearMonthDayPicker("选择日期", mYear, mMonth, mDay, this, new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String s, String s1, String s2) {
                textView.setText(s + "年" + s1 + "月" + s2 + "日");
                startTime = s + "年" + s1 + "月" + s2 + "日";

            }
        });
    }


    @OnClick(R.id.tv_endtime)
    void endtime_Click(final TextView textView) {
        PickerUtil.onYearMonthDayPicker("选择日期", mYear, mMonth, mDay, this, new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String s, String s1, String s2) {
                textView.setText(s + "年" + s1 + "月" + s2 + "日");
                endTime = s + "年" + s1 + "月" + s2 + "日";
                mYear = Integer.parseInt(s);
                mMonth = Integer.parseInt(s1);
                mDay = Integer.parseInt(s2);
            }
        });
    }


    @OnClick(R.id.tv_export)
    void export() {

        if (!NetWorkUtil.isNetworkAvailable(this)) {
            MyToast.makeText(this, getResources().getString(R.string.nonetwork), Toast.LENGTH_SHORT).show();
            return;
        }
        String email = et_Email.getText().toString();

        String startTime1 = startTime;

        String endTime1 = endTime;


        if (email == null || email.equals("")) {
            return;
        }

        boolean isEmail = Validator.isEmail(email);

        if (!isEmail) {

            MyLoadingView.showEmailErr(this);

        } else {
            MyLoadingView.showLoadingView(this);

            String time = gettime(System.currentTimeMillis() + "");

            startTime1 = gettime(TimeUtlis.dateToStamp(startTime1));

            endTime1 = gettime(TimeUtlis.dateToStamp(endTime1));

            if (startTime1 == null || endTime1 == null || time == null || email == null) {
                return;
            }
            mPresenter.exportBill(email, startTime1, endTime1, time);

        }

    }


    private String gettime(String time) {

        String timestamp = String.valueOf(Long.parseLong(time) / 1000);
        return timestamp;

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent2(LoginOutEvent loginOutEvent) {
        finish();
    }

    @Override
    public void setSuccessExportBillView() {


        Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                MyLoadingView.dialogDismiss();
                MyLoadingView.showSuccessView(Activity_ExportBill.this);

            }
        };

        handler.sendEmptyMessageDelayed(0, 1500);


    }

    @Override
    public void setFaildExportBillView() {
        ToastUtil.show(this,"导出失败");
        MyLoadingView.dialogDismiss();
//        MyLoadingView.showEmailErr(this);

    }
}
