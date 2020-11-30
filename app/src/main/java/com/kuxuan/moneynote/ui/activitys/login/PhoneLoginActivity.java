package com.kuxuan.moneynote.ui.activitys.login;

import android.content.Intent;
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
import android.widget.Toast;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.base.BaseActivity;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.db.UserDBOperator;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.LoginJson;
import com.kuxuan.moneynote.json.netbody.LoginBody;
import com.kuxuan.moneynote.json.netbody.WeChatJson;
import com.kuxuan.moneynote.listener.DownDbListener;
import com.kuxuan.moneynote.servier.ServiceUtil;
import com.kuxuan.moneynote.ui.activitys.MainActivity;
import com.kuxuan.moneynote.ui.activitys.eventbus.LoginEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.RefreshEvent;
import com.kuxuan.moneynote.ui.activitys.register.RegisterThreeActivity;
import com.kuxuan.moneynote.ui.weight.MyToast;
import com.kuxuan.moneynote.utils.AppManager;
import com.kuxuan.moneynote.utils.DownDbDataOpertor;
import com.kuxuan.moneynote.utils.DrawableUtil;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.SPUtil;
import com.kuxuan.moneynote.utils.ToastUtil;
import com.kuxuan.moneynote.utils.UIHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xieshengqi on 2017/10/23.
 */

public class PhoneLoginActivity extends BaseActivity {
    @Bind(R.id.activity_loginphone_phone_edit)
    EditText phoneEdit;
    @Bind(R.id.activity_loginphone_phone_delete_img)
    ImageView deleteImg;
    @Bind(R.id.activity_loginphone_pwd_edit)
    EditText pwdEdit;
    @Bind(R.id.activity_loginphone_pwd_lookimg)
    ImageView pwdLookimg;
    @Bind(R.id.activity_loginphone_phone_login_btn)
    Button phoneLoginBtn;
    @Bind(R.id.activity_loginphone_wchat_btn)
    Button whcat_btn;

    private boolean isLookPwd;

    private static final String TAG = "PhoneLoginActivity";

    public static final String GOTYPE = "gotype";

    //要跳转主页的页面
    private int goType = 3;

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
        return R.layout.activity_loginphone_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        getTitleView(1).setTitle("登录");
        getTitleView(1).setLeft_text("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            goType = getIntent().getExtras().getInt(GOTYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initEdit();
        putPhoneAndPwd();
        phoneLoginBtn.setBackground(DrawableUtil.getShape(this));
        whcat_btn.setBackground(DrawableUtil.getShape(this));
    }

    private void putPhoneAndPwd() {
        String phone = (String) SPUtil.get(this, Constant.UserInfo.PHONE, "");
        String pwd = (String) SPUtil.get(this, Constant.UserInfo.PWD, "");
        if (!TextUtils.isEmpty(phone)) {
            phoneEdit.setText(phone);

        }
        if (!TextUtils.isEmpty(pwd)) {
            pwdEdit.setText(pwd);
        }
    }

    private void initEdit() {
        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String temp = charSequence.toString();
                if (temp.length() != 0) {
                    deleteImg.setVisibility(View.VISIBLE);
                } else {
                    deleteImg.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pwdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String temp = charSequence.toString();
                if (temp.length() != 0) {
                    if (!isLookPwd) {
                        pwdLookimg.setImageResource(R.mipmap.login_pw_invisible_pressed);
                    } else {
                        pwdLookimg.setImageResource(R.mipmap.login_pw_visible_pressed);
                    }
                } else {
                    if (!isLookPwd) {
                        pwdLookimg.setImageResource(R.mipmap.login_pw_invisible_normal);
                    } else {
                        pwdLookimg.setImageResource(R.mipmap.login_pw_visible_normal);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.activity_loginphone_phone_delete_img, R.id.activity_loginphone_pwd_lookimg, R.id.activity_loginphone_phone_login_btn, R.id.activity_loginphone_wchat_btn, R.id.activity_loginphone_findpwd_text, R.id.activity_loginphone_register_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_loginphone_phone_delete_img:
                phoneEdit.setText("");
                break;
            case R.id.activity_loginphone_pwd_lookimg:
                if (isLookPwd) {
                    //变成不可见
                    isLookPwd = false;
                    pwdEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); //字符
                    if (pwdEdit.getText().length() != 0) {
                        pwdLookimg.setImageResource(R.mipmap.login_pw_invisible_pressed);
                    } else {
                        pwdLookimg.setImageResource(R.mipmap.login_pw_invisible_normal);
                    }
                } else {
                    isLookPwd = true;
                    pwdEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); //字符
                    if (pwdEdit.getText().length() != 0) {
                        pwdLookimg.setImageResource(R.mipmap.login_pw_visible_pressed);
                    } else {
                        pwdLookimg.setImageResource(R.mipmap.login_pw_visible_normal);
                    }
                }
                break;
            case R.id.activity_loginphone_phone_login_btn:
                login();
//                goMain();
                break;
            case R.id.activity_loginphone_wchat_btn:
//微信登录
                UMShareAPI.get(this).doOauthVerify(PhoneLoginActivity.this, SHARE_MEDIA.WEIXIN, mUMAuthListener);
//                UMShareAPI.get(PhoneLoginActivity.this).getPlatformInfo(PhoneLoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.activity_loginphone_findpwd_text:
                //找回密码
                Bundle bundle = new Bundle();
                bundle.putBoolean(RegisterThreeActivity.ISFIND_PWD, true);
                UIHelper.openActivityWithBundle(PhoneLoginActivity.this, RegisterThreeActivity.class, bundle);
                break;
            case R.id.activity_loginphone_register_text:
                //注册
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean(RegisterThreeActivity.ISFIND_PWD, false);
                UIHelper.openActivityWithBundle(PhoneLoginActivity.this, RegisterThreeActivity.class, bundle1);
                break;
        }
    }


