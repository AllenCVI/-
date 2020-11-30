package com.kuxuan.moneynote.api;

import android.text.TextUtils;

import com.kuxuan.moneynote.BuildConfig;
import com.kuxuan.moneynote.utils.LoginStatusUtil;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xieshengqi on 2018/5/3.
 */

public class TokenParamsInterceptor implements Interceptor {
//    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6Ijg1ZWZjZWJiMWJhYjI1ZGU5ZWI1MDMwNWJjZmJlZTAxM2I5YzA2ZWE2YzllM2JiZDA3MGRlNjdjZmI3ZGYwNDY1NDI0MTBkYzE1Njk1OTBmIn0.eyJhdWQiOiIzIiwianRpIjoiODVlZmNlYmIxYmFiMjVkZTllYjUwMzA1YmNmYmVlMDEzYjljMDZlYTZjOWUzYmJkMDcwZGU2N2NmYjdkZjA0NjU0MjQxMGRjMTU2OTU5MGYiLCJpYXQiOjE1MjUzMzUzNjAsIm5iZiI6MTUyNTMzNTM2MCwiZXhwIjoxNTU2ODcxMzYwLCJzdWIiOiIxMTg1NTIzMyIsInNjb3BlcyI6W119.iRPOYB-4pLb3s0RWVYww8t-S7voXWgrODT7RYu4iSZgp9zwF0AIyr0hPva1fiZAZT1QE2DzSlmSDQN67PIoLa2DdZxyNTyKsmRzCOhw00KtzR7Ruvg0dj3ebPXy7gKwTiiNTR73ScthBN5Zds00XUkE2o70R6z1zXu_jQ3BqnBra_qL3uHebse08fINxaATEoKqJUTDlfEGW2trmmjpQ-UT-YE7S5W4D4rcQZZGWwvDSyTIW6l2ipcAtrFl1UbdZwYyvYBHHFchHzWOU76nY3k_XNnYrpnXmOOigVgedh061THrrhnFcyJBrAhp0uFWYJbXrthX4KjENqrMsVbaXiGKw5I3vbx8wvdlKbjhw95iLZ5vTcyMCnVMy2JYBLi8vh8ZoX8pfGZpxv7LcHnwESy-6S89XpOdNr_yK3xos3Xk_x3Y2pp-fH-1jxCAhbkLuXqU-emsKiO5tCzTJ-leoz1GczdX2ZJKthjnu8v3VITIxQjYnnZievWKhoQR8HT00AceQHmIKY3Jg1B4Y4ctyNrTCkE8GnrUVW10BwZ9E99cdt4G8uqfzmMyIHF2OHNaVCqnaWFCgZj3qX1WRTigClX2MBKPLRLTiPHGrcX64x2Pts0UrSnmlOyux6bI5PxHGq-u8aK6oDJfuSUu_M6Yw19ZKcQ8I7nJzPbUPaPDv4tE";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request request;
        String method = originalRequest.method();
        Headers headers = originalRequest.headers();
        HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                .build();
        //new builder
        Request.Builder builder = originalRequest.newBuilder();

        String token = LoginStatusUtil.getToken();
        long tokenTime = LoginStatusUtil.getTokenTime();
        long currentTime = System.currentTimeMillis();
        if (!TextUtils.isEmpty(token)) {
//            if (tokenTime != 0) {
//                if (currentTime - tokenTime > Constant.TOKEN_FAIL_DAY) {
//                    //token失效(随便截取一个数)
//                    builder.addHeader("Authorization", "Bearer " + token.substring(0, 2));
//                } else {
//                    builder.addHeader("Authorization", "Bearer " + token);
//                }
//            } else {
//            builder.addHeader("Authorization", "Bearer " + token);
            builder.addHeader("Authorization", "Bearer " + token);
//            }
        } else {
            //没登录的时候需要校验
//            String systemtime = System.currentTimeMillis() / 1000 + "";
//            String sign = "time=" + systemtime + "b693013c19222873eece0526b7b85da2";
//            builder.addHeader("sign", Md5Util.md5(sign));
//            builder.addHeader("time", systemtime);
        }
        builder.addHeader("Accept", "application/json");
        //渠道号
        builder.addHeader("cid", BuildConfig.CHANNLE + "");
        builder.addHeader("version", BuildConfig.VERSION_NAME + "");


        builder.url(modifiedUrl);
        request = builder.build();
        return chain.proceed(request);
    }
}
