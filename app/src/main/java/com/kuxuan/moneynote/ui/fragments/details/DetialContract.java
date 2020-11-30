package com.kuxuan.moneynote.ui.fragments.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.kuxuan.moneynote.base.mvpbase.BaseModel;
import com.kuxuan.moneynote.base.mvpbase.BasePresent;
import com.kuxuan.moneynote.base.mvpbase.BaseView;
import com.kuxuan.moneynote.json.TypeDataJson;
import com.kuxuan.moneynote.json.UserAllBillJson;
import com.kuxuan.moneynote.json.netbody.RES;
import com.kuxuan.moneynote.json.netbody.SkinBean;
import com.kuxuan.moneynote.listener.MVPListener;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.HashMap;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public interface DetialContract {


    interface DetialView extends BaseView {


        void showNoLogin();


        void showLogin();

        void showNoData();

        void showHaveData();

        void showNoNetWork();


        void showError();


        void getDataFirst();



        void showYearAndMonth(String year, String month);

        void setScrollForNodata();

        void setScrollData();

        void showContentLayout();

        void finishRefresh();

        void setData(String year, String month, String inmoney, String outMoey);


        void goToEdit(TypeDataJson dayDataEntity);

        void showSkinData(SkinBean skinBean);

        void showDownLoad(RES res, int size, int positon, View background, ProgressBar progressBar, int current);

    }


    public interface DownloadListener {
        void onStart();

        void onProgress(int currentLength);

        void onFinish(String localPath);

        void onFailure();
    }


    interface DetialModel extends BaseModel {

        void getDataLists(String year, String month, MVPListener<UserAllBillJson> listMVPListener);


        void getPopWindowData(MVPListener<SkinBean> listMvpListener);

        void getDataListsForDB(String year,String month, HashMap<String,Integer> maps,MVPListener<UserAllBillJson> listMVPListener);

        void downLoadPic(String url,int size,int position,DownloadListener downloadListener);

    }


    abstract class DetialPresent extends BasePresent<DetialModel, DetialView> {


        abstract void initRecyclerView(Context context, RecyclerView recyclerView);

        abstract void initRefreshLayout(Context context, TwinklingRefreshLayout refreshLayout);

        abstract void initListener(View view);

        abstract void getDataLists(String year, String month);


        abstract void  loginCheck();


        abstract void InitPopWindow();


        abstract void downLoadPic(RES res,View background,ProgressBar progressBar, String url, int size, int position);

    }

}
