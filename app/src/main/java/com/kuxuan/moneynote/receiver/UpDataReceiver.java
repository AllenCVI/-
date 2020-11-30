package com.kuxuan.moneynote.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by xieshengqi on 2018/4/8.
 */

public class UpDataReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, com.kuxuan.moneynote.servier.UpDataService.class);
        context.startService(i);
    }
}
