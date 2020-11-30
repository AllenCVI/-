package com.kuxuan.moneynote.ui.fragments.report;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.kuxuan.moneynote.base.mvpbase.BaseModel;
import com.kuxuan.moneynote.base.mvpbase.BasePresent;
import com.kuxuan.moneynote.base.mvpbase.BaseView;
import com.kuxuan.moneynote.json.ChartData;
import com.kuxuan.moneynote.listener.MVPListener;

import java.util.ArrayList;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public interface ReportContract {


    interface RepView extends BaseView {


        void showNoLogin();


        void showNoNetWork();


        void showError();


        void showErrorMsg(String msg);

        void getRadioData(int statisc_type);

        void refreshData(ArrayList<ChartData> chartDatas);

    }


    interface RepModel extends BaseModel {


        void getDataLists(int type ,int statisc_type, MVPListener<ArrayList<ChartData>> listMVPListener );
    }


    abstract class RepPresent extends BasePresent<RepModel, RepView> {
        abstract void initRadioGroup(RadioGroup radioGroup);

        abstract void initTabLayout(Context context, TabLayout tabLayout);

        abstract void initViewPagerTablyout(TabLayout tabLayout, ViewPager viewPager,int type);

        abstract void setTabLayoutData(ArrayList<String> data);

        abstract void changeChart(int type);

        abstract void changeData(int type,int statis_type);

        abstract boolean jugeNeedGetData(int type,int statis_type);


        abstract void refreshTabLayout(TabLayout tabLayout,ViewPager viewPager,ArrayList<ChartData> datas);
        abstract void clearMap();
    }

}
