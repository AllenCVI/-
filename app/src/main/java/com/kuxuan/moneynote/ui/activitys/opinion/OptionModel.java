package com.kuxuan.moneynote.ui.activitys.opinion;

import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.netbody.MessageBody;
import com.kuxuan.moneynote.listener.MVPListener;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class OptionModel implements OptionContract.OptionModel {


    @Override
    public void sendMessage(final MVPListener<Object> listMVPListener, String message) {
        RetrofitClient.getApiService().addUserMessage(new MessageBody(message)).subscribeOn(Schedulers.io())
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
