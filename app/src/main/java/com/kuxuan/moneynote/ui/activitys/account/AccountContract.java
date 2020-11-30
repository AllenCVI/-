package com.kuxuan.moneynote.ui.activitys.account;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.kuxuan.moneynote.base.mvpbase.BaseModel;
import com.kuxuan.moneynote.base.mvpbase.BasePresent;
import com.kuxuan.moneynote.base.mvpbase.BaseView;
import com.kuxuan.moneynote.json.CategoryJson;
import com.kuxuan.moneynote.json.netbody.BillBody;
import com.kuxuan.moneynote.listener.MVPListener;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public interface AccountContract {

    interface  AccountView extends BaseView {
        /**
         * 获取成功
         */
        void getCategorySuccess();

        void finishActivity();

        void setResultData(BillBody billBody);

        void showMsg(String msg);

    }



    interface AccountModel extends BaseModel {
        /**
         * 获取类别列表
         */
        void getCategoryList(MVPListener<CategoryJson> listMVPListener, String type);

        /**
         * 获取没有登录的类别列表
         * @param listMVPListener
         * @param type
         */
        void getNoLoginCategoryList(MVPListener<CategoryJson> listMVPListener, String type);

        /**
         * 添加账单
         * @param listMVPListener
         * @param billBody
         */
        void addAccount(MVPListener<Object> listMVPListener, BillBody billBody);

        /**
         * 修改账单
         * @param listMVPListener
         * @param billBody
         */
        void uploadBill(MVPListener<Object> listMVPListener, BillBody billBody);

        /**
         * 数据库修改账单
         * @param listMVPListener
         * @param billBody
         * @param name
         * @param bill_id
         */
        void uploadBillForDB(MVPListener<Object> listMVPListener,BillBody billBody,String name,String bill_id);


        /**
         * 添加账单数据库
         * @param billBody
         */
         void addAccountForDB(MVPListener<Object> listMVPListener,BillBody billBody,String name);


    }


    abstract  class  AccountPresent extends BasePresent<AccountModel,AccountView> {
        /**
         *
         * @param context
         * @param recyclerView
         * @param type   判断支出还是收入
         */
        protected abstract void initRecyclerView(Context context, RecyclerView recyclerView,int type,AccountPresenter.ClickListener mClickListener);
        /**
         * 获取类别列表
         * @param type  支出/收入
         */
        abstract void getCategoryList(String type);
        /**
         * 获取没有登录的类别列表
         * @param type
         */
        abstract void getNoLoginCategoryList( String type);
        /**
         * 添加账单
         * @param billBody
         */
        abstract void addAccount(BillBody billBody,int online_OR_Offline,String name);



        /**
         * 修改账单
         * @param billBody
         * @param bill_id 账单id
         */
        abstract void uploadBill(BillBody billBody,int online_OR_Offline,String name,String bill_id);


    }
}
