package com.kuxuan.moneynote.ui.activitys.bindphone;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.kuxuan.moneynote.ui.activitys.eventbus.LoginOutEvent;
import com.kuxuan.moneynote.ui.weight.CustormDialog;
import com.kuxuan.moneynote.utils.AppManager;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
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
 * 绑定第一个界面
 * @author hfrx
 */

public class BindActivity extends BaseActivity {
    @Bind(R.id.activity_register_first_edit)
    EditText editText;
    @Bind(R.id.activity_register_first_delete_img)
    ImageView deleteimg;
    @Bind(R.id.activity_register_first_register_btn)
    Button register_btn;
    boolean newUser;
    boolean isFirstWechatLogin;
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

        try {
            isFirstWechatLogin = getIntent().getExtras().getBoolean(Constant.IsFirstWEiChatLogin.ISFIRSTWEICHATLOGIN,false);
            newUser = getIntent().getExtras().getBoolean(Constant.IsFirstWEiChatLogin.NEWUSER,false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        register_btn.setText(getResources().getString(R.string.next_step));
            getTitleView(1).setTitle(getResources().getString(R.string.bind_phone));

        getTitleView(1).setLeft_text("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean iswechat_newUserFirstLogin = (boolean) SPUtil.get(getApplicationContext(),Constant.IsFirstWEiChatLogin.ISWECHATNEWUSERFIRSTLOGIN,true);
                if(isFirstWechatLogin&&iswechat_newUserFirstLogin){

                    //老用户
                    if(newUser){
                        if(iswechat_newUserFirstLogin) {
                            newUser();
                        }else {
                            finish();
                        }
                    }else {
                        oldUser();
                    }

                }else {
                    finish();
                }

            }
        });
        initEidt();
    }



    private void newUser() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("温馨提示");

        builder.setMessage("为了保证您的数据安全与多账户数据同步，请您绑定手机号，以免导致数据丢失");

        SPUtil.putAndApply(getApplicationContext(),Constant.IsFirstWEiChatLogin.ISWECHATNEWUSERFIRSTLOGIN,false);

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
                SPUtil.putAndApply(BindActivity.this,Constant.IsFirstWEiChatLogin.ISFIRSTWEICHATLOGIN,true);
                finish();

            }
        });

        builder.create().show();

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
                if(beforeText.length()<s.length()){
                    if (temp.length() == 3 || temp.length() == 8) {
                        temp += "-";
                        editText.setText(temp);
                        editText.setSelection(temp.length());
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


    private void makeSureDialog() {
        new CustormDialog(this)
                .builder()
                .setTitle("手机号确认")
                .setMsg(editText.getText().toString())
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkPhone(2);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //填写事件
                    }
                }).show();
    }


    /**
     * 1 检测是否被注册 2 检测是否已绑定
     *
     * @param check_type
     */
    private void checkPhone(int check_type) {
        String phone = editText.getText().toString().replaceAll("-","");
        RetrofitClient.getApiService().checkMobile(new CheckMobileBody(phone, check_type + "")).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                ToastUtil.show(BindActivity.this, e.message);
            }

            @Override
            public void onSuccess(BaseJson<Object> objectBaseJson) {
                if (objectBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                    //跳转
                    goToNext();
                } else {
                    ToastUtil.show(BindActivity.this, objectBaseJson.getError().get(0));
                }

            }
        });


    }



    @Override
    public void onBackPressed() {

        boolean iswechat_newUserFirstLogin = (boolean) SPUtil.get(getApplicationContext(),Constant.IsFirstWEiChatLogin.ISWECHATNEWUSERFIRSTLOGIN,true);

        if(isFirstWechatLogin){
            //老用户
            if(newUser){
                if(iswechat_newUserFirstLogin) {
                    newUser();
                }else {
                    finish();
                }

            }else {
                oldUser();
            }
        }else {
            finish();
        }

    }

    private void finishLoginOut() {
        SPUtil.remove(getApplicationContext(),Constant.IsFirstWEiChatLogin.ISWECHATNEWUSERFIRSTLOGIN);
        LoginStatusUtil.loginOut();
        EventBus.getDefault().post(new LoginOutEvent());
    }

    private void goToNext() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IsFirstWEiChatLogin.ISFIRSTWEICHATLOGIN,isFirstWechatLogin);
        UIHelper.openActivityWithBundle(BindActivity.this, BindThirdActivity.class, bundle);
    }
    @Override
    protected void onDestroy() {
        boolean iswechat_newUserFirstLogin = (boolean) SPUtil.get(getApplicationContext(),Constant.IsFirstWEiChatLogin.ISWECHATNEWUSERFIRSTLOGIN,true);
        if(isFirstWechatLogin){
            //老用户
            if(newUser){
                if(!iswechat_newUserFirstLogin) {
                    finishLoginOut();
                }
                }
            }
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}