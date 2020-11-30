package com.kuxuan.moneynote.ui.activitys.bill;

import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.BillJson;
import com.kuxuan.moneynote.json.netbody.YearBody;
import com.kuxuan.moneynote.listener.MVPListener;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class BillModel implements BillContract.BillModel {
    private static final String TAG = "BillModel";
    @Override
    public void getBillData(final MVPListener<BillJson> listMVPListener, String year) {
        RetrofitClient.getApiService().getMineBillData(new YearBody(year)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObsever<BaseJson<BillJson>>() {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        listMVPListener.onFail(e.message);

                    }

                    @Override
                    public void onSuccess(BaseJson<BillJson> arrayListBaseJson) {
                        if (arrayListBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                            listMVPListener.onSuccess(arrayListBaseJson.getData());

                        } else {
                            listMVPListener.onFail(arrayListBaseJson.getError().get(0));

                        }
                    }
                });
    }


}
