package com.kuxuan.moneynote.ui.fragments.find;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.base.BaseFragment;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.FindJson;
import com.kuxuan.moneynote.ui.activitys.WebviewActivity;
import com.kuxuan.moneynote.ui.activitys.eventbus.SkinEvent;
import com.kuxuan.moneynote.ui.activitys.login.LoginActivity;
import com.kuxuan.moneynote.ui.activitys.login.PhoneLoginActivity;
import com.kuxuan.moneynote.ui.adapter.FindAdapter;
import com.kuxuan.moneynote.ui.weight.MyToast;
import com.kuxuan.moneynote.utils.DrawableUtil;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.UIHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 发现页
 * Created by xieshengqi on 2017/10/31.
 */

public class FindFragment extends BaseFragment {
    @Bind(R.id.fragment_find_recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.fragment_find_layout)
    LinearLayout linearLayout;
    @Bind(R.id.no_network_view)
    View netWorkView;

    private FindAdapter adapter;

    @Override
    public void onBaseCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        getTitleView(0).setTitle("发现");
        initRecyclerView();
        getLists();
        netWorkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLists();
            }
        });
    }


    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new FindAdapter(R.layout.item_find_layout);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FindJson findJson = (FindJson) adapter.getData().get(position);
                if (!LoginStatusUtil.isLoginin()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(PhoneLoginActivity.GOTYPE, 3);
                    UIHelper.openActivityWithBundle(getActivity(), LoginActivity.class, bundle);
                } else {
                    //跳转H5页面
                    Bundle webBundle = new Bundle();
                    webBundle.putString(WebviewActivity.URL, findJson.getUrl());
                    webBundle.putString(WebviewActivity.TITLE, findJson.getName());
                    UIHelper.openActivityWithBundle(getActivity(), WebviewActivity.class, webBundle);
                }
            }
        });
    }


    private void getLists() {
        RetrofitClient.getApiService().getFindLists().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<ArrayList<FindJson>>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                if (e.message.equals("网络连接失败，请检测网络")) {
                    showNoNetWorkLog();
                }

            }

            @Override
            public void onSuccess(BaseJson<ArrayList<FindJson>> arrayListBaseJson) {
                linearLayout.setVisibility(View.VISIBLE);
                if (arrayListBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                    if (arrayListBaseJson.getData() != null)
                        adapter.setNewData(arrayListBaseJson.getData());
                }
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_find;
    }


    /**
     * 展示无网络的时候
     */
    public void showNoNetWorkLog() {
        linearLayout.setVisibility(View.GONE);
        MyToast.makeText(getActivity(), 5000).show();
    }

    /**
     * 更换皮肤颜色
     */
    private void changeSkinData() {
        RelativeLayout head = getTitleView(0).getHead();
        if (head != null) {
            head.setBackgroundColor(DrawableUtil.getSkinColor(getActivity()));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SkinEvent skinBean) {
        if(skinBean.getCode()!=1000) {
            changeSkinData();
        }
        }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
