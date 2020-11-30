package com.kuxuan.moneynote.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.ui.activitys.MainActivity;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
//注意：receiver记得在manifest.xml注册
public  class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bitmap bmp= BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon);
            Log.e(TAG,"闹钟已响");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.mipmap.icon_logo);
            RemoteViews contentView = new RemoteViews(getPackageName(),R.layout.notification);
            builder.setContent(contentView);
            builder.setAutoCancel(true);
            // 需要VIBRATE权限
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
            builder.setPriority(Notification.PRIORITY_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.notification, pendingIntent);
            notificationManager.notify(111, builder.build());




    }
}
