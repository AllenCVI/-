package com.kuxuan.moneynote.api;

import android.support.annotation.NonNull;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by roy on 16/7/11.
 */
public class MyOkClient {
    static OkHttpClient client;
    static OkHttpClient tokenclient;

    @NonNull
    public static OkHttpClient getOkHttpClient() {
        if (client != null) {
            return client;
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        if (AppConstants.sDebug) {
            // Log信息拦截器
  //          HttpLoggingInterceptor loggingInterceptorhead = new HttpLoggingInterceptor();
  //          loggingInterceptorhead.setLevel(HttpLoggingInterceptor.Level.BODY);
//            MyHttpLoggingInterceptor loggingInterceptorhead = new MyHttpLoggingInterceptor();
            HttpLoggingInterceptor loggingInterceptorhead = new HttpLoggingInterceptor();
            loggingInterceptorhead.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptorhead);
            builder.addInterceptor(new QueryParameterInterceptor());
            builder.addInterceptor(new TokenInterceptor());
//        }
        //设置cookie
        CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));
        //公共参数
//        builder.addInterceptor(new QueryParameterInterceptor());
        //Gzip压缩
//        builder.addInterceptor(new GzipRequestInterceptor());
        //开启缓存
//        File cacheFile = new File(RockContextUtils.getApplicationContext().getExternalCacheDir(), "appCache");
//        Cache cache = new Cache(cacheFile, 1024 * 1024 * 30);
//        builder.cache(cache).addInterceptor(new CacheInterceptor());
        //开启facebook sDebug
//        builder.addNetworkInterceptor(new StethoInterceptor());
        //设置超时
        builder.connectTimeout(50, TimeUnit.SECONDS);
        builder.readTimeout(50, TimeUnit.SECONDS);
        builder.writeTimeout(50, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        client = builder.build();
        return client;
    }


    @NonNull
    public static OkHttpClient getTokenOkHttpClient() {
        if (tokenclient != null) {
            return tokenclient;
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        if (AppConstants.sDebug) {
        // Log信息拦截器
        //          HttpLoggingInterceptor loggingInterceptorhead = new HttpLoggingInterceptor();
        //          loggingInterceptorhead.setLevel(HttpLoggingInterceptor.Level.BODY);
//            MyHttpLoggingInterceptor loggingInterceptorhead = new MyHttpLoggingInterceptor();
        HttpLoggingInterceptor loggingInterceptorhead = new HttpLoggingInterceptor();
        loggingInterceptorhead.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptorhead);
        builder.addInterceptor(new TokenParamsInterceptor());
//        }
        //设置cookie
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));
        //公共参数
//        builder.addInterceptor(new QueryParameterInterceptor());
        //Gzip压缩
//        builder.addInterceptor(new GzipRequestInterceptor());
        //开启缓存
//        File cacheFile = new File(RockContextUtils.getApplicationContext().getExternalCacheDir(), "appCache");
//        Cache cache = new Cache(cacheFile, 1024 * 1024 * 30);
//        builder.cache(cache).addInterceptor(new CacheInterceptor());
        //开启facebook sDebug
//        builder.addNetworkInterceptor(new StethoInterceptor());
        //设置超时
        builder.connectTimeout(50, TimeUnit.SECONDS);
        builder.readTimeout(50, TimeUnit.SECONDS);
        builder.writeTimeout(50, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(false);
        tokenclient = builder.build();
        return tokenclient;
    }
}
