package com.kuxuan.moneynote.ui.activitys.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.NetWorkUtil;
import com.kuxuan.moneynote.utils.SPUtil;
import com.kuxuan.moneynote.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 预算设置
 * Created by xieshengqi on 2018/4/24.
 */

public class BudgetActivity extends BaseActivity {
    @Bind(R.id.activity_buget_switch_checkbox)
    ImageView imageView;
    @Bind(R.id.activity_buget_num_text)
    TextView activityBugetNumText;
    @Bind(R.id.activity_buget_gotosetting_layout)
    RelativeLayout activityBugetGotosettingLayout;
    @Bind(R.id.activity_buget_explain_text)
    TextView activityBugetExplainText;

    @Override
    public int getLayout() {
        return R.layout.activity_buget_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        EventBus.getDefault().register(this);
        getTitleView(1).setTitle("预算设置");
        getTitleView(1).setLeft_text("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetWorkUtil.isNetworkAvailable(BudgetActivity.this)) {
                    ToastUtil.show(BudgetActivity.this, getResources().getString(R.string.nonetwork));
                    return;
                }
                boolean isOpne = (boolean) SPUtil.get(BudgetActivity.this, Constant.System.SWITCH, false);
                if (isOpne) {
                    setData("0", isOpne);
                } else {
                    long d = (Long) SPUtil.get(BudgetActivity.this, Constant.System.BUGET_NUM, Constant.System.NORMAL_NUM);
                    setData(d + "", isOpne);
                }

            }
        });
        activityBugetGotosettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BudgetActivity.this, BudgetSettingActivity.class);
                startActivity(intent);
            }
        });
        changeLayout();
        setData();
    }


    private void changeLayout() {
        boolean isOpen = (boolean) SPUtil.get(this, Constant.System.SWITCH, false);
        if (isOpen) {
            imageView.setImageResource(R.mipmap.icon_switch_open);
            activityBugetGotosettingLayout.setVisibility(View.VISIBLE);
            activityBugetExplainText.setVisibility(View.VISIBLE);
        } else {
            imageView.setImageResource(R.mipmap.icon_switch_close);
            activityBugetGotosettingLayout.setVisibility(View.GONE);
            activityBugetExplainText.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setData() {
        activityBugetNumText.setText((Long) SPUtil.get(this, Constant.System.BUGET_NUM, Constant.System.NORMAL_NUM) + "");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent2(BugetEvent loginOutEvent) {
        setData();
    }


    private void setData(final String data, final boolean isOpen) {
        RetrofitClient.getApiService().setBudgetData(data).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
            }

            @Override
            public void onSuccess(BaseJson<Object> objectBaseJson) {
                if (objectBaseJson != null) {
                    if (objectBaseJson.getCode() == 0) {
                        long i = Long.parseLong(data);
                        if (i != 0){

                            SPUtil.putAndApply(MyApplication.getInstance(), Constant.System.BUGET_NUM, i);
                        }
                        MineJson userInfo = LoginStatusUtil.getUserInfo();
                        if (userInfo != null) {
                            userInfo.setMonth_budget(data);
                            LoginStatusUtil.setUserInfo(userInfo);
                        }
                        if (isOpen) {
                            SPUtil.putAndApply(BudgetActivity.this, Constant.System.SWITCH, false);
                        } else {
                            SPUtil.putAndApply(BudgetActivity.this, Constant.System.SWITCH, true);
                        }
                        changeLayout();
                        EventBus.getDefault().post(new BugetEvent());
//                        finish();
                    } else {
                    }
                } else {

                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent2(LoginOutEvent loginOutEvent) {
        finish();
    }


}
