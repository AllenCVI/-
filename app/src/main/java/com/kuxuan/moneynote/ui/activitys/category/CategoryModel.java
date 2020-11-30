package com.kuxuan.moneynote.ui.activitys.category;

import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.CategoryJson;
import com.kuxuan.moneynote.json.netbody.AllCategoryBody;
import com.kuxuan.moneynote.json.netbody.CategoryBody;
import com.kuxuan.moneynote.json.netbody.CustomCategoryBody;
import com.kuxuan.moneynote.listener.MVPListener;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class CategoryModel implements CategoryContract.CategoryModel {
    @Override
    public void getCategoryList(final MVPListener<CategoryJson> listMVPListener, String type) {
        RetrofitClient.getApiService().getCategoryData(new AllCategoryBody(type)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObsever<BaseJson<CategoryJson>>() {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        listMVPListener.onFail(e.message);

                    }

                    @Override
                    public void onSuccess(BaseJson<CategoryJson> arrayListBaseJson) {
                        if (arrayListBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                            listMVPListener.onSuccess(arrayListBaseJson.getData());

                        } else {
                            listMVPListener.onFail(arrayListBaseJson.getError().get(0));

                        }
                    }
                });
    }

    @Override
    public void removeCategory(final MVPListener<Object> listMVPListener, String category_id, int category_type, String name, String type) {
        if (category_type == 2) {
            //自定义类别删除
            RetrofitClient.getApiService().delCustormCategory(new CustomCategoryBody(name, type)).subscribeOn(Schedulers.io())
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

        } else {
            //系统类别删除
            RetrofitClient.getApiService().delsystemCategory(new CategoryBody(category_id)).subscribeOn(Schedulers.io())
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
//        RetrofitClient.getApiService().removeCategory(new CategoryBody(category_id, category_type)).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new MyObsever<BaseJson<Object>>() {
//                    @Override
//                    public void onError(ExceptionHandle.ResponeThrowable e) {
//                        listMVPListener.onFail(e.message);
//
//                    }
//
//                    @Override
//                    public void onSuccess(BaseJson<Object> arrayListBaseJson) {
//                        if (arrayListBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
//                            listMVPListener.onSuccess(arrayListBaseJson.getData());
//
//                        } else {
//                            listMVPListener.onFail(arrayListBaseJson.getError().get(0));
//
//                        }
//                    }
//                });
    }

    @Override
    public void addCategory(final MVPListener<Object> listMVPListener, String category_id) {
        RetrofitClient.getApiService().addCategory(new CategoryBody(category_id)).subscribeOn(Schedulers.io())
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
    public void addCustomCategory(final MVPListener<Object> listMVPListener, String name, String type) {
        RetrofitClient.getApiService().addCustomCategory(new CustomCategoryBody(name, type)).subscribeOn(Schedulers.io())
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
    public void checkBillCategory(final MVPListener<Boolean> listMVPListener, final int type, final String category_id) {
//        RetrofitClient.getApiService().checkBillByCategoryId(category_id).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new MyObsever<BaseJson<Object>>() {
//                    @Override
//                    public void onError(ExceptionHandle.ResponeThrowable e) {
//                        listMVPListener.onFail(e.message);
//
//                    }
//
//                    @Override
//                    public void onSuccess(BaseJson<Object> arrayListBaseJson) {
//                        if (arrayListBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
//                            listMVPListener.onSuccess(arrayListBaseJson.getData());
//
//                        } else {
//                            listMVPListener.onFail(arrayListBaseJson.getError().get(0));
//
//                        }
//                    }
//                });

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                boolean b = CategoryDaoOperator.newInstance().checkIsHaveTypelists(category_id);
                e.onNext(b);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                listMVPListener.onSuccess(aBoolean);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }
}
