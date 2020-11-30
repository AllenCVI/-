package com.kuxuan.moneynote.ui.activitys.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.kuxuan.moneynote.MyApplication;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.json.Time;
import com.kuxuan.moneynote.listener.DeleteListener;
import com.kuxuan.moneynote.receiver.AlarmReceiver;
import com.kuxuan.moneynote.ui.adapter.AlarmAdapter;
import com.kuxuan.moneynote.ui.weight.DividerItemDecoration;
import com.kuxuan.moneynote.utils.SPUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class AlarmPresenter extends AlarmContract.AlarmPresent implements DeleteListener {
    AlarmAdapter mAdapter;
    Context mContext;
    List<Time> data;
    private static final String TAG = "AlarmPresenter";
    @Override
    protected void initRecyclerView(Context context, RecyclerView recyclerView) {
        mContext = context;
        data = new ArrayList<Time>();
        loadArray(data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new AlarmAdapter(R.layout.item_alarm_list, data,this);

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void addData(Time time) {
        for(int i=0;i<data.size();i++) {
            if (time.getTime().equals(data.get(i).getTime())) {
                Toast.makeText(mContext, "相同时间无法添加多次", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        data.add(time);
        mAdapter.notifyDataSetChanged();
        SPUtil.putAndApply(mContext,"1",time.getTime());
        String a[] = time.getTime().split(":");

        //得到日历实例，主要是为了下面的获取时间
        Calendar mCalendar1 = Calendar.getInstance();
        mCalendar1.setTimeInMillis(System.currentTimeMillis());

        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();

        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar1.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        mCalendar1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置在几点提醒  设置的为13点
        mCalendar1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(a[0]));
        //设置在几分提醒  设置的为25分
        mCalendar1.set(Calendar.MINUTE, Integer.parseInt(a[1]));
        //下面这两个看字面意思也知道
        mCalendar1.set(Calendar.SECOND, 0);
        mCalendar1.set(Calendar.MILLISECOND, 0);


        long selectTime = mCalendar1.getTimeInMillis();
        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if(systemTime > selectTime) {
            mCalendar1.add(Calendar.DAY_OF_MONTH, 1);
        }
        //AlarmReceiver.class为广播接受者
        Intent intent = new Intent(MyApplication.getInstance(), AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(MyApplication.getInstance(), 0, intent, 0);
        //得到AlarmManager实例
        AlarmManager am = (AlarmManager)MyApplication.getInstance().getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,mCalendar1.getTimeInMillis(),24*60*60*1000,pi);
    }


    @Override
    public void delete(int position) {
        data.remove(position);
        mAdapter.notifyItemRemoved(position);
        saveArray(data);
    }

    public void save(){
        saveArray(data);
    }

    public boolean saveArray(List<Time> list) {
        SharedPreferences sp = mContext.getSharedPreferences("ingoreList", MODE_PRIVATE);
        SharedPreferences.Editor mEdit1= sp.edit();
        mEdit1.putInt("Status_size",list.size());

        for(int i=0;i<list.size();i++) {
            mEdit1.remove("Status_" + i);
            mEdit1.putString("Status_" + i, list.get(i).getTime());
        }
        return mEdit1.commit();
    }

    public void loadArray(List<Time> list) {

        SharedPreferences mSharedPreference1 = mContext.getSharedPreferences("ingoreList", MODE_PRIVATE);
        list.clear();
        int size = mSharedPreference1.getInt("Status_size", 0);
        for(int i=0;i<size;i++) {
            list.add(new Time(mSharedPreference1.getString("Status_" + i, null)));

        }
    }



}
