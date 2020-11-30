package com.kuxuan.moneynote.utils;

import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.db.UpDataOperator;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.netbody.UpDataBody;
import com.kuxuan.moneynote.listener.UpDBListener;
import com.kuxuan.sqlite.db.CategoryDB;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 上传数据帮助类（上传数据到服务器，更新数据库）
 * Created by xieshengqi on 2018/5/8.
 */

public class UpDBDataOpertor {


    private static UpDBDataOpertor mInstance;

    private CategoryDaoOperator categoryDaoOperator;

    private UpDBDataOpertor() {
        categoryDaoOperator = CategoryDaoOperator.newInstance();
    }

    public static UpDBDataOpertor getInstance() {
        if (mInstance == null) {
            synchronized (UpDBDataOpertor.class) {
                mInstance = new UpDBDataOpertor();
            }
        }
        return mInstance;
    }


    /**
     * 更新数据
     * @param listener
     */
    public void onUpData(UpDBListener listener) {
        upData(listener);
    }


    /**
     * 访问数据库看是不是需要上传数据
     * @param listener
     */
    private void upData(final UpDBListener listener) {
        final ArrayList<CategoryDB> needUpdataJson = categoryDaoOperator.getNeedUpdataJson();
        if (needUpdataJson != null && needUpdataJson.size() != 0) {
            String json = UpDataOperator.getInstance().getJson(needUpdataJson);
            RetrofitClient.getApiService().upData(new UpDataBody(0, json)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
                @Override
                public void onError(ExceptionHandle.ResponeThrowable e) {
                    if (listener != null){
                        listener.onFail();
                        listener.onexitLoginFail(needUpdataJson);
                    }
                }

                @Override
                public void onSuccess(BaseJson<Object> objectBeanNewJson) {
                    //更新数据库
                    updataInDB(needUpdataJson, objectBeanNewJson, listener);

                }
            });
        } else {
            if (listener != null)
                listener.onNoNeedUpdata();
        }
    }


    /**
     * 上传完成更新数据库
     * @param needUpdataJson
     * @param objectBeanNewJson
     * @param listener
     */
    private void updataInDB(final ArrayList<CategoryDB> needUpdataJson, final BaseJson<Object> objectBeanNewJson, final UpDBListener listener) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                if (objectBeanNewJson != null && objectBeanNewJson.getCode() == 0) {
                    for (CategoryDB categoryDB : needUpdataJson) {
                        if (categoryDB.getStatus() == 1) {
                            categoryDaoOperator.deleteData(categoryDB.getBill_id());
                        } else {
                            categoryDaoOperator.updataNoNeedSynce(categoryDB.getBill_id());
                        }
                    }
                } else {
                    for (CategoryDB categoryDB : needUpdataJson) {
                        categoryDaoOperator.updataNeedSynce(categoryDB.getBill_id());
                    }
                }
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
                if (listener != null)
                    listener.onSuccess();
            }
        });

    }

}
