package com.kuxuan.moneynote.ui.activitys.register;

import android.os.Bundle;
import android.text.Editable;
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
import com.kuxuan.moneynote.json.netbody.CheckMobileBody;
import com.kuxuan.moneynote.ui.weight.CustormDialog;
import com.kuxuan.moneynote.utils.AppManager;
import com.kuxuan.moneynote.utils.ToastUtil;
import com.kuxuan.moneynote.utils.UIHelper;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 注册第一个页面
 * Created by xieshengqi on 2017/10/23.
 */

public class RegisterFirstActivity extends BaseActivity {
    @Bind(R.id.activity_register_first_edit)
    EditText editText;
    @Bind(R.id.activity_register_first_delete_img)
    ImageView deleteimg;
    @Bind(R.id.activity_register_first_register_btn)
    Button register_btn;
    private boolean isFindPwd;


    public static final String ISFIND_PWD = "isfind_pwd";

    @Override
    public int getLayout() {
        return R.layout.activity_register_first;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        isFindPwd = getIntent().getExtras().getBoolean(ISFIND_PWD);

        if (isFindPwd) {
            register_btn.setText(getResources().getString(R.string.next_step));
            getTitleView(1).setTitle(getResources().getString(R.string.find_pwd));
        } else {
            register_btn.setText(getResources().getString(R.string.register));
            getTitleView(1).setTitle(getResources().getString(R.string.register));
        }

        getTitleView(1).setLeft_text("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
        initEidt();
    }


    String beforeText = null;

    private void initEidt() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                beforeText = charSequence.toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temp = s.toString();
                String addChar = temp.substring(start);
                String str = editText.getText().toString();
                if (beforeText.length() < s.length()) {
                    if (temp.length() == 3 || temp.length() == 8) {
                        temp += "-";
                        editText.setText(temp);
                        editText.setSelection(temp.length());
                    } else if (temp.length() == 4 || temp.length() == 9) {
                        boolean contains = temp.endsWith("-");
                        if (!contains) {
                            StringBuffer stringBuffer = new StringBuffer();
                            stringBuffer.append(temp.substring(0, temp.length() - 1));
                            stringBuffer.append("-");
                            stringBuffer.append(temp.substring(temp.length() - 1, temp.length()));
                            temp = stringBuffer.toString();
                            editText.setText(temp);
                            editText.setSelection(temp.length());
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
                if (editable.length() == 13) {
                    register_btn.setBackground(getResources().getDrawable(R.drawable.bg_orange_btn));
                    register_btn.setTextColor(getResources().getColor(R.color.black));
                    register_btn.setEnabled(true);
                } else {
                    register_btn.setBackground(getResources().getDrawable(R.drawable.bg_gray_btn));
                    register_btn.setTextColor(getResources().getColor(R.color.gray_text));
                    register_btn.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.activity_register_first_delete_img, R.id.activity_register_first_register_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_register_first_delete_img:
                editText.setText("");
                break;
            case R.id.activity_register_first_register_btn:
                //进入注册下一步
                makeSureDialog();
                break;
        }
    }


    /**
     * 1 检测是否被注册 2 检测是否已绑定
     *
     * @param check_type
     */
    private void checkPhone(int check_type) {
        String phone = editText.getText().toString().replaceAll("-", "");
        RetrofitClient.getApiService().checkMobile(new CheckMobileBody(phone, check_type + "")).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {

                ToastUtil.show(RegisterFirstActivity.this, e.message);

            }

            @Override
            public void onSuccess(BaseJson<Object> objectBaseJson) {
                if (objectBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                    //跳转
                    goToNext();

                } else {
                    ToastUtil.show(RegisterFirstActivity.this, objectBaseJson.getError().get(0));
                }
            }
        });


    }


    private void makeSureDialog() {
        new CustormDialog(this)
                .builder()
                .setTitle("手机号确认")
                .setMsg(editText.getText().toString())
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFindPwd) {
                            goToNext();
                        } else {
                                checkPhone(1);
                        }
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //填写事件
                    }
                }).show();
    }


    private void goToNext() {
        Bundle bundle = new Bundle();
        bundle.putString(RegisterSecondActivity.PHONE, editText.getText().toString());
        bundle.putBoolean(RegisterSecondActivity.ISFIND_PWD, isFindPwd);
        UIHelper.openActivityWithBundle(RegisterFirstActivity.this, RegisterSecondActivity.class, bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
