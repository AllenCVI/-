package com.kuxuan.moneynote.servier;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kuxuan.moneynote.listener.DownDbListener;
import com.kuxuan.moneynote.receiver.DownLoadReceiver;
import com.kuxuan.moneynote.ui.activitys.eventbus.RefreshEvent;
import com.kuxuan.moneynote.utils.DownDbDataOpertor;
import com.kuxuan.moneynote.utils.LoginStatusUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xieshengqi on 2018/4/9.
 */

public class DownLoadService extends Service {


    private final String TAG = "DownLoadService";
    /**
     * 每3分钟更新一次数据
     */
    private static final int ONE_Miniute = 30 * 1000;
    private static final int PENDING_REQUEST = 0;
    AlarmManager alarmManager;
    PendingIntent pIntent;

    public DownLoadService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //这里模拟后台操作
//上传数据
        if (LoginStatusUtil.isLoginin()) {
            syncData();

//        Log.e("UpDataService", "我在执行");

        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        if (alarmManager != null)
            alarmManager.cancel(pIntent);//关闭的服务的时候同时关闭广播注册者
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 同步数据
     */
    private void syncData() {


        DownDbDataOpertor.getInstance().syncForThread(new DownDbListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess() {
                EventBus.getDefault().post(new RefreshEvent());
                //结束服务
                stopService(new Intent(DownLoadService.this, DownLoadService.class));
            }

            @Override
            public void onFail() {
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                long triggerAtTime = SystemClock.elapsedRealtime() + ONE_Miniute;//从开机到现在的毫秒书（手机睡眠(sleep)的时间也包括在内
                Intent i = new Intent(DownLoadService.this, DownLoadReceiver.class);
                pIntent = PendingIntent.getBroadcast(DownLoadService.this, PENDING_REQUEST, i, PENDING_REQUEST);
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pIntent);
            }
        });

//        RetrofitClient.getApiService().getDownLoadData(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<UploadBeanJson>>() {
//            @Override
//            public void onError(ExceptionHandle.ResponeThrowable e) {
//                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                long triggerAtTime = SystemClock.elapsedRealtime() + ONE_Miniute;//从开机到现在的毫秒书（手机睡眠(sleep)的时间也包括在内
//                Intent i = new Intent(DownLoadService.this, DownLoadReceiver.class);
//                pIntent = PendingIntent.getBroadcast(DownLoadService.this, PENDING_REQUEST, i, PENDING_REQUEST);
//                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pIntent);
//            }
//
//            @Override
//            public void onSuccess(final BaseJson<UploadBeanJson> objectBaseJson) {
//                if (objectBaseJson != null && objectBaseJson.getCode() == 0) {
//                    final UploadBeanJson res = objectBaseJson.getData();
//                    if (res != null) {
//                        final int number = res.getNumber();
//                        if (res.getData() != null) {
//                            io.reactivex.Observable.create(new ObservableOnSubscribe<Integer>() {
//                                @Override
//                                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//                                    insertDb(objectBaseJson.getData().getData());
//                                    if (res.getData().size() < number) {
//                                        //结束
//                                        e.onComplete();
//                                    } else {
//                                        int count = page;
//                                        e.onNext(count + 1);
//                                    }
//                                }
//                            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
//                                @Override
//                                public void onSubscribe(Disposable d) {
//
//                                }
//
//                                @Override
//                                public void onNext(Integer o) {
//                                    syncData(o);
//
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//
//                                }
//
//                                @Override
//                                public void onComplete() {
//                                    EventBus.getDefault().post(new RefreshEvent());
//                                    //结束服务
//                                    stopService(new Intent(DownLoadService.this, DownLoadService.class));
//
//                                }
//                            });
//
//
//                        } else {
//                            stopService(new Intent(DownLoadService.this, DownLoadService.class));
//
//                        }
//
//                    } else {
//                        stopService(new Intent(DownLoadService.this, DownLoadService.class));
//
//                    }
//                }
//
//            }
//        });
    }

//    private void insertDb(ArrayList<UploadDbjson> uploadDbjsons) {
//        if (uploadDbjsons == null) {
//            return;
//        }
//        CategoryDaoOperator categoryDaoOperator = CategoryDaoOperator.newInstance();
//        Integer id = (Integer) SPUtil.get(MyApplication.getInstance(), Constant.DbInfo.DB_ID_COUNT, -1);
//        for (UploadDbjson uploadDbjson : uploadDbjsons) {
//            long time = uploadDbjson.getTime() * 1000;
//            long create_time = uploadDbjson.getCreated_at() * 1000;
//            long updata_time = uploadDbjson.getUpdated_at() * 1000;
//            if (create_time == 0) {
//                create_time =uploadDbjson.getTime();
//            }
//            if (updata_time == 0) {
//                updata_time = time;
//            }
//            String data = TimeUtlis.getData(time);
//            String[] split = data.split("-");
//            id++;
//            categoryDaoOperator.insert((long)id,uploadDbjson.getIdentification(), uploadDbjson.getDemo(), uploadDbjson.getCategory_name(), uploadDbjson.getType(), BillCategoreDaoOperator.newInstance().getDetaillIconUrlById(uploadDbjson.getCategory_id()), Double.parseDouble(uploadDbjson.getAccount()),uploadDbjson.getCategory_id(),Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]),create_time, updata_time, uploadDbjson.getStatus(), uploadDbjson.getUser_id(), false);
//        }
//        SPUtil.putAndApply(MyApplication.getInstance(), Constant.DbInfo.DB_ID_COUNT, id);
//    }

}

