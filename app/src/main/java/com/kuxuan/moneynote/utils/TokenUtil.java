package com.kuxuan.moneynote.utils;

import com.google.gson.Gson;
import com.kuxuan.moneynote.api.ApiService;
import com.kuxuan.moneynote.api.MyGson;
import com.kuxuan.moneynote.api.MyOkClient;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.listener.TokenListener;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xieshengqi on 2018/5/3.
 */

public class TokenUtil {


    /**
     * 获取新token
     *
     * @param listener
     */
    public static void getNewToken(final TokenListener listener) {
        final Gson gson = MyGson.get();
        OkHttpClient client = MyOkClient.getTokenOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.TOKEN_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getNewToken().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<BaseJson<Object>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseJson<Object> o) {
                if (o != null && o.getCode() == 0) {
                    Gson gson1 = new Gson();
                    String json = gson1.toJson(o.getData());
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        String token = jsonObject.getString("token");
                        if (token != null) {
                            LoginStatusUtil.setToken(token);
                            listener.onSuccess();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFail();
                    }
                } else {
                    listener.onFail();
                }
            }

            @Override
            public void onError(Throwable e) {
                listener.onFail();

            }

            @Override
            public void onComplete() {

            }
        });

    }
}
