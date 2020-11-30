package com.kuxuan.moneynote.ui.activitys.exportbill;

import com.kuxuan.moneynote.base.mvpbase.BaseModel;
import com.kuxuan.moneynote.base.mvpbase.BasePresent;
import com.kuxuan.moneynote.base.mvpbase.BaseView;
import com.kuxuan.moneynote.json.netbody.ExportBill;
import com.kuxuan.moneynote.listener.MVPListener;

/**
 * Created by Allence on 2018/3/15 0015.
 */

public interface Contract_ExpoetBill {


    interface ExportBillView extends BaseView{


        void setSuccessExportBillView();

        void setFaildExportBillView();

    }


    interface ExportBillModel extends BaseModel{

        void exportBill(String email,String startTime,String endTime,String time,final MVPListener<ExportBill> listMVPListener);

    }


    abstract class ExportBillPresenter extends BasePresent<ExportBillModel,ExportBillView>{

        void exportBill(String email,String startTime,String endTime,String time){}


    }





}
