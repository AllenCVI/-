package com.kuxuan.moneynote.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.kuxuan.moneynote.servier.ServiceUtil;
import com.kuxuan.moneynote.ui.activitys.eventbus.NetworkEvent;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.NetWorkUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xieshengqi on 2018/4/8.
 */

public class NetBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (NetWorkUtil.isNetworkAvailable(context)) {
                //有网的情况下
                if (LoginStatusUtil.isLoginin()) {
                    if(LoginStatusUtil.getLoginUserId()==-1){
                        LoginStatusUtil.getFirstUserId();
                        ServiceUtil.startDownLoadData(context);
                    }
                    ServiceUtil.startUpData(context);
                }
            } else {
                //没网的情况下
                if (LoginStatusUtil.isLoginin()) {
                    ServiceUtil.stopUpData(context);
                }
            }
            // 接口回调传过去状态的类型
            EventBus.getDefault().post(new NetworkEvent());
        }
    }

}

