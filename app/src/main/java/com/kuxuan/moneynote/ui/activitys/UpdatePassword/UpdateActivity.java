package com.kuxuan.moneynote.ui.activitys.UpdatePassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.base.BaseActivity;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.utils.AppManager;
import com.kuxuan.moneynote.utils.DrawableUtil;
import com.kuxuan.moneynote.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 绑定手机号第三步
 *
 * @author HFRX
 */

public class UpdateActivity extends BaseActivity {

    public static final String PHONE = "phone";

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
    //新密码
    @Bind(R.id.activity_update_three_new_password_edit)
    EditText mNewPasswordEdit;
    @Bind(R.id.activity_update_three_new_password_delete_img)
    ImageView mNewPasswordDeleteImg;
    @Bind(R.id.activity_update_new_password_lookimg)
    ImageView mNewPasswordLookImg;

    private String phoneNum;

    private static final String TAG = "UpdateActivity";

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 是否查看密码
     */
    private boolean isLookPwd, isLookagainPwd, isLookNewPassword;

    @Override
    public int getLayout() {
        return R.layout.activity_update;
    }

    public static void show(Context context, String phoneNum) {
        Intent intent = new Intent(context, UpdateActivity.class);
        intent.putExtra(PHONE, phoneNum);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleView(1).setTitle(getResources().getString(R.string.update_password));
        try {
            phoneNum = getIntent().getExtras().getString(PHONE);
        } catch (Exception e) {

        }

        AppManager.getAppManager().addActivity(this);
        getTitleView(1).setLeft_text("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initEdit();
        complete_btn.setBackground(getResources().getDrawable(R.drawable.bg_gray_btn));
        complete_btn.setTextColor(getResources().getColor(R.color.gray_text));
        complete_btn.setEnabled(false);
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
                    complete_btn.setBackground(DrawableUtil.getShape(UpdateActivity.this));
                    complete_btn.setTextColor(getResources().getColor(R.color.white));
                    complete_btn.setEnabled(true);
                } else {
                    complete_btn.setBackground(getResources().getDrawable(R.drawable.bg_gray_btn));
                    complete_btn.setTextColor(getResources().getColor(R.color.gray_text));
                    complete_btn.setEnabled(false);
                }
            }
        });

        mNewPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String temp = charSequence.toString();
                if (temp.length() != 0) {
                    mNewPasswordDeleteImg.setVisibility(View.VISIBLE);
                    if (!isLookNewPassword) {
                        mNewPasswordLookImg.setImageResource(R.mipmap.login_pw_invisible_pressed);
                    } else {
                        mNewPasswordLookImg.setImageResource(R.mipmap.login_pw_visible_pressed);
                    }
                } else {
                    mNewPasswordDeleteImg.setVisibility(View.INVISIBLE);
                    if (!isLookNewPassword) {
                        mNewPasswordLookImg.setImageResource(R.mipmap.login_pw_invisible_normal);
                    } else {
                        mNewPasswordLookImg.setImageResource(R.mipmap.login_pw_visible_normal);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//
                if (editable.length() != 0 && pwd_edit.getText().length() != 0) {

                } else {
                    complete_btn.setBackground(getResources().getDrawable(R.drawable.bg_gray_btn));
                    complete_btn.setTextColor(getResources().getColor(R.color.gray_text));
                    complete_btn.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.activity_register_three_pwd_lookimg,
            R.id.activity_register_three_againpwd_delete_img,
            R.id.activity_register_three_againpwd_lookimg,
            R.id.activity_register_three_complete_btn,
            R.id.activity_update_new_password_lookimg,
            R.id.activity_update_three_new_password_delete_img})
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
            case R.id.activity_update_three_new_password_delete_img:
                mNewPasswordEdit.setText("");
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

            case R.id.activity_update_new_password_lookimg:
                if (isLookNewPassword) {

                    //变成不可见
                    //字符
                    mNewPasswordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isLookNewPassword = false;
                    if (mNewPasswordEdit.getText().length() != 0) {
                        mNewPasswordLookImg.setImageResource(R.mipmap.login_pw_invisible_pressed);
                    } else {
                        mNewPasswordLookImg.setImageResource(R.mipmap.login_pw_invisible_normal);
                    }
                } else {
                    mNewPasswordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isLookNewPassword = true;
                    if (mNewPasswordEdit.getText().length() != 0) {
                        mNewPasswordLookImg.setImageResource(R.mipmap.login_pw_visible_pressed);
                    } else {
                        mNewPasswordLookImg.setImageResource(R.mipmap.login_pw_visible_normal);
                    }
                }
                break;


            case R.id.activity_register_three_complete_btn:
                //完成修改
                goToReigster();
                break;
            default:
                break;
        }
    }

    private void goToReigster() {
        String pwd = pwd_edit.getText().toString();
        String againPwd = againpwd_edit.getText().toString();
        String mNewPwd = mNewPasswordEdit.getText().toString();
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

        if (!mNewPwd.equals(againPwd)) {
            ToastUtil.show(this, getResources().getString(R.string.nosample_pwd));
            return;
        }
        bind(pwd, againPwd, mNewPwd);

    }


    private void bind(String pwd, String c_pwd, String newPassword) {
        showProgressDialog(getResources().getString(R.string.binding));
        RetrofitClient.getApiService().updatePassword( pwd, newPassword, c_pwd).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                closeProgressDialog();
                ToastUtil.show(UpdateActivity.this, e.message);
            }

            @Override
            public void onSuccess(BaseJson<Object> loginJsonBaseJson) {
                closeProgressDialog();
                if (loginJsonBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                    ToastUtil.show(UpdateActivity.this,"修改成功");
                    goMain();
                } else {
                    ToastUtil.show(UpdateActivity.this, loginJsonBaseJson.getError().get(0));
                }
            }
        });
    }


    private void goMain() {

        finish();
    }

}
