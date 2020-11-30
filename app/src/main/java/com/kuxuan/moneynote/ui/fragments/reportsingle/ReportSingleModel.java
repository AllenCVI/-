package com.kuxuan.moneynote.ui.fragments.reportsingle;

import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.ChartData;
import com.kuxuan.moneynote.json.NewCategoryJson;
import com.kuxuan.moneynote.json.NewChartData;
import com.kuxuan.moneynote.json.netbody.ChartBody;
import com.kuxuan.moneynote.json.netbody.NewChartBody;
import com.kuxuan.moneynote.listener.MVPListener;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public class ReportSingleModel implements ReportSingleContract.RepModel {
    @Override
    public void getDataLists(int type, int statisc_type, int catrogy_id, final MVPListener<ArrayList<ChartData>> listMVPListener) {
        ChartBody chartBody;
        if (catrogy_id == -1)
            chartBody = new ChartBody(type + "", statisc_type + "");
        else
            chartBody = new ChartBody(type + "", statisc_type + "", catrogy_id + "");
        RetrofitClient.getApiService().getChartData(chartBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<ArrayList<ChartData>>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                listMVPListener.onFail(e.message);
            }

            @Override
            public void onSuccess(BaseJson<ArrayList<ChartData>> arrayListBaseJson) {
                if (arrayListBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                    listMVPListener.onSuccess(arrayListBaseJson.getData());
                } else {
                    listMVPListener.onFail(arrayListBaseJson.getError().get(0));
                }
            }
        });
    }

    @Override
    public void getDataCategotyLists(int type, int statisc_type, int catrogy_id, String year, String month, String start_date, String end_date, final MVPListener<NewCategoryJson> listMVPListener) {
        NewChartBody newChartBody;
        if (catrogy_id == -1) {
            newChartBody = new NewChartBody(statisc_type, type, year, month, start_date, end_date, null);
        } else {
            newChartBody = new NewChartBody(statisc_type, type, year, month, start_date, end_date, catrogy_id + "");

        }
        RetrofitClient.getApiService().getChartNewCategoryData(newChartBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<NewCategoryJson>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                listMVPListener.onFail(e.message);
            }

            @Override
            public void onSuccess(BaseJson<NewCategoryJson> newCategoryJsonBaseJson) {
                if (newCategoryJsonBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                    listMVPListener.onSuccess(newCategoryJsonBaseJson.getData());
                } else {
                    listMVPListener.onFail(newCategoryJsonBaseJson.getError().get(0));
                }
            }
        });

    }

    @Override
    public void getNewChartDataLists(int type, int statisc_type, int catrogy_id, final MVPListener<ArrayList<NewChartData>> listMVPListener) {
        ChartBody chartBody;
        if (catrogy_id == -1)
            chartBody = new ChartBody(type + "", statisc_type + "");
        else
            chartBody = new ChartBody(type + "", statisc_type + "", catrogy_id + "");
        RetrofitClient.getApiService().getChartNewData(chartBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<ArrayList<NewChartData>>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                listMVPListener.onFail(e.message);
            }

            @Override
            public void onSuccess(BaseJson<ArrayList<NewChartData>> arrayListBaseJson) {
                if (arrayListBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                    listMVPListener.onSuccess(arrayListBaseJson.getData());
                } else {
                    listMVPListener.onFail(arrayListBaseJson.getError().get(0));
                }
            }
        });
    }

    @Override
    public void getDataCatgoryForDB(final int statistic_type, final int type, final int category_id, final CategoryDaoOperator categoryDaoOperator, final NewChartData newChartData, final MVPListener<NewCategoryJson> listener) {
        Observable<NewCategoryJson> observable = Observable.create(new ObservableOnSubscribe<NewCategoryJson>() {
            @Override
            public void subscribe(ObservableEmitter<NewCategoryJson> e) throws Exception {
                NewCategoryJson categoryJson;
                if (statistic_type == 1) {
                    //周：
                    categoryJson = ReportSingleDBOpertor.getInstance().getWeekNewCategoryJson((ArrayList<String>) newChartData.getTime_range(), categoryDaoOperator, category_id, type);
                } else if (statistic_type == 2) {
                    //月
                    categoryJson = ReportSingleDBOpertor.getInstance().getMonthNewCategoryJson(Integer.parseInt(newChartData.getYear()), Integer.parseInt(newChartData.getMonth()), categoryDaoOperator, type, category_id);
                } else {
                    //年
                    categoryJson = ReportSingleDBOpertor.getInstance().getYearNewCategoryJson(Integer.parseInt(newChartData.getYear()), categoryDaoOperator, type, category_id);
                }
                e.onNext(categoryJson);
                e.onComplete();
            }
        });
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<NewCategoryJson>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(NewCategoryJson newCategoryJson) {
                listener.onSuccess(newCategoryJson);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    @Override
    public void getDataForDB(final HashMap<String, Integer> db_maps, final int statis_type, final int type, final MVPListener<ArrayList<NewChartData>> listener) {
        Observable<ArrayList<NewChartData>> observable = Observable.create(new ObservableOnSubscribe<ArrayList<NewChartData>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<NewChartData>> e) throws Exception {
                ArrayList<NewChartData> weekTabDatas;
                //周
                if (statis_type == 1) {
                    weekTabDatas = ReportSingleDBOpertor.getInstance().getWeekTabData(db_maps, statis_type);
                }//月
                else if (statis_type == 2) {
                    weekTabDatas = ReportSingleDBOpertor.getInstance().getMonthTabData(db_maps, statis_type);
                }
                //年
                else if (statis_type == 3) {
                    weekTabDatas = ReportSingleDBOpertor.getInstance().getYearTabData(db_maps, statis_type);
                } else {
                    weekTabDatas = null;
                }
                e.onNext(weekTabDatas);
            }
        });
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList<NewChartData>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ArrayList<NewChartData> newChartData) {
                listener.onSuccess(newChartData);
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
