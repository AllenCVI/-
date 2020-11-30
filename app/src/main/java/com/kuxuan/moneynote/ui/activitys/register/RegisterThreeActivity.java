package com.kuxuan.moneynote.ui.activitys.register;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.base.BaseActivity;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.LoginJson;
import com.kuxuan.moneynote.json.netbody.RegisterBody;
import com.kuxuan.moneynote.json.netbody.SendCodeBody;
import com.kuxuan.moneynote.servier.ServiceUtil;
import com.kuxuan.moneynote.ui.activitys.MainActivity;
import com.kuxuan.moneynote.ui.activitys.eventbus.LoginEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.RefreshEvent;
import com.kuxuan.moneynote.utils.AppManager;
import com.kuxuan.moneynote.utils.CountDownTimerUtil;
import com.kuxuan.moneynote.utils.DrawableUtil;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.RegexUtils;
import com.kuxuan.moneynote.utils.SPUtil;
import com.kuxuan.moneynote.utils.ToastUtil;
import com.kuxuan.moneynote.utils.UIHelper;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 注册第三步
 * Created by xieshengqi on 2017/10/23.
 */

public class RegisterThreeActivity extends BaseActivity {
    @Bind(R.id.activity_register_three_pwd_edit)
    EditText pwd_edit;
    @Bind(R.id.activity_register_three_pwd_lookimg)
    ImageView look_pwd_img;
    @Bind(R.id.activity_register_three_againpwd_edit)
    EditText againpwd_edit;
    @Bind(R.id.activity_register_three_againpwd_delete_img)
    ImageView delete_agaimpwd_img;
    @Bind(R.id.activity_register_three_againpwd_lookimg)
    ImageView look_again_img;
    @Bind(R.id.activity_register_three_complete_btn)
    Button complete_btn;

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public static final String PHONE = "phone";

    //    private String phoneNum;
    private boolean isFindPwd;
    public static final String ISFIND_PWD = "isfind_pwd";
    /**
     * 是否查看密码
     */
    private boolean isLookPwd, isLookagainPwd;

    @Override
    public int getLayout() {
        return R.layout.activity_register_three;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFindPwd = getIntent().getExtras().getBoolean(ISFIND_PWD);
        if (isFindPwd) {
            getTitleView(1).setTitle(getResources().getString(R.string.find_pwd));
        } else {
            getTitleView(1).setTitle(getResources().getString(R.string.register));
        }
        getTitleView(1).setLeft_text("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initEdit();
        initSendText();
    }


    private void initEdit() {
        initPhoneEidt();
        initSendCodeEdit();
        pwd_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String temp = charSequence.toString();
                if (temp.length() != 0) {
                    if (!isLookPwd) {
                        look_pwd_img.setImageResource(R.mipmap.login_pw_invisible_pressed);
                    } else {
                        look_pwd_img.setImageResource(R.mipmap.login_pw_visible_pressed);
                    }
                } else {
                    if (!isLookPwd) {
                        look_pwd_img.setImageResource(R.mipmap.login_pw_invisible_normal);
                    } else {
                        look_pwd_img.setImageResource(R.mipmap.login_pw_visible_normal);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        againpwd_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String temp = charSequence.toString();
                if (temp.length() != 0) {
                    delete_agaimpwd_img.setVisibility(View.VISIBLE);
                    if (!isLookagainPwd) {
                        look_again_img.setImageResource(R.mipmap.login_pw_invisible_pressed);
                    } else {
                        look_again_img.setImageResource(R.mipmap.login_pw_visible_pressed);
                    }
                } else {
                    delete_agaimpwd_img.setVisibility(View.INVISIBLE);
                    if (!isLookagainPwd) {
                        look_again_img.setImageResource(R.mipmap.login_pw_invisible_normal);
                    } else {
                        look_again_img.setImageResource(R.mipmap.login_pw_visible_normal);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0 && pwd_edit.getText().length() != 0) {
                    complete_btn.setBackground(DrawableUtil.getShape(RegisterThreeActivity.this));
                    complete_btn.setTextColor(getResources().getColor(R.color.white));
                    complete_btn.setEnabled(true);
                } else {
                    complete_btn.setBackground(getResources().getDrawable(R.drawable.bg_gray_btn));
                    complete_btn.setTextColor(getResources().getColor(R.color.gray_text));
                    complete_btn.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.activity_register_three_pwd_lookimg, R.id.activity_register_three_againpwd_delete_img, R.id.activity_register_three_againpwd_lookimg, R.id.activity_register_three_complete_btn, R.id.activity_register_first_delete_img, R.id.activity_register_second_code_delete_img, R.id.activity_register_second_code_send_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_register_three_pwd_lookimg:
                if (isLookPwd) {
                    //变成不可见
                    isLookPwd = false;
                    pwd_edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); //字符
                    if (pwd_edit.getText().length() != 0) {
                        look_pwd_img.setImageResource(R.mipmap.login_pw_invisible_pressed);
                    } else {
                        look_pwd_img.setImageResource(R.mipmap.login_pw_invisible_normal);
                    }
                } else {
                    isLookPwd = true;
                    pwd_edit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); //字符
                    if (pwd_edit.getText().length() != 0) {
                        look_pwd_img.setImageResource(R.mipmap.login_pw_visible_pressed);
                    } else {
                        look_pwd_img.setImageResource(R.mipmap.login_pw_visible_normal);
                    }
                }
                break;
            case R.id.activity_register_three_againpwd_delete_img:
                againpwd_edit.setText("");
                break;
            case R.id.activity_register_three_againpwd_lookimg:
                if (isLookagainPwd) {
                    //变成不可见
                    againpwd_edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); //字符
                    isLookagainPwd = false;
                    if (againpwd_edit.getText().length() != 0) {
                        look_again_img.setImageResource(R.mipmap.login_pw_invisible_pressed);
                    } else {
                        look_again_img.setImageResource(R.mipmap.login_pw_invisible_normal);
                    }
                } else {
                    againpwd_edit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isLookagainPwd = true;
                    if (againpwd_edit.getText().length() != 0) {
                        look_again_img.setImageResource(R.mipmap.login_pw_visible_pressed);
                    } else {
                        look_again_img.setImageResource(R.mipmap.login_pw_visible_normal);
                    }
                }
                break;
            case R.id.activity_register_three_complete_btn:
//完成注册

                goToReigster();
                break;
            case R.id.activity_register_first_delete_img:
                phoneEdit.setText("");
                break;
            case R.id.activity_register_second_code_delete_img:
                code_edit.setText("");
                break;
            case R.id.activity_register_second_code_send_text:
                //// TODO: 2017/10/23 发送验证码
                sendCode();
                break;
        }
    }

    private void goToReigster() {
        String pwd = pwd_edit.getText().toString();
        String againPwd = againpwd_edit.getText().toString();
        String phoneNum = phoneEdit.getText().toString().replace("-", "");
        String code = code_edit.getText().toString();
        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtil.show(this, getResources().getString(R.string.no_phone));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtil.show(this, getResources().getString(R.string.verification_code_hint));
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.show(this, getResources().getString(R.string.no_pwd));
            return;
        }
        if (pwd.length() < 6) {
            ToastUtil.show(this, getResources().getString(R.string.register_three_pwd_error1));
            return;
        }
        if (TextUtils.isEmpty(againPwd)) {
            ToastUtil.show(this, getResources().getString(R.string.no_againpwd));
            return;
        }

        if (!pwd.equals(againPwd)) {
            ToastUtil.show(this, getResources().getString(R.string.nosample_pwd));
            return;
        }
        //// TODO: 2017/10/23 去注册
        if (isFindPwd) {
            findpwd(pwd, againPwd);
        } else {
            register(pwd, againPwd);
        }


    }


