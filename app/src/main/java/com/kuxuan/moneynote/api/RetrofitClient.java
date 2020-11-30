package com.kuxuan.moneynote.api;

import com.google.gson.Gson;
import com.kuxuan.moneynote.common.Constant;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络访问
 */
public class RetrofitClient {
    public static Retrofit getRetrofit() {
        Gson gson = MyGson.get();
        OkHttpClient client = MyOkClient.getOkHttpClient();
        return new Retrofit.Builder().baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)

                .build();
    }

    public static <T> T createApi(Class<T> clazz, Retrofit retrofit) {
        return retrofit.create(clazz);
    }

    private static ApiService mCCApiService;

    public static ApiService getApiService() {
        if (mCCApiService == null) {
            mCCApiService = createApi(ApiService.class, getRetrofit());
        }
        return mCCApiService;
    }

    /**
     * 文件body
     * @param key
     * @param mediaType
     * @param file
     * @return
     */
    public static MultipartBody.Part getMultiPart(String key, String mediaType, File file) {
        MultipartBody.Part body = MultipartBody.Part.createFormData(key, file.getName(),
                RequestBody.create(MediaType.parse(mediaType), file));
        return body;
    }

    public static RequestBody getRequestBody(String value){
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }
    public static MultipartBody.Builder getMultiBuilder(HashMap<String, File> map) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Set<String> set = map.keySet();
        for (String key : set) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), map.get(key));
            builder.addFormDataPart(key, map.get(key).getName(), requestFile);
        }
        return builder;
    }
}
