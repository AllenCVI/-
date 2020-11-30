package com.kuxuan.moneynote.ui.activitys.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.base.BaseFragmentActivity;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.db.UserDBOperator;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.LoginJson;
import com.kuxuan.moneynote.json.netbody.WeChatJson;
import com.kuxuan.moneynote.listener.DownDbListener;
import com.kuxuan.moneynote.servier.ServiceUtil;
import com.kuxuan.moneynote.ui.activitys.MainActivity;
import com.kuxuan.moneynote.ui.activitys.eventbus.LoginEvent;
import com.kuxuan.moneynote.ui.activitys.register.RegisterThreeActivity;
import com.kuxuan.moneynote.ui.weight.ActionSheetDialog;
import com.kuxuan.moneynote.utils.AppManager;
import com.kuxuan.moneynote.utils.DownDbDataOpertor;
import com.kuxuan.moneynote.utils.DrawableUtil;
import com.kuxuan.moneynote.utils.GlideRoundTransform;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.SPUtil;
import com.kuxuan.moneynote.utils.UIHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.uploadlog.UMLog;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 登录页
 * Created by xieshengqi on 2017/10/23.
 */

public class LoginActivity extends BaseFragmentActivity {
    @Bind(R.id.activity_login_wchat_btn)
    Button wchat_btn;
    @Bind(R.id.activity_login_more_text)
    TextView login_more_text;

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    public static final String GOTYPE = "gotype";

    //要跳转主页的页面
    private int goType = 3;
    @Bind(R.id.logo)
    ImageView mLogo;

    private static final String TAG = "LoginActivity";

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            goType = getIntent().getExtras().getInt(GOTYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        UMLog.setIsOpenShareEdit(true);
        AppManager.getAppManager().addActivity(this);
        Glide.with(this).load(R.mipmap.icon_logo).transform(new GlideRoundTransform(this, 10)).into(mLogo);
        wchat_btn.setBackground(DrawableUtil.getShape(this));
    }


    private UMAuthListener mUMAuthListener = new UMAuthListener() {


        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

            UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };


    @OnClick({R.id.activity_login_wchat_btn, R.id.activity_login_more_text, R.id.activity_login_finish_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_login_wchat_btn:
//                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
//                UMShareAPI.get(this).doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                UMShareAPI.get(this).doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN, mUMAuthListener);
                break;
            case R.id.activity_login_more_text:
                choose();
                break;
            case R.id.activity_login_finish_text:
                finish();
                break;
        }
    }

    private void choose() {
        new ActionSheetDialog(LoginActivity.this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("注册",
                        ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                //填写事件
                                //注册
                                Bundle bundle = new Bundle();
                                bundle.putBoolean(RegisterThreeActivity.ISFIND_PWD, false);
                                UIHelper.openActivityWithBundle(LoginActivity.this, RegisterThreeActivity.class, bundle);
//                                Bundle bundle1 = new Bundle();
//                                bundle1.putBoolean(RegisterFirstActivity.ISFIND_PWD, false);
//                                UIHelper.openActivityWithBundle(LoginActivity.this, RegisterFirstActivity.class, bundle1);
                            }
                        })
                .addSheetItem("手机号登录",
                        ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                //填写事件
                                //手机登录
                                Intent intent1 = new Intent(LoginActivity.this, PhoneLoginActivity.class);
                                startActivity(intent1);
                            }
                        }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).deleteOauth(LoginActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
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

    boolean newUser = false;
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
            Log.e("授权回调", platform.toString());
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            String openid = data.get("openid");
            String unionid = data.get("uid");
            String gender = data.get("gender");
            String iconurl = data.get("iconurl");
            String name = data.get("name");
            Log.e(TAG, "openid" + openid + "gender" + gender + "iconurl" + iconurl + "name" + name);
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

                        int isNewUser = objectBaseJson.getData().getNew_user();

                        if (isNewUser == 1) {
                            newUser = true;
                            SPUtil.putAndApply(LoginActivity.this, Constant.IsFirstWEiChatLogin.IsLoginOut, true);
                        }
                        boolean flag = (boolean) SPUtil.get(getApplicationContext(), Constant.IsFirstWEiChatLogin.ISFIRSTWEICHATLOGIN, false);
                        if (flag && !newUser) {

                            Toast.makeText(getApplicationContext(), "微信登录成功", Toast.LENGTH_SHORT).show();
                        }

                        LoginStatusUtil.setToken(objectBaseJson.getData().getToken(), objectBaseJson.getData().getId());
//                        EventBus.getDefault().post(new RefreshEvent());
                        SPUtil.putAndApply(LoginActivity.this, Constant.IsFirstWEiChatLogin.ISWEICHATLOGIN, true);
                        //跳转
                        syncData(0);

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

    private void goToNext() {

        LoginStatusUtil.loginIn();
        UserDBOperator.getInstance().insertUserId(LoginStatusUtil.getLoginUserId());
        LoginEvent loginEvent = new LoginEvent(newUser);
        EventBus.getDefault().post(loginEvent);
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.GO_TYPE, goType);
        UIHelper.openActivityWithBundle(this, MainActivity.class, bundle);
        //开启上传服务
        ServiceUtil.startUpData(this);
        finish();
    }


    /**
     * 同步数据
     */
    private void syncData(final int page) {

        DownDbDataOpertor.getInstance().syncForThread(new DownDbListener() {
            @Override
            public void onStart() {
                showProgressDialog("数据同步中...");
            }

            @Override
            public void onSuccess() {
                closeProgressDialog();
                goToNext();
            }

            @Override
            public void onFail() {
                closeProgressDialog();
                ServiceUtil.startDownLoadData(LoginActivity.this);
                goToNext();
            }
        });

//        showProgressDialog("数据同步中...");
//        RetrofitClient.getApiService().getDownLoadData(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<UploadBeanJson>>() {
//            @Override
//            public void onError(ExceptionHandle.ResponeThrowable e) {
//                closeProgressDialog();
//                ServiceUtil.startDownLoadData(LoginActivity.this);
//                goToNext();
////                MyToast.makeText(LoginActivity.this, "数据同步失败", Toast.LENGTH_SHORT);
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
//                                goToNext();
//                            } else {
//
//                                syncData(objectBaseJson.getData().getPage()+1);
//                            }
//                        } else {
//                            closeProgressDialog();
//                            goToNext();
//                        }
//
//                    } else {
//                        closeProgressDialog();
//                        goToNext();
//                    }
//                }
//
//            }
//        });
    }

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



}
