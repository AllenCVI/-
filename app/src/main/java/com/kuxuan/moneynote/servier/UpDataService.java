package com.kuxuan.moneynote.servier;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.listener.UpDBListener;
import com.kuxuan.moneynote.receiver.UpDataReceiver;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.UpDBDataOpertor;
import com.kuxuan.sqlite.db.CategoryDB;

import java.util.ArrayList;

/**
 * Created by xieshengqi on 2018/4/8.
 */

public class UpDataService extends Service {

    /**
     * 每3分钟更新一次数据
     */
    private static final int ONE_Miniute = 180 * 1000;
    private static final int PENDING_REQUEST = 0;
    AlarmManager alarmManager;
    PendingIntent pIntent;

    public UpDataService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //这里模拟后台操作
//上传数据
        if (LoginStatusUtil.isLoginin()) {
            final CategoryDaoOperator categoryDaoOperator = CategoryDaoOperator.newInstance();
            if (LoginStatusUtil.getLoginUserId() != -1) {
//                final ArrayList<CategoryDB> needUpdataJson = categoryDaoOperator.getNeedUpdataJson();
//                if (needUpdataJson != null && needUpdataJson.size() != 0) {
//                    String json = UpDataOperator.getInstance().getJson(needUpdataJson);
//                    RetrofitClient.getApiService().upData(new UpDataBody(0, json)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
//                        @Override
//                        public void onError(ExceptionHandle.ResponeThrowable e) {
//                            Log.e("UpDataService",e.toString());
//
//                        }
//                        @Override
//                        public void onSuccess(BaseJson<Object> objectBeanNewJson) {
//                            //更新数据库
//                            if(objectBeanNewJson!=null&&objectBeanNewJson.getCode()==0){
//                                for (CategoryDB categoryDB : needUpdataJson) {
//                                    if (categoryDB.getStatus() == 1) {
//                                        categoryDaoOperator.deleteData(categoryDB.getBill_id());
//                                    } else {
//                                        categoryDaoOperator.updataNoNeedSynce(categoryDB.getBill_id());
//                                    }
//                                }
//                            }else{
//                                for (CategoryDB categoryDB : needUpdataJson) {
//                                    categoryDaoOperator.updataNeedSynce(categoryDB.getBill_id());
//                                }
//                            }
//
//                        }
//                    });
//                }
                UpDBDataOpertor.getInstance().onUpData(new UpDBListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onexitLoginFail(ArrayList<CategoryDB> needUpdataJson) {

                    }

                    @Override
                    public void onNoNeedUpdata() {

                    }
                });
            }
//        Log.e("UpDataService", "我在执行");
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            long triggerAtTime = SystemClock.elapsedRealtime() + ONE_Miniute;//从开机到现在的毫秒书（手机睡眠(sleep)的时间也包括在内
            Intent i = new Intent(this, UpDataReceiver.class);
            pIntent = PendingIntent.getBroadcast(this, PENDING_REQUEST, i, PENDING_REQUEST);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pIntent);
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        alarmManager.cancel(pIntent);//关闭的服务的时候同时关闭广播注册者
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