    private UMAuthListener mUMAuthListener = new UMAuthListener() {


        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

            UMShareAPI.get(PhoneLoginActivity.this).getPlatformInfo(PhoneLoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };

    boolean newUser = false;
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            String openid = data.get("openid");
            String unionid = data.get("uid");
            String gender = data.get("gender");
            String iconurl = data.get("iconurl");
            String name = data.get("name");
            Log.e(TAG, "openid:" + openid + "\n" + "gender:" + gender + "\n" + "iconurl:" + iconurl + "\n" + "name:" + name);
            String sex;
            if ("男".equals(gender)) {
                sex = "1";
            } else {
                sex = "2";
            }
            RetrofitClient.getApiService().weChatLogin(new WeChatJson(openid, name, sex, iconurl, unionid)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<LoginJson>>() {
                @Override
                public void onError(ExceptionHandle.ResponeThrowable e) {

                }

                @Override
                public void onSuccess(BaseJson<LoginJson> objectBaseJson) {

                    if (objectBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                        LoginStatusUtil.setToken(objectBaseJson.getData().getToken(), objectBaseJson.getData().getId());
                        //跳转
                        int isNewUser = objectBaseJson.getData().getNew_user();

                        if (isNewUser == 1) {
                            newUser = true;
                            SPUtil.putAndApply(PhoneLoginActivity.this, Constant.IsFirstWEiChatLogin.IsLoginOut, true);
                        }
                        boolean flag = (boolean) SPUtil.get(getApplicationContext(), Constant.IsFirstWEiChatLogin.ISFIRSTWEICHATLOGIN, false);
                        if (flag && !newUser) {
                            Toast.makeText(getApplicationContext(), "微信登录成功", Toast.LENGTH_SHORT).show();
                        }

                        SPUtil.putAndApply(PhoneLoginActivity.this, Constant.IsFirstWEiChatLogin.ISWEICHATLOGIN, true);
//                        syncData(0);
                        sync();
                    } else {

                    }

                }
            });
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "微信登录失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "微信登录取消", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

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
     * 登录
     */
    private void login() {
        final String phone = phoneEdit.getText().toString();
        final String pwd = pwdEdit.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show(this, getResources().getString(R.string.error_phone));
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.show(this, getResources().getString(R.string.error_pwd));
            return;
        }
        showProgressDialog(getResources().getString(R.string.logining));
        LoginStatusUtil.loginOut();
        RetrofitClient.getApiService().login(new LoginBody(phone, pwd)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<LoginJson>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                closeProgressDialog();
                ToastUtil.show(PhoneLoginActivity.this, e.message);
            }

            @Override
            public void onSuccess(BaseJson<LoginJson> loginJsonBaseJson) {
                if (loginJsonBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                    savePhoneAndPwd(phone, pwd);
                    if (loginJsonBaseJson.getData().getName() != null) {
                        SPUtil.putAndApply(PhoneLoginActivity.this, Constant.UserInfo.MOBILE, loginJsonBaseJson.getData().getName());
                    }
                    LoginStatusUtil.setToken(loginJsonBaseJson.getData().getToken(), loginJsonBaseJson.getData().getId());
                    SPUtil.putAndApply(PhoneLoginActivity.this, Constant.IsFirstWEiChatLogin.ISWEICHATLOGIN, false);
                    sync();
//                    syncData(0);
//                    goMain();
                } else {
                    closeProgressDialog();
                    ToastUtil.show(PhoneLoginActivity.this, loginJsonBaseJson.getError().get(0));
                }
            }
        });
    }

    private void goMain() {


        LoginStatusUtil.loginIn();
        UserDBOperator.getInstance().insertUserId(LoginStatusUtil.getLoginUserId());
        sendEventMessage();
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.GO_TYPE, goType);
        UIHelper.openActivityWithBundle(this, MainActivity.class, bundle);
        //开启上传服务
        ServiceUtil.startUpData(this);
        AppManager.getAppManager().finishAllActivity();
        finish();
    }


    private void sync() {
        DownDbDataOpertor.getInstance().syncForThread(new DownDbListener() {
            @Override
            public void onStart() {
                showProgressDialog("数据同步中...");
            }

            @Override
            public void onSuccess() {
                closeProgressDialog();
                goMain();
            }

            @Override
            public void onFail() {
                closeProgressDialog();
                ServiceUtil.startDownLoadData(PhoneLoginActivity.this);
                goMain();
                MyToast.makeText(PhoneLoginActivity.this, "数据同步失败", Toast.LENGTH_SHORT);
            }
        });
    }
