package com.kuxuan.moneynote.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.kuxuan.moneynote.MyApplication;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.base.BaseFragmentActivity;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.db.BillCategoreDaoOperator;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.UploadBeanJson;
import com.kuxuan.moneynote.json.UploadDbjson;
import com.kuxuan.moneynote.servier.ServiceUtil;
import com.kuxuan.moneynote.ui.activitys.account.AccountActivity;
import com.kuxuan.moneynote.ui.activitys.eventbus.SkinEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.TokenloseEvent;
import com.kuxuan.moneynote.ui.activitys.home.DataGenerator;
import com.kuxuan.moneynote.ui.fragments.find.FindFragment;
import com.kuxuan.moneynote.ui.fragments.mine.MineFragment;
import com.kuxuan.moneynote.ui.fragments.reportsingle.ReportSingleFragment;
import com.kuxuan.moneynote.ui.weight.NavigationLayout;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.NetWorkUtil;
import com.kuxuan.moneynote.utils.SPUtil;
import com.kuxuan.moneynote.utils.TimeUtlis;
import com.kuxuan.moneynote.utils.ToastUtil;
import com.kuxuan.sqlite.db.CategoryDB;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xieshengqi on 2017/10/18.
 */

public class MainActivity extends BaseFragmentActivity {

    @Bind(R.id.activity_main_navigation)
    NavigationLayout navigationLayout;
    public static final String GO_TYPE = "gotype";

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        //更新版本的时候判断这个值

