package com.kuxuan.moneynote.ui.activitys.bill;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.kuxuan.moneynote.base.mvpbase.BaseModel;
import com.kuxuan.moneynote.base.mvpbase.BasePresent;
import com.kuxuan.moneynote.base.mvpbase.BaseView;
import com.kuxuan.moneynote.json.BillJson;
import com.kuxuan.moneynote.json.BillJsonList;
import com.kuxuan.moneynote.json.Time;
import com.kuxuan.moneynote.listener.MVPListener;

import java.util.List;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public interface BillContract {
    interface  BillView extends BaseView {
        /**
         * 设置账单数据
         * @param billJson
         */
        void setBillData(BillJson billJson);


        /**
         * 设置账单数据
         */
        void setData();


        void setOffLineBillData(List<BillJsonList> billJsonLists);
    }



    interface BillModel extends BaseModel {
        /**
         * 获取我的账单数据
         * @param listMVPListener
         * @param year 年
         */
        void getBillData(MVPListener<BillJson> listMVPListener, String year);
    }


    abstract  class  BillPresent extends BasePresent<BillModel,BillView> {
        abstract void initRecyclerView(Context context, RecyclerView recyclerView);
        abstract void addData(Time time);
        abstract void getBillData(int year);

        abstract void getOfflineData(int year);

    }

}
