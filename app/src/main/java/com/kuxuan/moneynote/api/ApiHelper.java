package com.kuxuan.moneynote.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * ApiHelper
 */
public class ApiHelper {

    private static final String TAG = "ApiHelper";

    private static ApiHelper mInstance;
    private Retrofit mRetrofit;
    private OkHttpClient mHttpClient;

    private ApiHelper() {
        this( 30, 30, 30);
    }

    public ApiHelper( int connTimeout, int readTimeout, int writeTimeout) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS);

        mHttpClient = builder.build();
    }

    public static ApiHelper getInstance() {
        if (mInstance == null) {
            mInstance = new ApiHelper();
        }

        return mInstance;
    }

    public ApiHelper buildRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .client(mHttpClient)
                .baseUrl("http://182.92.118.1:8090")
                .build();
        return this;
    }

    public <T> T createService(Class<T> serviceClass) {
        return mRetrofit.create(serviceClass);
    }

}
