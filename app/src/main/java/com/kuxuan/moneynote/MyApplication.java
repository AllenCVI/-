package com.kuxuan.moneynote;

import android.app.Application;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;

import com.kuxuan.moneynote.db.DbManager;
import com.kuxuan.moneynote.utils.DisplayUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.utils.UMUtils;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.io.File;


/**
 * Created by xieshengqi on 2017/3/31.
 */

public class MyApplication extends Application {

    private static MyApplication application;

    //    private static final String UMENT_SECRET="59b64bfa677baa34a00017ca";
    private static final String UMENG_APPKEY = "59fc677e734be45f110001f6";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        //内存检测工具
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You /should not init your app in this proc
// ess.
//            return;
//        }
//        LeakCanary.install(this);
        AutoLayoutConifg.getInstance().useDeviceSize();
        DisplayUtil.init(this);
        application = this;
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_DUM_NORMAL);
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        UMUtils.setChannel(this, BuildConfig.CHANNLE);
        QueuedWork.isUseThreadPool = false;
        UMShareAPI.get(this);
//        DisplayUtil.init(this);
//        initComponent();
//        initJpush();
        //各个平台的配置，建议放在全局Application或者程序入口
        {
            PlatformConfig.setWeixin("wxee1937fe7fbf238d", "2016584eff63b03b5a911d41c12519b3");
            PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        }


        //api 27后监听网络变化
/*        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        NetworkRequest request = builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        connectivityManager.requestNetwork(request,
                new ConnectivityManager.NetworkCallback() {

                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);
                        Toast.makeText(getApplicationContext(),"网络未连接",Toast.LENGTH_SHORT).show();
                    }

                    @Override public void onAvailable(Network network) {
                        super.onAvailable(network);
                        Toast.makeText(getApplicationContext(),"网络已连接",Toast.LENGTH_SHORT).show();
                    }


                });*/


    }

//    private AppComponent mAppComponent;
//
//    private void initComponent() {
//        mAppComponent = DaggerAppComponent.builder().applicationModule(new ApplicationModule(this)).build();
//        mAppComponent.inject(this);
//    }
//
//    public AppComponent getAppComponent() {
//        return mAppComponent;
//    }

    public static MyApplication getInstance() {
        return application;
    }

    public static File getCacheDirFile() {
        return application.getCacheDir();
    }


    /**
     * 获取头像的临时存储文件地址
     *
     * @return 临时文件
     */
    public static File getPortraitTmpFile() {
        // 得到头像目录的缓存地址
        File dir = new File(getCacheDirFile(), "portrait");
        // 创建所有的对应的文件夹
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();

        // 删除旧的一些缓存为文件
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }

        // 返回一个当前时间戳的目录文件地址
        File path = new File(dir, SystemClock.uptimeMillis() + ".jpg");
        return path.getAbsoluteFile();
    }


//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this) ;
//    }

}
