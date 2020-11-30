package com.kuxuan.moneynote.ui.fragments.reportdetial;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.kuxuan.moneynote.base.mvpbase.BaseModel;
import com.kuxuan.moneynote.base.mvpbase.BasePresent;
import com.kuxuan.moneynote.base.mvpbase.BaseView;
import com.kuxuan.moneynote.json.CategoryDataJson;
import com.kuxuan.moneynote.json.ChartData;
import com.kuxuan.moneynote.json.LineJson;
import com.kuxuan.moneynote.ui.weight.ChartLayout;

import java.util.ArrayList;

/**
 * Created by xieshengqi on 2017/10/20.
 */

public interface ReportDetialContract {

    interface RepDeView extends BaseView {

        void setLineData(ArrayList<LineJson> data);


        void setCircleData(ArrayList<CategoryDataJson> data);

    }


    interface RepDeModel extends BaseModel {

    }


    abstract class RepDePresent extends BasePresent<RepDeModel, RepDeView> {

        abstract void setChartData(ChartLayout chartLayout, int type, ArrayList<LineJson> datas);


        abstract void initRecyclerView(Context context,RecyclerView recyclerView);


        abstract void setLineData(ChartData chartData);
        abstract void setCircleData(ChartData chartData);
    }
}
