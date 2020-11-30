package com.kuxuan.moneynote.ui.fragments.reportsingle;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioGroup;

import com.kuxuan.moneynote.base.mvpbase.BaseModel;
import com.kuxuan.moneynote.base.mvpbase.BasePresent;
import com.kuxuan.moneynote.base.mvpbase.BaseView;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.CategoryDataJson;
import com.kuxuan.moneynote.json.ChartData;
import com.kuxuan.moneynote.json.LineJson;
import com.kuxuan.moneynote.json.NewCategoryJson;
import com.kuxuan.moneynote.json.NewChartData;
import com.kuxuan.moneynote.json.TypeDataJson;
import com.kuxuan.moneynote.listener.MVPListener;
import com.kuxuan.moneynote.ui.weight.ChartLayout;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public interface ReportSingleContract {


    interface RepView extends BaseView {


        void showNoData();

        void showNoDBdata();

        void showNoLogin();

        void showContentLayout();

        void showData();

        void showNoNetWork();

        void showLoadding();


        void showError();

        /**
         * 设置折线图的数据
         * @param data
         */
        void setLineData(ArrayList<LineJson> data);

        /**
         * 设置圆环的数据
         * @param data
         */
        void setCircleData(ArrayList<CategoryDataJson> data);

        void showErrorMsg(String msg);

        /**
         * 点击radiobtn时调用，周月年
         * @param statisc_type
         */
        void getRadioData(int statisc_type);

        /**
         * 获取数据设置
         * @param chartNewData
         * @param newCategoryJson
         */
        void setChartNewData(NewChartData chartNewData,NewCategoryJson newCategoryJson);


        void getDataFirst();

        void setCharData(ChartData charData);

        void refreshData(ArrayList<ChartData> chartDatas);

        void refreshNewData(ArrayList<NewChartData> chartDatas);

        /**
         * 跳转编辑页
         * @param typeDataJson
         */
        void goToEdit(TypeDataJson typeDataJson);

        /**
         * 跳转详情页（同一种fragment）
         * @param catrogy_id
         */
        void goToCatrogyDetial(int catrogy_id);
    }


    interface RepModel extends BaseModel {


        void getDataLists(int type, int statisc_type, int catrogy_id, MVPListener<ArrayList<ChartData>> listMVPListener);

        /**
         * 获取单个数据
         *
         * @param type            1 收入 2 支出
         * @param statisc_type    1 周 2 月 3 年
         * @param catrogy_id      当点击详情的时候 为必传
         * @param year            当 statistic_type 为 2，3 必传 例如 2018
         * @param month           当 statistic_type 为 2 必传 例如 8 或 10
         * @param listMVPlistener
         */
        void getDataCategotyLists(int type, int statisc_type, int catrogy_id, String year, String month, String start_date, String end_date, MVPListener<NewCategoryJson> listMVPlistener);


        /**
         * 获取时间段
         *
         * @param type
         * @param statisc_type
         * @param catrogy_id
         * @param listMVPListener
         */
        void getNewChartDataLists(int type, int statisc_type, int catrogy_id, MVPListener<ArrayList<NewChartData>> listMVPListener);


        /**
         * 从数据库中获取数据
         * @param statistic_type
         * @param newChartData
         */
   void getDataCatgoryForDB(int statistic_type,int type,int category_id, CategoryDaoOperator categoryDaoOperator,NewChartData newChartData,MVPListener<NewCategoryJson> listener);

        /**
         * 从数据库中获取tab
         * @param maps
         * @param statistic_type
         */
   void getDataForDB(HashMap<String,Integer> maps, int statistic_type, int type, MVPListener<ArrayList<NewChartData>> listMVPListener);
    }


    abstract class RepPresent extends BasePresent<RepModel, RepView> {
        abstract void initRadioGroup(RadioGroup radioGroup);

        abstract void initTabLayout(Context context, TabLayout tabLayout);


        abstract void setTabLayoutData(ArrayList<String> data);

        abstract void changeChart(int type);

        abstract void changeData(int type, int statis_type, int catrogy_id);

        abstract boolean jugeNeedGetData(int type, int statis_type);


        abstract void refreshTabLayout(TabLayout tabLayout, int type, String title, ArrayList<ChartData> datas);

        abstract void clearMap();


        abstract void setChartData(ChartLayout chartLayout, int type, ArrayList<LineJson> datas);

        abstract void initRecyclerView(Context context, RecyclerView recyclerView);

        //旧版数据（不使用）
        abstract void setLineData(ChartData chartData);

        //新数据(使用)
        abstract void setLineData(NewChartData chartData,NewCategoryJson newCategoryJson);
        //旧版数据（不使用）
        abstract void setCircleData(ChartData chartData);
        //新数据(使用)
        abstract void setCircleData(NewChartData chartData,NewCategoryJson newCategoryJson);

        abstract void loginCheck();
        ///新数据(使用)
        abstract void refreshNewTabLayout(Context context,TabLayout tabLayout, int type, String title, ArrayList<NewChartData> datas);
    }

}
