package com.kuxuan.moneynote.ui.activitys.budget;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kuxuan.moneynote.MyApplication;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.base.BaseActivity;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.MineJson;
import com.kuxuan.moneynote.ui.activitys.eventbus.BugetEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.LoginOutEvent;
import com.kuxuan.moneynote.utils.DrawableUtil;
import com.kuxuan.moneynote.utils.JavaFormatUtils;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.NetWorkUtil;
import com.kuxuan.moneynote.utils.SPUtil;
import com.kuxuan.moneynote.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xieshengqi on 2018/4/24.
 */

public class BudgetSettingActivity extends BaseActivity {
    @Bind(R.id.activity_bugetsetting_numedit)
    EditText numedit;
    @Bind(R.id.activity_bugetsetting_complete_btn)
    Button completeBtn;

    @Override
    public int getLayout() {
        return R.layout.activity_bugetsetting_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getTitleView(1).setTitle("预算设置");
        getTitleView(1).setLeft_text("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        long num = (Long) SPUtil.get(this, Constant.System.BUGET_NUM, Constant.System.NORMAL_NUM);
        numedit.setHint(JavaFormatUtils.formatFloatNumber(num));
        initEdit();

    }

    private void initEdit() {
        numedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0 && numedit.getText().length() != 0) {
                    completeBtn.setBackground(DrawableUtil.getShape(BudgetSettingActivity.this));
                    completeBtn.setTextColor(getResources().getColor(R.color.white));
                    completeBtn.setEnabled(true);
                } else {
                    completeBtn.setBackground(getResources().getDrawable(R.drawable.bg_gray_btn));
                    completeBtn.setTextColor(getResources().getColor(R.color.white));
                    completeBtn.setEnabled(false);
                }
            }
        });
    }

    @OnClick(R.id.activity_bugetsetting_complete_btn)
    public void onViewClicked() {
        String string = numedit.getText().toString();
        long da = Long.parseLong(string);
        if (da == 0) {
            ToastUtil.show(this, "预算金额必须大于0");
            return;
        }
        if (!NetWorkUtil.isNetworkAvailable(this)) {
            ToastUtil.show(this, getResources().getString(R.string.nonetwork));
            return;
        }
        setData(string);
    }


    private void setData(final String data) {
        RetrofitClient.getApiService().setBudgetData(data).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                ToastUtil.show(BudgetSettingActivity.this, "设置失败");
            }

            @Override
            public void onSuccess(BaseJson<Object> objectBaseJson) {
                if (objectBaseJson != null) {
                    if (objectBaseJson.getCode() == 0) {
                        long i = Long.parseLong(data);
                        MineJson userInfo = LoginStatusUtil.getUserInfo();
                        if (userInfo != null) {
                            userInfo.setMonth_budget(data);
                            LoginStatusUtil.setUserInfo(userInfo);
                        }
                        if (i != 0)
                            SPUtil.putAndApply(MyApplication.getInstance(), Constant.System.BUGET_NUM, i);
                        EventBus.getDefault().post(new BugetEvent());
                        finish();
                    } else {
                        ToastUtil.show(BudgetSettingActivity.this, objectBaseJson.getMessage().get(0));
                    }
                } else {
                    ToastUtil.show(BudgetSettingActivity.this, "设置失败");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent2(LoginOutEvent loginOutEvent) {
        finish();
    }
}
