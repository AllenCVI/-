package com.kuxuan.moneynote.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by xieshengqi on 2018/4/9.
 */

public class DownLoadReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, com.kuxuan.moneynote.servier.DownLoadService.class);
        context.startService(i);
    }
}