        if (LoginStatusUtil.isLoginin()) {
            if (LoginStatusUtil.getLoginUserId() == -1) {
                //退出登录，首次离线记账（没有用户id，取用户id（有网的情况下））
                if (NetWorkUtil.isNetworkAvailable(this)) {
                    ServiceUtil.startDownLoadData(MainActivity.this);
                    showProgressDialog("加载中...");
                    syncData(0);
//                    LoginStatusUtil.getFirstUserId(new Successlistener() {
//                        @Override
//                        public void onSuccess() {
//                            //首次下载数据
//
//                        }
//
//                        @Override
//                        public void onFail() {
//                            //失败就在获取一次
//                            closeProgressDialog();
//                            initLayout();
//                            LoginStatusUtil.getFirstUserId();
//                            ServiceUtil.startDownLoadData(MainActivity.this);
//                        }
//                    });
                } else {
                    initLayout();
                }
            } else {
                initLayout();
                ServiceUtil.startDownLoadData(this);
                ServiceUtil.startUpData(this);
            }
        } else {
            initLayout();
        }
//        getTitleView(0).setTitle(getResources().getString(R.string.app_name));


    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        ServiceUtil.stopUpData(this);
        ServiceUtil.stopDownData(this);
        super.onDestroy();
    }

    Observable<Integer> integerObservable;

    /**
     * 同步数据
     */
    private void syncData(final int page) {
        RetrofitClient.getApiService().getDownLoadData(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<UploadBeanJson>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                closeProgressDialog();
                initLayout();
                ServiceUtil.startDownLoadData(MainActivity.this);
                ServiceUtil.startUpData(MainActivity.this);
            }

            @Override
            public void onSuccess(final BaseJson<UploadBeanJson> objectBaseJson) {
                if (objectBaseJson != null && objectBaseJson.getCode() == 0) {
                    final UploadBeanJson res = objectBaseJson.getData();
                    if (res != null) {
                        final int number = res.getNumber();
                        if (res.getData() != null) {
                            integerObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
                                @Override
                                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                                    insertDb(objectBaseJson.getData().getData());
                                    if (res.getData().size() < number) {
                                        //结束
                                        e.onComplete();
                                    } else {
                                        int count = page;
                                        e.onNext(objectBaseJson.getData().getPage() + 1);
                                    }
                                }
                            });
                            integerObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Integer o) {
                                    syncData(o);

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {
                                    initLayout();
                                    ServiceUtil.startUpData(MainActivity.this);
                                    closeProgressDialog();
                                }
                            });
                        } else {

                        }

                    } else {

                    }
                }

            }
        });
    }

    private void insertDb(ArrayList<UploadDbjson> uploadDbjsons) {
        if (uploadDbjsons == null) {
            return;
        }
        CategoryDaoOperator categoryDaoOperator = CategoryDaoOperator.newInstance();
        Integer id = (Integer) SPUtil.get(MyApplication.getInstance(), Constant.DbInfo.DB_ID_COUNT, -1);
        ArrayList<CategoryDB> categoryDBS = new ArrayList<>();
        for (UploadDbjson uploadDbjson : uploadDbjsons) {
            long time = uploadDbjson.getTime() * 1000;
            long create_time = uploadDbjson.getCreated_at() * 1000;
            long updata_time = uploadDbjson.getUpdated_at() * 1000;
            if (create_time == 0) {
                create_time = uploadDbjson.getTime();
            }
            if (updata_time == 0) {
                updata_time = time;
            }
            String data = TimeUtlis.getData(time);
            String[] split = data.split("-");
            id++;
            categoryDaoOperator.insert((long) id, uploadDbjson.getIdentification(), uploadDbjson.getDemo(), uploadDbjson.getCategory_name(), uploadDbjson.getType(), BillCategoreDaoOperator.newInstance().getDetaillIconUrlById(uploadDbjson.getCategory_id()), Double.parseDouble(uploadDbjson.getAccount()), uploadDbjson.getCategory_id(), Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), create_time, updata_time, time, uploadDbjson.getStatus(), uploadDbjson.getUser_id(), false);
//            CategoryDB categoryDB = new CategoryDB((long) id, uploadDbjson.getIdentification(), uploadDbjson.getDemo(), uploadDbjson.getCategory_name(), uploadDbjson.getType(), BillCategoreDaoOperator.newInstance().getDetaillIconUrlById(uploadDbjson.getCategory_id()), Double.parseDouble(uploadDbjson.getAccount()), uploadDbjson.getCategory_id(), Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), create_time, updata_time, uploadDbjson.getStatus(), uploadDbjson.getUser_id(), false);
//            categoryDBS.add(categoryDB);
//            categoryDaoOperator.insert(categoryDBS);
        }
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.DbInfo.DB_ID_COUNT, id);

    }


    private void initLayout() {
        navigationLayout.setListener(new NavigationLayout.OnItemClickListener() {
            @Override
            public void onClick(int position) {

                //选择下面的button 后，换fragment
                onTabItemSelected(position);

            }

            @Override
            public void onClickCenter() {

                //TODO 判断网络
                boolean isAvailable = NetWorkUtil.isNetworkAvailable(getApplicationContext());
                if (LoginStatusUtil.isLoginin() && isAvailable) {
                    AccountActivity.show(MainActivity.this, Constant.Online_OR_Offline.ONLINE);
                } else {
                    AccountActivity.show(MainActivity.this, Constant.Online_OR_Offline.OFFLINE);
                }
                overridePendingTransition(R.anim.push_bottom_in, R.anim.activity_stay);
            }
        });
        initFragments();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int intExtra = 0;
        try {
            intExtra = intent.getExtras().getInt(GO_TYPE, 0);
        } catch (Exception e) {

        }

        onTabItemSelected(intExtra);
        navigationLayout.setPosition(intExtra);
    }

    private Fragment[] mFragmensts;

    /**
     * 初始化tab
     */
    private void initFragments() {
        mFragmensts = DataGenerator.getFragments();
//        for (int i = 0; i < mFragmensts.length; i++) {
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_content, mFragmensts[0]).commit();
//        }
        int type = 0;
        try {
            type = getIntent().getExtras().getInt(GO_TYPE, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onTabItemSelected(type);
        navigationLayout.setPosition(type);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SkinEvent skinBean) {
        if (skinBean.getCode() != 1000) {
            navigationLayout.refresh(0);
            navigationLayout.setCenter_CIColor();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TokenloseEvent skinBean) {
        ToastUtil.show(this, getResources().getString(R.string.lose_token));
    }

    private void
    onTabItemSelected(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = mFragmensts[0];
                break;
            case 1:
                fragment = mFragmensts[1];
                if (fragment == null) {
                    mFragmensts[1] = new ReportSingleFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.activity_main_content, mFragmensts[1]).commit();
                    fragment = mFragmensts[1];
                }
                //为了让报表页在点击的时才加载
//                if (fragment instanceof TestFragment) {
//                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                    mFragmensts[1] = new ReportSingleFragment();
//                    getSupportFragmentManager().beginTransaction().add(R.id.activity_main_content, mFragmensts[1]).commit();
//                    fragment = mFragmensts[1];
//                }
                break;
            case 2:
                fragment = mFragmensts[2];
                if (fragment == null) {
                    mFragmensts[2] = new FindFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.activity_main_content, mFragmensts[2]).commit();
                    fragment = mFragmensts[2];
                }
                break;
            case 3:
                fragment = mFragmensts[3];
                if (fragment == null) {
                    mFragmensts[3] = new MineFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.activity_main_content, mFragmensts[3]).commit();
                    fragment = mFragmensts[3];
                }
                break;
        }
        //把所有的fragment都隐藏一遍
        for (int i = 0; i < mFragmensts.length; i++) {
            try {
                if (mFragmensts[i] != null)
                    getSupportFragmentManager().beginTransaction().hide(mFragmensts[i]).commit();
            } catch (Exception e) {

            }
        }
        //显示
        if (fragment != null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.home_container, fragment).commit();
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int indext = 0; indext < fragmentManager.getFragments().size(); indext++) {
            Fragment fragment = fragmentManager.getFragments().get(indext); //找到第一层Fragment
            if (fragment == null)
                Log.w("aaa", "Activity result no fragment exists for index: 0x"
                        + Integer.toHexString(requestCode));
            else
                handleResult(fragment, requestCode, resultCode, data);
        }


        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 递归调用，对所有的子Fragment生效
     *
     * @param fragment
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment fragment, int requestCode, int resultCode, Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);//调用每个Fragment的onActivityResult
        Log.e("sss", "MyBaseFragmentActivity");
        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); //找到第二层Fragment
        if (childFragment != null)
            for (Fragment f : childFragment)
                if (f != null) {
                    handleResult(f, requestCode, resultCode, data);
                }
        if (childFragment == null)
            Log.e("sss", "MyBaseFragmentActivity1111");
    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