//    /**
//     * 同步数据
//     */
//    private void syncData(final int page) {
//
//
//
//
//
//
//        Log.e("page", page + "");
//
//        RetrofitClient.getApiService().getDownLoadData(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<UploadBeanJson>>() {
//            @Override
//            public void onError(ExceptionHandle.ResponeThrowable e) {
//                closeProgressDialog();
//                ServiceUtil.startDownLoadData(PhoneLoginActivity.this);
//                goMain();
//                MyToast.makeText(PhoneLoginActivity.this, "数据同步失败", Toast.LENGTH_SHORT);
//            }
//
//            @Override
//            public void onSuccess(BaseJson<UploadBeanJson> objectBaseJson) {
//                if (objectBaseJson != null && objectBaseJson.getCode() == 0) {
//                    UploadBeanJson res = objectBaseJson.getData();
//                    if (res != null) {
//                        int number = res.getNumber();
//                        if (res.getData() != null) {
//                            insertDb(objectBaseJson.getData().getData());
//                            if (res.getData().size() < number) {
//                                //结束
//                                closeProgressDialog();
//                                goMain();
//                            } else {
//                                syncData(objectBaseJson.getData().getPage() + 1);
//                            }
//                        } else {
//                            closeProgressDialog();
//                            goMain();
//                        }
//
//                    } else {
//                        closeProgressDialog();
//                        goMain();
//                    }
//                }
//
//            }
//        });
//    }
//
//    private void insertDb(ArrayList<UploadDbjson> uploadDbjsons) {
//        if (uploadDbjsons == null) {
//            return;
//        }
//        CategoryDaoOperator categoryDaoOperator = CategoryDaoOperator.newInstance();
//        Integer id = (Integer) SPUtil.get(MyApplication.getInstance(), Constant.DbInfo.DB_ID_COUNT, -1);
//        for (UploadDbjson uploadDbjson : uploadDbjsons) {
//            long time = uploadDbjson.getTime() * 1000;
//            long create_time = uploadDbjson.getCreated_at() * 1000;
//            long updata_time = uploadDbjson.getUpdated_at() * 1000;
//            if (create_time == 0) {
//                create_time =uploadDbjson.getTime();
//            }
//            if (updata_time == 0) {
//                updata_time = time;
//            }
//            String data = TimeUtlis.getData(time);
//            String[] split = data.split("-");
//            id++;
//            categoryDaoOperator.insert((long)id,uploadDbjson.getIdentification(), uploadDbjson.getDemo(), uploadDbjson.getCategory_name(), uploadDbjson.getType(), BillCategoreDaoOperator.newInstance().getDetaillIconUrlById(uploadDbjson.getCategory_id()), Double.parseDouble(uploadDbjson.getAccount()),uploadDbjson.getCategory_id(),Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]),create_time, updata_time, uploadDbjson.getStatus(), uploadDbjson.getUser_id(), false);
//        }
//        SPUtil.putAndApply(MyApplication.getInstance(), Constant.DbInfo.DB_ID_COUNT, id);
//
//    }


    private void sendEventMessage() {
        LoginEvent loginEvent = new LoginEvent(newUser);
        EventBus.getDefault().post(loginEvent);
        EventBus.getDefault().post(new RefreshEvent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).deleteOauth(PhoneLoginActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
        AppManager.getAppManager().finishActivity(this);
    }
}
