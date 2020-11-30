package com.kuxuan.moneynote.ui.activitys.alarm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.kuxuan.moneynote.base.mvpbase.BaseModel;
import com.kuxuan.moneynote.base.mvpbase.BasePresent;
import com.kuxuan.moneynote.base.mvpbase.BaseView;
import com.kuxuan.moneynote.json.Time;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public interface AlarmContract {
    interface  AlarmView extends BaseView {


    }



    interface AlarmModel extends BaseModel {

    }


    abstract  class  AlarmPresent extends BasePresent<AlarmModel,AlarmView> {
        protected abstract void initRecyclerView(Context context, RecyclerView recyclerView);
        protected abstract void addData(Time time);

    }
}
