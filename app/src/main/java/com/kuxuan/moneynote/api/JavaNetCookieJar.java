package com.kuxuan.moneynote.api;


import com.kuxuan.moneynote.MyApplication;

import java.net.CookieManager;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by yshow_mdj on 2017/2/20.
 */
public class JavaNetCookieJar implements CookieJar {
    private static final String TAG = "CookieManger";
    private static PersistentCookieStore cookieStore;
    private CookieManager cookieManager;

    public JavaNetCookieJar(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
        if (cookieStore == null) {
            cookieStore = new PersistentCookieStore(MyApplication.getInstance());
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }
}
