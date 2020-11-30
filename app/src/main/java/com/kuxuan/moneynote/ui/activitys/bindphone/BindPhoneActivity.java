package com.kuxuan.moneynote.ui.activitys.bindphone;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseActivity;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.utils.AppManager;
import com.kuxuan.moneynote.utils.CountDownTimerUtil;
import com.kuxuan.moneynote.utils.SPUtil;
import com.kuxuan.moneynote.utils.UIHelper;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;

import static java.lang.System.currentTimeMillis;

/**
 * 绑定第二步
 * @author hfrx
 */

public class BindPhoneActivity extends BaseActivity {
    @Bind(R.id.activity_register_second_phone_text)
    TextView phone_text;
    @Bind(R.id.activity_register_second_code_edit)
    EditText code_edit;
    @Bind(R.id.activity_register_second_code_delete_img)
    ImageView delete_img;
    @Bind(R.id.activity_register_second_code_send_text)
    TextView sendCode_text;
    @Bind(R.id.activity_register_second_code_tonext_btn)
    Button toNext_btn;
    boolean isFirstWechatLogin;
    private static final String TAG = "BindPhoneActivity";
    public static final String PHONE = "phone";
    private String code_type = "1";

    private static final int TIME_SEND = 60000;//毫秒
    private static final int TIME_SEND_INTERNAL = 1000;//倒计时间隔
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_register_second;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);

        isFirstWechatLogin = getIntent().getExtras().getBoolean(Constant.IsFirstWEiChatLogin.ISFIRSTWEICHATLOGIN,false);

        getTitleView(1).setTitle(getResources().getString(R.string.bind_phone));

        getTitleView(1).setLeft_text("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        phone_text.setText(getIntent().getExtras().getString(PHONE));
        initEdit();
        initSendText();
        Long time = (Long) SPUtil.get(this, Constant.Time.SEND_TIME, 0L);
        long currtime = currentTimeMillis();
        Log.e("time",currtime+" : "+time);
        if (currtime - time < TIME_SEND) {
            //不能发送
            countDownTimerUtil = new CountDownTimerUtil(TIME_SEND-(currtime - time), TIME_SEND_INTERNAL, sendCode_text);
            countDownTimerUtil.start();
        } else {
            sendCode();
        }
    }

    CountDownTimerUtil countDownTimerUtil;

    private void initSendText() {
        countDownTimerUtil = new CountDownTimerUtil(TIME_SEND, TIME_SEND_INTERNAL, sendCode_text);
    }

    private void jugeCountDownTime() {
        if (countDownTimerUtil.getMillisInFuture() != TIME_SEND) {
            initSendText();
        }
    }

    /**
     * 发送验证码
     */
    private void sendCode() {
        long time = System.currentTimeMillis();
        String a = String.valueOf(time);
        showProgressDialog(getResources().getString(R.string.code_sendding));
        Log.e(TAG,Constant.MD5+phone_text.getText().toString()+a);
//        RetrofitClient.getApiService().sendCode(phone_text.getText().toString().replaceAll("-", ""), code_type,a,Md5Util.md5(Constant.MD5+phone_text.getText().toString().replaceAll("-", "")+a),"0").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
//            @Override
//            public void onError(ExceptionHandle.ResponeThrowable e) {
//                closeProgressDialog();
//                ToastUtil.show(BindPhoneActivity.this, e.message);
//            }
//
//            @Override
//            public void onSuccess(BaseJson<Object> objectBaseJson) {
//                closeProgressDialog();
//                if (objectBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
//                    ToastUtil.show(BindPhoneActivity.this, objectBaseJson.getSuccess().get(0));
//                    jugeCountDownTime();
//                    countDownTimerUtil.start();
//                    long l = currentTimeMillis();
//                    Log.e("存储的时间",l+"");
//                    SPUtil.putAndApply(BindPhoneActivity.this,Constant.Time.SEND_TIME,l);
//                } else {
//                    ToastUtil.show(BindPhoneActivity.this, objectBaseJson.getError().get(0));
//                }
//            }
//        });
    }

    private void initEdit() {
        code_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temp = s.toString();
                if (temp.length() != 0) {
                    delete_img.setVisibility(View.VISIBLE);
                } else {
                    delete_img.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 5) {
                    toNext_btn.setBackground(getResources().getDrawable(R.drawable.bg_orange_btn));
                    toNext_btn.setTextColor(getResources().getColor(R.color.black));
                    toNext_btn.setEnabled(true);
                } else {
                    toNext_btn.setBackground(getResources().getDrawable(R.drawable.bg_gray_btn));
                    toNext_btn.setTextColor(getResources().getColor(R.color.gray_text));
                    toNext_btn.setEnabled(false);
                }
            }

        });
    }

    @OnClick({R.id.activity_register_second_code_delete_img, R.id.activity_register_second_code_send_text, R.id.activity_register_second_code_tonext_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_register_second_code_delete_img:
                code_edit.setText("");
                break;
            case R.id.activity_register_second_code_send_text:
                //// TODO: 2017/10/23 发送验证码
                sendCode();
                break;
            case R.id.activity_register_second_code_tonext_btn:
                //进行下一步(先校验在跳转)
                validateCode();
                break;
        }
    }

    private void goToNext() {
        Bundle bundle = new Bundle();
        bundle.putString(BindPhoneActivity.PHONE, phone_text.getText().toString());
        bundle.putBoolean(Constant.IsFirstWEiChatLogin.ISFIRSTWEICHATLOGIN,isFirstWechatLogin);
        UIHelper.openActivityWithBundle(this, BindThirdActivity.class, bundle);
    }

    /**
     * 校验验证码
     */
    private void validateCode() {
        showProgressDialog(getResources().getString(R.string.code_validate));
//        RetrofitClient.getApiService().validateCode(phone_text.getText().toString().replaceAll("-", ""), code_edit.getText().toString(),"0").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
//            @Override
//            public void onError(ExceptionHandle.ResponeThrowable e) {
//                closeProgressDialog();
//                ToastUtil.show(BindPhoneActivity.this, e.message);
//            }
//
//            @Override
//            public void onSuccess(BaseJson<Object> objectBaseJson) {
//                closeProgressDialog();
//                if (objectBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
//                    goToNext();
//                } else {
//                    ToastUtil.show(BindPhoneActivity.this, objectBaseJson.getError().get(0));
//                }
//            }
//        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
