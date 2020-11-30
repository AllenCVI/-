package com.kuxuan.moneynote.ui.activitys.exportbill;

import com.kuxuan.moneynote.json.netbody.ExportBill;
import com.kuxuan.moneynote.listener.MVPListener;

/**
 * Created by Allence on 2018/3/15 0015.
 */

public class Presenter_ExportBill extends Contract_ExpoetBill.ExportBillPresenter{


    @Override
    void exportBill(String email,String startTime,String endTime,String time) {


        mModel.exportBill(email,startTime,endTime,time, new MVPListener<ExportBill>() {

            @Override
            public void onSuccess(ExportBill content) {

                view.setSuccessExportBillView();

            }

            @Override
            public void onFail(String msg) {

                view.setFaildExportBillView();

            }

        });





    }
}
