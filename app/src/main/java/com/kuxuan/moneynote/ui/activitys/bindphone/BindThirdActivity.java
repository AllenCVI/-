package com.kuxuan.moneynote.ui.activitys.bindphone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.base.BaseActivity;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.netbody.BindBody;
import com.kuxuan.moneynote.ui.activitys.MainActivity;
import com.kuxuan.moneynote.ui.activitys.eventbus.LoginOutEvent;
import com.kuxuan.moneynote.utils.AppManager;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 绑定手机号第三步
 *
 * @author HFRX
 */

public class BindThirdActivity extends BaseActivity {
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
    @Bind(R.id.sendcode_layout)
    LinearLayout sendCode_layout;
    boolean newUser;
    boolean isFirstWechatLogin;


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
        sendCode_layout.setVisibility(View.GONE);
        try {
            isFirstWechatLogin = getIntent().getExtras().getBoolean(Constant.IsFirstWEiChatLogin.ISFIRSTWEICHATLOGIN, false);
            newUser = getIntent().getExtras().getBoolean(Constant.IsFirstWEiChatLogin.NEWUSER, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getTitleView(1).setTitle(getResources().getString(R.string.bind_phone));
        AppManager.getAppManager().addActivity(this);
        getTitleView(1).setLeft_text("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean iswechat_newUserFirstLogin = (boolean) SPUtil.get(getApplicationContext(), Constant.IsFirstWEiChatLogin.ISWECHATNEWUSERFIRSTLOGIN, true);
                if (isFirstWechatLogin && iswechat_newUserFirstLogin) {
                    //老用户
                    if (newUser) {
                        if (iswechat_newUserFirstLogin) {
                            newUser();
                        } else {
                            finish();
                        }
                    } else {
                        oldUser();
                    }

                } else {
                    finish();
                }

            }
        });
        initEdit();
        initPhoneEidt();
    }


    private void newUser() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("温馨提示");

        builder.setMessage("为了保证您的数据安全与多账户数据同步，请您绑定手机号，以免导致数据丢失");

        SPUtil.putAndApply(getApplicationContext(), Constant.IsFirstWEiChatLogin.ISWECHATNEWUSERFIRSTLOGIN, false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }


    private void oldUser() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("温馨提示");

        builder.setMessage("为了保证您的数据安全与多账户数据同步，请您绑定手机号，以免导致数据丢失");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });


        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                SPUtil.putAndApply(BindThirdActivity.this, Constant.IsFirstWEiChatLogin.ISFIRSTWEICHATLOGIN, true);
                finish();

            }
        });

        builder.create().show();

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

    private void initEdit() {
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
                    complete_btn.setBackground(DrawableUtil.getShape(BindThirdActivity.this));
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

    @OnClick({R.id.activity_register_three_pwd_lookimg, R.id.activity_register_three_againpwd_delete_img, R.id.activity_register_three_againpwd_lookimg, R.id.activity_register_three_complete_btn})
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
            default:
                break;
        }
    }

    private void goToReigster() {
        String pwd = pwd_edit.getText().toString();
        String againPwd = againpwd_edit.getText().toString();
        String phoneNum = phoneEdit.getText().toString().replace("-", "");
        boolean mobileSimple = RegexUtils.isMobileSimple(phoneNum);
        if (!mobileSimple) {
            ToastUtil.show(this, getResources().getString(R.string.error_phone));
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
        bind(pwd, againPwd);
    }


    private void bind(String pwd, String c_pwd) {
        showProgressDialog(getResources().getString(R.string.binding));
        String phoneNum = phoneEdit.getText().toString().replace("-", "");
        RetrofitClient.getApiService().bindPhone(new BindBody(phoneNum, pwd, c_pwd)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                closeProgressDialog();
                ToastUtil.show(BindThirdActivity.this, e.message);
            }

            @Override
            public void onSuccess(BaseJson<Object> loginJsonBaseJson) {
                closeProgressDialog();
                if (loginJsonBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                    if (isFirstWechatLogin) {
                        SPUtil.putAndApply(BindThirdActivity.this, Constant.IsFirstWEiChatLogin.ISFIRSTWEICHATLOGIN, true);
                    }
                    SPUtil.putAndApply(BindThirdActivity.this, Constant.IsFirstWEiChatLogin.IsLoginOut, false);
                    goMain();
                } else {
                    ToastUtil.show(BindThirdActivity.this, loginJsonBaseJson.getError().get(0));
                }
            }
        });
    }


    private int goType = 3;

    private void goMain() {
//        AppManager.getAppManager().finishAllActivity();
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.GO_TYPE, goType);
        UIHelper.openActivityWithBundle(this, MainActivity.class, bundle);
        finish();
    }


    @Override
    public void onBackPressed() {

        boolean iswechat_newUserFirstLogin = (boolean) SPUtil.get(getApplicationContext(), Constant.IsFirstWEiChatLogin.ISWECHATNEWUSERFIRSTLOGIN, true);

        if (isFirstWechatLogin) {
            //老用户
            if (newUser) {
                if (iswechat_newUserFirstLogin) {
                    newUser();
                } else {
                    finish();
                }

            } else {
                oldUser();
            }
        } else {
            finish();
        }

    }

    private void finishLoginOut() {
        SPUtil.remove(getApplicationContext(), Constant.IsFirstWEiChatLogin.ISWECHATNEWUSERFIRSTLOGIN);
        LoginStatusUtil.loginOut();
        EventBus.getDefault().post(new LoginOutEvent());
    }


    @Override
    protected void onDestroy() {
        boolean iswechat_newUserFirstLogin = (boolean) SPUtil.get(getApplicationContext(), Constant.IsFirstWEiChatLogin.ISWECHATNEWUSERFIRSTLOGIN, true);
        if (isFirstWechatLogin) {
            //老用户
            if (newUser) {
                if (!iswechat_newUserFirstLogin) {
                    finishLoginOut();
                }
            }
        }
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