    private void register(final String pwd, String c_pwd) {
        showProgressDialog(getResources().getString(R.string.registering));
        final String phoneNum = phoneEdit.getText().toString().replace("-", "");
        String code = code_edit.getText().toString();
        RetrofitClient.getApiService().register(new RegisterBody(phoneNum, pwd, c_pwd, code)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<LoginJson>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                closeProgressDialog();
                ToastUtil.show(RegisterThreeActivity.this, e.message);
            }

            @Override
            public void onSuccess(BaseJson<LoginJson> loginJsonBaseJson) {
                closeProgressDialog();
                if (loginJsonBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                    LoginStatusUtil.setToken(loginJsonBaseJson.getData().getToken(), loginJsonBaseJson.getData().getId());
                    EventBus.getDefault().post(new RefreshEvent());
                    savePhoneAndPwd(phoneNum, pwd);
                    goMain();
                } else {
                    ToastUtil.show(RegisterThreeActivity.this, loginJsonBaseJson.getError().get(0));
                }
            }
        });
    }

    /**
     * 记住登录名和密码
     *
     * @param phone
     * @param pwd
     */
    private void savePhoneAndPwd(String phone, String pwd) {
        SPUtil.putAndApply(this, Constant.UserInfo.PHONE, phone);
        SPUtil.putAndApply(this, Constant.UserInfo.PWD, pwd);
    }

    /**
     * 找回密码
     *
     * @param pwd
     * @param c_pwd
     */
    private void findpwd(String pwd, String c_pwd) {
        showProgressDialog(getResources().getString(R.string.loadding));

        final String phoneNum = phoneEdit.getText().toString().replace("-", "");
        String code = code_edit.getText().toString();
        Observable<BaseJson<Object>> pwd1 = RetrofitClient.getApiService().findPwd(new RegisterBody(phoneNum, pwd, c_pwd,code));
        pwd1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                closeProgressDialog();
                ToastUtil.show(RegisterThreeActivity.this, e.message);
            }

            @Override
            public void onSuccess(BaseJson<Object> objectBaseJson) {
                closeProgressDialog();
                if (objectBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                    AppManager.getAppManager().finishActivity();
//                    UIHelper.openActivity(RegisterThreeActivity.this, LoginActivity.class);
                    finish();
                } else {
                    ToastUtil.show(RegisterThreeActivity.this, objectBaseJson.getError().get(0));
                }

            }
        });
    }

    private void goMain() {
        EventBus.getDefault().post(new LoginEvent());
        EventBus.getDefault().post(new RefreshEvent());
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.GO_TYPE, 3);
        UIHelper.openActivityWithBundle(this, MainActivity.class, bundle);
        //开启上传服务
        ServiceUtil.startUpData(this);
        AppManager.getAppManager().finishAllActivity();
        finish();
    }

    @Bind(R.id.activity_register_first_delete_img)
    ImageView deleteimg;
    @Bind(R.id.activity_register_first_edit)
    EditText phoneEdit;
    String beforeText = null;

    private void initPhoneEidt() {
        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                beforeText = charSequence.toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temp = s.toString();
                String addChar = temp.substring(start);
                String str = phoneEdit.getText().toString();
                if (beforeText.length() < s.length()) {
                    if (temp.length() == 3 || temp.length() == 8) {
                        temp += "-";
                        phoneEdit.setText(temp);
                        phoneEdit.setSelection(temp.length());
                    } else if (temp.length() == 4 || temp.length() == 9) {
                        boolean contains = temp.endsWith("-");
                        if (!contains) {
                            StringBuffer stringBuffer = new StringBuffer();
                            stringBuffer.append(temp.substring(0, temp.length() - 1));
                            stringBuffer.append("-");
                            stringBuffer.append(temp.substring(temp.length() - 1, temp.length()));
                            temp = stringBuffer.toString();
                            phoneEdit.setText(temp);
                            phoneEdit.setSelection(temp.length());
                        }
                    }
                }
                if (temp.length() != 0) {
                    deleteimg.setVisibility(View.VISIBLE);
                } else {
                    deleteimg.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (editable.length() == 13) {
//                    register_btn.setBackground(getResources().getDrawable(R.drawable.bg_orange_btn));
//                    register_btn.setTextColor(getResources().getColor(R.color.black));
//                    register_btn.setEnabled(true);
//                } else {
//                    register_btn.setBackground(getResources().getDrawable(R.drawable.bg_gray_btn));
//                    register_btn.setTextColor(getResources().getColor(R.color.gray_text));
//                    register_btn.setEnabled(false);
//                }
            }
        });
    }

    @Bind(R.id.activity_register_second_code_edit)
    EditText code_edit;
    @Bind(R.id.activity_register_second_code_delete_img)
    ImageView delete_img;
    @Bind(R.id.activity_register_second_code_send_text)
    TextView sendCode_text;

    private void initSendCodeEdit() {
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
//                if (editable.length() == 5) {
//                    toNext_btn.setBackground(getResources().getDrawable(R.drawable.bg_orange_btn));
//                    toNext_btn.setTextColor(getResources().getColor(R.color.black));
//                    toNext_btn.setEnabled(true);
//                } else {
//                    toNext_btn.setBackground(getResources().getDrawable(R.drawable.bg_gray_btn));
//                    toNext_btn.setTextColor(getResources().getColor(R.color.gray_text));
//                    toNext_btn.setEnabled(false);
//                }
            }

        });
    }

    private String code_type = "1";

    /**
     * 发送验证码
     */
    private void sendCode() {
        //判断手机号对不对

        long time = System.currentTimeMillis();
        String a = String.valueOf(time);
        String send_type = "2";
        String phone = phoneEdit.getText().toString().replaceAll("-", "");
        boolean mobileSimple = RegexUtils.isMobileSimple(phone);
        if (!mobileSimple) {
            ToastUtil.show(this, getResources().getString(R.string.error_phone));
            return;
        }
        showProgressDialog(getResources().getString(R.string.code_sendding));
        if (!isFindPwd)
            send_type = "1";
        RetrofitClient.getApiService().sendCode(new SendCodeBody(phone, code_type, send_type)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                closeProgressDialog();
                ToastUtil.show(RegisterThreeActivity.this, e.message);
            }

            @Override
            public void onSuccess(BaseJson<Object> objectBaseJson) {
                closeProgressDialog();
                if (objectBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                    ToastUtil.show(RegisterThreeActivity.this, objectBaseJson.getSuccess().get(0));
                    jugeCountDownTime();
                    countDownTimerUtil.start();
                    long l = System.currentTimeMillis();
                    Log.e("存储的时间", l + "");
                    SPUtil.putAndApply(RegisterThreeActivity.this, Constant.Time.SEND_TIME, l);
                } else {
                    ToastUtil.show(RegisterThreeActivity.this, objectBaseJson.getError().get(0));
                }
            }
        });
    }


    private static final int TIME_SEND = 60000;//毫秒
    private static final int TIME_SEND_INTERNAL = 1000;//倒计时间隔
    CountDownTimerUtil countDownTimerUtil;

    private void initSendText() {
        countDownTimerUtil = new CountDownTimerUtil(TIME_SEND, TIME_SEND_INTERNAL, sendCode_text);
    }

    private void jugeCountDownTime() {
        if (countDownTimerUtil.getMillisInFuture() != TIME_SEND) {
            initSendText();
        }
    }
}
