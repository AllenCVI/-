package com.kuxuan.moneynote.api;

import android.text.TextUtils;

import com.kuxuan.moneynote.BuildConfig;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.Md5Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by roy on 16/6/27.
 */
public class QueryParameterInterceptor implements Interceptor {
    //        private String token ="ReyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjE0MzkxMWNjNjBiNz" +
//            "E0MmI1ZTYwNTIyM2EyYWQ2MGNlNGJkZGMzODU0NTlmOWFjOTI4OTI1MjgyNDNhM2EyOTBmNGZhNm" +
//            "E5OTk4ZTFlNjEzIn0.eyJhdWQiOiIzIiwianRpIjoiMTQzOTExY2M2MGI3MTQyYjVlNjA1MjIzYT" +
//            "JhZDYwY2U0YmRkYzM4NTQ1OWY5YWM5Mjg5MjUyODI0M2EzYTI5MGY0ZmE2YTk5OThlMWU2MTMiLC" +
//            "JpYXQiOjE1MDkwMDA0OTEsIm5iZiI6MTUwOTAwMDQ5MSwiZXhwIjoxNTQwNTM2NDkxLCJzdWIiOi" +
//            "IxNCIsInNjb3BlcyI6W119.ocBsSif7wVSiCkQ1y5JGFXAkEphyP3L68EfjWQ0S9as3Fm4CfMks7" +
//            "zlHY8ZovsYlPNa0I1jJMX0SL2FYMFq3DW-QTvvaBJPgNiZT4w2XLYRDSwCg1pWhfUq6rM0krsWjs" +
//            "36QH7G2z8SXhyEZm-62QCWrzr9MnsxpAS9ImYbQEjC1lAdTKi48N-MPTa7q46pR4geTqLAh5lgSx" +
//            "ZeR4dTZa7VAVF4hhjae8RTDtiga7HYHgMWLB5e8ysgcjnkdP-gKg0DfscKe0PczgkASADDMZK3Om" +
//            "U2p5yQDnVSHiTnHWNlN04EWpOsm7KK5fb7wX_sQiKGgKVYI80Akya07cipkxEZ7YwrUKhbCcdVLd" +
//            "uBgU3EiR_MQ8XH5HlJNZweMqNWWz_QpoA5wmYZ9Y0Ac73CHyGuC5kRu8kIn2HdpQVQdXGahzzufk" +
//            "MfQTRUGJ19h32HNRGIvd4j8AfU5AScl6aLfY17CM45ntYnAEOBysoYG8yWBskccLqBufVecAOtzW" +
//            "ktuZ9Isgnou4UUS1B8MV4JVSvytJNzr2tR-hQIWyXiGtVoRv1EdDbx_xAOHAZBf8mn_uKs6fjzBT" +
//            "3YdeLHEFAWfmsT241WXias2OVPNjO7hp-tR6nHNST2wckjyU-KzkF5M6RZd5K58f88btFRmjXV-M" +
//            "Zk2YUJ9stCqm4VTB8CqtoY";
    private static final Charset UTF8 = Charset.forName("UTF-8");

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
            builder.addHeader("token", token);
//            }
        }
//        else {
        try {   //没登录的时候需要校验
            Buffer buffer = new Buffer();
            RequestBody requestBody = originalRequest.body();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            String string = null;
            StringBuffer stringBuffer = new StringBuffer();
            if (charset != null) {
                string = buffer.readString(charset);
                ArrayList<String> list = getSignDataLists(string);
                for (int i = 0; i < list.size(); i++) {
                    if (i != list.size() - 1)
                        stringBuffer.append(list.get(i) + "&");
                    else
                        stringBuffer.append(list.get(i));
                }
            }
//                HashMap<String, String> signData = getSignData(string);
//                Set<String> strings = signData.keySet();
//                Iterator<String> iterator = strings.iterator();
//                while (iterator.hasNext()) {
//                    String next = iterator.next();
//                    list.add(next + "=" + signData.get(next));
//                }

//                for (int i = list.size() - 1; i >= 0; i--) {
//                    if (i != 0)
//                        stringBuffer.append(list.get(i) + "&");
//                    else
//                        stringBuffer.append(list.get(i));
//                }

            String timeda = System.currentTimeMillis() / 1000 + "";
            if (stringBuffer.toString().length() == 0) {
                stringBuffer.append("time=" + timeda);
            } else {
                stringBuffer.append("&time=" + timeda);
            }
            stringBuffer.append("3d0e4b94e04b8dcca1cedb9c33051173");
            String si = Md5Util.md5(stringBuffer.toString());
            builder.addHeader("sign", si);
            builder.addHeader("time", timeda);
        } catch (Exception e) {

        }

//        }
        builder.addHeader("Accept", "application/json");
        //渠道号
        builder.addHeader("cid", BuildConfig.CHANNLE + "");
        builder.addHeader("version", BuildConfig.VERSION_NAME + "");


        builder.url(modifiedUrl);
        request = builder.build();
        return chain.proceed(request);
    }


    private HashMap<String, String> getSignData(String json) throws JSONException {
        HashMap<String, String> map = new HashMap<>();
        JSONObject jsonObject = new JSONObject(json);
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            map.put(next, jsonObject.getString(next));
        }
        return map;
    }

    private ArrayList<String> getSignDataLists(String json) {

        ArrayList<String> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                list.add(next + "=" + jsonObject.getString(next));
            }
        } catch (Exception e) {

        }

        return list;
    }
}
