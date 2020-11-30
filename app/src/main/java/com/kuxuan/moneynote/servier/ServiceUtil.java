package com.kuxuan.moneynote.servier;

import android.content.Context;
import android.content.Intent;


/**
 * Created by xieshengqi on 2018/4/9.
 */

public class ServiceUtil {


    /**
     * 开启上传服务
     *
     * @param context
     */
    public static void startUpData(Context context) {
        Intent intent = new Intent(context, UpDataService.class);
        context.startService(intent);
    }
    /**
     * 开启下载服务
     *
     * @param context
     */
    public static void startDownLoadData(Context context) {
        Intent intent = new Intent(context, DownLoadService.class);
        context.startService(intent);
    }

    /**
     * 关闭服务
     *
     * @param context
     */
    public static void stopUpData(Context context) {
        Intent intent = new Intent(context, UpDataService.class);
        context.stopService(intent);
    }
    /**
     * 关闭服务
     *
     * @param context
     */
    public static void stopDownData(Context context) {
        Intent intent = new Intent(context, DownLoadService.class);
        context.stopService(intent);
    }
}
