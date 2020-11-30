package com.kuxuan.moneynote.ui.activitys;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.db.BillCategoreDaoOperator;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.BillTypeJson;
import com.kuxuan.moneynote.listener.TokenListener;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.SPUtil;
import com.kuxuan.moneynote.utils.TokenUtil;
import com.kuxuan.moneynote.utils.Tools;
import com.kuxuan.moneynote.utils.UIHelper;
import com.kuxuan.sqlite.db.BillCategoreDB;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xieshengqi on 2017/10/30.
 */

public class LunchActivity extends Activity {


    private static final int TOMAIN = 0;
    private static final int TOLOGIN = 1;
    /**
     * 延时
     */
    private static final int TIME = 3000;

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initStatus() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            try {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            } catch (Exception e) {
            }
        }

    }

    //有token进主页无token进登录页
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case TOMAIN:
//                    goMain();
//                    break;
//                case TOLOGIN:
////                    goLogin();
//                    goMain();
//                    break;
//            }
//        }
//    };

    private void goMain() {
//        boolean isInsert = (boolean) SPUtil.get(getApplicationContext(), Constant.CategoreyDBConstant.ISINSERT, false);
//            if (!isInsert) {
        Log.e("是否添加图片到数据库", "true");
        int lastID = CategoryDaoOperator.newInstance().getLastID();
        SPUtil.putAndApply(this, Constant.DbInfo.DB_ID_COUNT, lastID);
        //首次进去添加类别数据库
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                initBillCategore();
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                SPUtil.putAndApply(getApplicationContext(), Constant.CategoreyDBConstant.ISINSERT, true);
                if (handler != null)
                    handler.sendEmptyMessageDelayed(1, 1);
            }
        });
//        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(1);
            handler = null;

        }

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            goToNext();
        }
    };


    private void goToNext() {
//        LoginStatusUtil.setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6Ijg1ZWZjZWJiMWJhYjI1ZGU5ZWI1MDMwNWJjZmJlZTAxM2I5YzA2ZWE2YzllM2JiZDA3MGRlNjdjZmI3ZGYwNDY1NDI0MTBkYzE1Njk1OTBmIn0.eyJhdWQiOiIzIiwianRpIjoiODVlZmNlYmIxYmFiMjVkZTllYjUwMzA1YmNmYmVlMDEzYjljMDZlYTZjOWUzYmJkMDcwZGU2N2NmYjdkZjA0NjU0MjQxMGRjMTU2OTU5MGYiLCJpYXQiOjE1MjUzMzUzNjAsIm5iZiI6MTUyNTMzNTM2MCwiZXhwIjoxNTU2ODcxMzYwLCJzdWIiOiIxMTg1NTIzMyIsInNjb3BlcyI6W119.iRPOYB-4pLb3s0RWVYww8t-S7voXWgrODT7RYu4iSZgp9zwF0AIyr0hPva1fiZAZT1QE2DzSlmSDQN67PIoLa2DdZxyNTyKsmRzCOhw00KtzR7Ruvg0dj3ebPXy7gKwTiiNTR73ScthBN5Zds00XUkE2o70R6z1zXu_jQ3BqnBra_qL3uHebse08fINxaATEoKqJUTDlfEGW2trmmjpQ-UT-YE7S5W4D4rcQZZGWwvDSyTIW6l2ipcAtrFl1UbdZwYyvYBHHFchHzWOU76nY3k_XNnYrpnXmOOigVgedh061THrrhnFcyJBrAhp0uFWYJbXrthX4KjENqrMsVbaXiGKw5I3vbx8wvdlKbjhw95iLZ5vTcyMCnVMy2JYBLi8vh8ZoX8pfGZpxv7LcHnwESy-6S89XpOdNr_yK3xos3Xk_x3Y2pp-fH-1jxCAhbkLuXqU-emsKiO5tCzTJ-leoz1GczdX2ZJKthjnu8v3VITIxQjYnnZievWKhoQR8HT00AceQHmIKY3Jg1B4Y4ctyNrTCkE8GnrUVW10BwZ9E99cdt4G8uqfzmMyIHF2OHNaVCqnaWFCgZj3qX1WRTigClX2MBKPLRLTiPHGrcX64x2Pts0UrSnmlOyux6bI5PxHGq-u8aK6oDJfuSUu_M6Yw19ZKcQ8I7nJzPbUPaPDv4tE");
        if (LoginStatusUtil.isLoginin()) {
            String token = LoginStatusUtil.getToken();
            if (token.length() > 42) {
//说明是老token（需要更换成新的）
                TokenUtil.getNewToken(new TokenListener() {
                    @Override
                    public void onSuccess() {
                        go();
                    }

                    @Override
                    public void onFail() {
                        LoginStatusUtil.loginOut();
                        go();
                    }
                });
            } else {
                go();
            }
        } else {
            go();
        }


    }

    private void go() {
        SPUtil.remove(getApplicationContext(), Constant.IsFirstWEiChatLogin.ISWECHATNEWUSERFIRSTLOGIN);
        if ((Boolean) SPUtil.get(LunchActivity.this, Constant.IsFirstWEiChatLogin.ISWEICHATLOGIN, false) && (Boolean) SPUtil.get(LunchActivity.this, Constant.IsFirstWEiChatLogin.IsLoginOut, false)) {
            LoginStatusUtil.loginOut();
        }
        UIHelper.openActivity(LunchActivity.this, MainActivity.class);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);
        //进主页
        goMain();
    }


    private void initBillCategore() {

        BillCategoreDaoOperator billCategoreDaoOperator = BillCategoreDaoOperator.newInstance();

        String json = Tools.getJson("categore.txt", getApplicationContext());

        Gson gson = new Gson();
        Type tp = new TypeToken<List<BillTypeJson>>() {
        }.getType();
        List<BillTypeJson> categoryDBJson = gson.fromJson(json, tp);
        List<BillCategoreDB> categoreDBS = new ArrayList<>();
        if (categoreDBS != null) {
            for (int i = 0; i < categoryDBJson.size(); i++) {
                BillTypeJson billCategoreDB = categoryDBJson.get(i);
                billCategoreDB.setIcon("file:///android_asset/icon_" + i + ".png");
                billCategoreDB.setSelected_icon("file:///android_asset/selected_icon_" + i + ".png");
                billCategoreDB.setDetail_icon("file:///android_asset/detail_icon_" + i + ".png");
                categoreDBS.add(new BillCategoreDB(billCategoreDB.getId(), billCategoreDB.getCategory_type(), billCategoreDB.getIcon(), billCategoreDB.getSelected_icon(), billCategoreDB.getDetail_icon(), billCategoreDB.getName(), billCategoreDB.getType()));
            }
            billCategoreDaoOperator.insertList(categoreDBS);
        }

    }


}
