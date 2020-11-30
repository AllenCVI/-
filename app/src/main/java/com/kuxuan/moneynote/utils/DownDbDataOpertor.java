package com.kuxuan.moneynote.utils;

import com.kuxuan.moneynote.MyApplication;
import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.db.BillCategoreDaoOperator;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.UploadBeanJson;
import com.kuxuan.moneynote.json.UploadDbjson;
import com.kuxuan.moneynote.listener.DownDbListener;
import com.kuxuan.sqlite.db.CategoryDB;

import java.util.ArrayList;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 下载数据帮助类（下载数据存入数据库）
 * Created by xieshengqi on 2018/4/27.
 */

public class DownDbDataOpertor {


    private static DownDbDataOpertor mInstance;

    private CategoryDaoOperator categoryDaoOperator;

    private DownDbDataOpertor() {
        categoryDaoOperator = CategoryDaoOperator.newInstance();
    }

    public static DownDbDataOpertor getInstance() {
        if (mInstance == null) {
            synchronized (DownDbDataOpertor.class) {
                mInstance = new DownDbDataOpertor();
            }
        }
        return mInstance;
    }


    private void insertDb(ArrayList<UploadDbjson> uploadDbjsons) {
        if (uploadDbjsons == null) {
            return;
        }
        CategoryDaoOperator categoryDaoOperator = CategoryDaoOperator.newInstance();
        Integer id = (Integer) SPUtil.get(MyApplication.getInstance(), Constant.DbInfo.DB_ID_COUNT, 0);
        ArrayList<CategoryDB> categoryDBS = new ArrayList<>();
        for (UploadDbjson uploadDbjson : uploadDbjsons) {
            long time = uploadDbjson.getTime() * 1000;
            long create_time = uploadDbjson.getCreated_at() * 1000;
            long updata_time = uploadDbjson.getUpdated_at() * 1000;
            if (create_time == 0) {
                create_time = uploadDbjson.getTime();
            }
            if (updata_time == 0) {
                updata_time = time;
            }
            String data = TimeUtlis.getData(time);
            String[] split = data.split("-");
            id++;
//            CategoryDB categoryDB = new CategoryDB((long) id, uploadDbjson.getIdentification(), uploadDbjson.getDemo(), uploadDbjson.getCategory_name(), uploadDbjson.getType(), BillCategoreDaoOperator.newInstance().getDetaillIconUrlById(uploadDbjson.getCategory_id()), Double.parseDouble(uploadDbjson.getAccount()), uploadDbjson.getCategory_id(), Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), create_time, updata_time, uploadDbjson.getStatus(), uploadDbjson.getUser_id(), false);
//            categoryDBS.add(categoryDB);
            categoryDaoOperator.insert((long) id, uploadDbjson.getIdentification(), uploadDbjson.getDemo(), uploadDbjson.getCategory_name(), uploadDbjson.getType(), BillCategoreDaoOperator.newInstance().getDetaillIconUrlById(uploadDbjson.getCategory_id()), Double.parseDouble(uploadDbjson.getAccount()), uploadDbjson.getCategory_id(), Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), create_time, updata_time, time, uploadDbjson.getStatus(), uploadDbjson.getUser_id(), false);
//            categoryDaoOperator.insert(categoryDBS);
        }

//        Log.e("主键id",id+"");
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.DbInfo.DB_ID_COUNT, id);

    }


    public void syncForThread(DownDbListener listener) {
        if (listener != null)
            listener.onStart();
        int index = DBMaxIdOpertor.getInstance().getMaxId(LoginStatusUtil.getLoginUserId() + "");
        syncDataForThread(index, listener);
    }

    /**
     * 同步数据
     */
    private void syncDataForThread(final int page, final DownDbListener listener) {
        RetrofitClient.getApiService().getDownLoadData(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<UploadBeanJson>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                if (listener != null)
                    listener.onFail();
            }

            @Override
            public void onSuccess(final BaseJson<UploadBeanJson> objectBaseJson) {
                if (objectBaseJson != null && objectBaseJson.getCode() == 0) {
                    final UploadBeanJson res = objectBaseJson.getData();
                    if (res != null) {
                        final int number = res.getNumber();
                        if (res.getData() != null) {
                            io.reactivex.Observable.create(new ObservableOnSubscribe<Integer>() {
                                @Override
                                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                                    insertDb(objectBaseJson.getData().getData());
                                    if (res.getData().size() < number) {
                                        //结束
//                                        SPUtil.putAndApply(MyApplication.getInstance(), Constant.DbInfo.DB_DOWNLOAD_INDEX, res.getMaxId());
                                        DBMaxIdOpertor.getInstance().inserData(LoginStatusUtil.getLoginUserId() + "", res.getMaxId());
                                        e.onComplete();
                                    } else {
                                        e.onNext(res.getMaxId());
                                    }
                                }
                            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Integer o) {
                                    syncDataForThread(o, listener);


                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (listener != null)
                                        listener.onFail();
                                }

                                @Override
                                public void onComplete() {

                                    if (listener != null)
                                        listener.onSuccess();
                                }
                            });
                        } else {
                            if (listener != null)
                                listener.onSuccess();
                        }

                    } else {
                        DBMaxIdOpertor.getInstance().inserData(LoginStatusUtil.getLoginUserId() + "", res.getMaxId());

                        if (listener != null)
                            listener.onSuccess();
                    }
                } else {
                    //完全没有数据的时候
                    if (listener != null)
                        listener.onSuccess();
                }

            }
        });
    }

}
