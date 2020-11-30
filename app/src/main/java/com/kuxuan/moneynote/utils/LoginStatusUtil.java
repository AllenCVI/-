package com.kuxuan.moneynote.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuxuan.moneynote.MyApplication;
import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.MineJson;
import com.kuxuan.moneynote.json.UserJson;
import com.kuxuan.moneynote.listener.Successlistener;

import java.lang.reflect.Type;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 登录状态工具类
 * Created by xieshengqi on 2017/10/30.
 */

public class LoginStatusUtil {

    /**
     * 判断是否登录
     *
     * @return
     */
    public static boolean isLoginin() {
        return (boolean) SPUtil.get(MyApplication.getInstance(), Constant.UserInfo.ISLOGIN, false);
    }


    public static void loginIn() {
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.UserInfo.ISLOGIN, true);
    }

    public static void loginOut() {
        //用户maxId清零
//        DBMaxIdOpertor.getInstance().inserData(getLoginUserId()+"",0);
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.UserInfo.ISLOGIN, false);
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.UserInfo.TOKEN, "");
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.Time.TOKEN_FAIL, 0L);
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.UserInfo.USER_ID, -1);
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.UserInfo.USER_INFO, "");
//预算初始化
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.System.SWITCH, false);
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.System.BUGET_NUM, Constant.System.NORMAL_NUM);

    }


    public static void setUserInfo(MineJson mineJson) {
        if (mineJson == null) {
            return;
        }
        Gson gson = new Gson();
        String json = gson.toJson(mineJson);
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.UserInfo.USER_INFO, json);
    }

    public static MineJson getUserInfo() {
        Gson gson = new Gson();
        Type tp = new TypeToken<MineJson>() {
        }.getType();
        String json = (String) SPUtil.get(MyApplication.getInstance(), Constant.UserInfo.USER_INFO, "");
        if (json == null) {
            return null;
        }
        MineJson mineJson = null;
        try {
            mineJson = gson.fromJson(json, tp);
        } catch (Exception e) {

        }
        return mineJson;
    }

    public static void setToken(String token) {
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.Time.TOKEN_FAIL, System.currentTimeMillis());
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.UserInfo.TOKEN, token);
        loginIn();
    }

    public static void setUserId(int user_id) {
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.UserInfo.USER_ID, user_id);
    }

    /**
     * 设置token和用户id
     *
     * @param token
     * @param user_id
     */
    public static void setToken(String token, int user_id) {
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.Time.TOKEN_FAIL, System.currentTimeMillis());
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.UserInfo.TOKEN, token);
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.UserInfo.USER_ID, user_id);
        loginIn();
    }

    /**
     * 获取登录的用户id
     *
     * @return
     */
    public static int getLoginUserId() {
        return (Integer) SPUtil.get(MyApplication.getInstance(), Constant.UserInfo.USER_ID, -1);
    }


    /**
     * 获取token时间
     *
     * @return
     */
    public static long getTokenTime() {
        return (long) SPUtil.get(MyApplication.getInstance(), Constant.Time.TOKEN_FAIL, 0l);
    }

    /**
     * 获取token
     *
     * @return
     */
    public static String getToken() {
        if (!isLoginin()) {
            return null;
        } else {
            String token = (String) SPUtil.get(MyApplication.getInstance(), Constant.UserInfo.TOKEN, "");
            if (TextUtils.isEmpty(token)) {
                return null;
            } else {
                return token;
            }
        }

    }

    /**
     * 通过网络获取userid
     */
    public static void getFirstUserId() {

        RetrofitClient.getApiService().getUserId().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<UserJson>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {

            }

            @Override
            public void onSuccess(BaseJson<UserJson> userJsonBaseJson) {
                if (userJsonBaseJson != null) {
                    if (userJsonBaseJson.getData() != null) {
                        setUserId(Integer.parseInt(userJsonBaseJson.getData().getUser_id()));
                    }
                }
            }
        });
    }

    /**
     * 通过网络获取userid
     */
    public static void getFirstUserId(final Successlistener successlistener) {

        RetrofitClient.getApiService().getUserId().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<UserJson>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                successlistener.onFail();

            }

            @Override
            public void onSuccess(BaseJson<UserJson> userJsonBaseJson) {
                if (userJsonBaseJson != null) {
                    if (userJsonBaseJson.getData() != null) {
                        setUserId(Integer.parseInt(userJsonBaseJson.getData().getUser_id()));
                        successlistener.onSuccess();
                    }
                }
            }
        });
    }
}
