package com.kuxuan.moneynote.ui.fragments.report;

import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.ChartData;
import com.kuxuan.moneynote.json.netbody.ChartBody;
import com.kuxuan.moneynote.listener.MVPListener;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public class ReportModel implements ReportContract.RepModel {
    @Override
    public void getDataLists(int type, int statisc_type, final MVPListener<ArrayList<ChartData>> listMVPListener) {
        RetrofitClient.getApiService().getChartData(new ChartBody(type + "", statisc_type + "")).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<ArrayList<ChartData>>>() {
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

}
