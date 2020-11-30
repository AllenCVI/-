package com.kuxuan.moneynote.ui.fragments.mine;

import com.kuxuan.moneynote.base.mvpbase.BaseModel;
import com.kuxuan.moneynote.base.mvpbase.BasePresent;
import com.kuxuan.moneynote.base.mvpbase.BaseView;
import com.kuxuan.moneynote.json.BillJson;
import com.kuxuan.moneynote.json.MineJson;
import com.kuxuan.moneynote.listener.MVPListener;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public interface MineContract {
    interface  MineView extends BaseView {
        /**
         * 设置我的界面的数据
         * @param mineModel
         */
        void setMineData(MineJson mineModel);

        /**
         * 设置我的账单数据
         * @param billJson
         */
        void setMineBillData(BillJson billJson);

        /**
         * 展示错误原因
         * @param msg 原因
         */
        void showErrorMsg(String msg);
    }



    interface MineModel extends BaseModel {
        /**
         * 获取我的个人信息数据
         * @param listMVPListener
         */
        void getMineData(MVPListener<MineJson> listMVPListener);

        /**
         * 获取我的账单数据
         * @param listMVPListener
         */
        void getMineBillData(MVPListener<BillJson> listMVPListener,String year);
    }


    abstract  class MinePresent extends BasePresent<MineModel,MineView> {
        /**
         * 获取我的的数据
         */
         abstract void getMineData();

        /**
         * 获取我的账单数据
         */
        abstract void getMineBill(String year);
    }
}
