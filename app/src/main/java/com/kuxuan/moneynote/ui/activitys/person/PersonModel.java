package com.kuxuan.moneynote.ui.activitys.person;

import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.MineJson;
import com.kuxuan.moneynote.json.netbody.PersonBody;
import com.kuxuan.moneynote.listener.MVPListener;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class PersonModel implements PersonContract.PersonModel {
    @Override
    public void getMineData(final MVPListener<MineJson> listMVPListener) {
        RetrofitClient.getApiService().getMineData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObsever<BaseJson<MineJson>>() {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        listMVPListener.onFail(e.message);
                    }

                    @Override
                    public void onSuccess(BaseJson<MineJson> arrayListBaseJson) {
                        if (arrayListBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                            listMVPListener.onSuccess(arrayListBaseJson.getData());
                        } else {
                            listMVPListener.onFail(arrayListBaseJson.getError().get(0));
                        }
                    }
                });
    }

    @Override
    public void updateAvatar(final MVPListener<Object> listMVPListener, PersonBody personBody) {


        MultipartBody.Part  avatar= RetrofitClient.getMultiPart("avatar","multipart/form-data",new File(personBody.getAvatar()));

        RetrofitClient.getApiService().updateAvatar(avatar).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObsever<BaseJson<Object>>() {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        listMVPListener.onFail(e.message);
                    }

                    @Override
                    public void onSuccess(BaseJson<Object> arrayListBaseJson) {
                        if (arrayListBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                            listMVPListener.onSuccess(arrayListBaseJson.getData());
                        } else {
                            listMVPListener.onFail(arrayListBaseJson.getError().get(0));
                        }
                    }
                });
    }

    @Override
    public void updatePersonData(final MVPListener<Object> listMVPListener, PersonBody personBody) {
        RetrofitClient.getApiService().updatePersonData(personBody).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObsever<BaseJson<Object>>() {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        listMVPListener.onFail(e.message);
                    }

                    @Override
                    public void onSuccess(BaseJson<Object> arrayListBaseJson) {
                        if (arrayListBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                            listMVPListener.onSuccess(arrayListBaseJson.getData());
                        } else {
                            listMVPListener.onFail(arrayListBaseJson.getError().get(0));
                        }
                    }
                });
    }


}
