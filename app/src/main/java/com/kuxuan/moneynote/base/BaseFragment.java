package com.kuxuan.moneynote.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuxuan.moneynote.R;

import butterknife.ButterKnife;

/**
 * Created by xieshengqi on 2016/2/17.
 */
public abstract class BaseFragment extends Fragment {
    private View rootView;
    public BaseFragmentActivity mActivity;
    private boolean created;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseFragmentActivity) context;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        created = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayout(), null);
            ButterKnife.bind(this, rootView);
            onBaseCreate(savedInstanceState);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private TitleView titleView;

    public TitleView getTitleView(int what) {

        return titleView == null ? new TitleView(getActivity(), (View) findViewById(R.id.header),what) : titleView;

    }


    private boolean isDestory = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        isDestory = true;
    }

    public boolean isDestory() {
        return isDestory;
    }


    public View findViewById(@IdRes int id) {
        if (rootView != null) {
            return rootView.findViewById(id);
        } else {
            throw new NullPointerException("rootView is null, run getLayout() is ture ?");
        }
    }

    /**
     * 页面第一次加载时杂乱操作放在这里执行，非子线程
     */
    public void onLoad() {
    }

    public String getPageTitle() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 判断当前fragment是否显示
        if (getUserVisibleHint()) {
            if (created) {
                onLoad();
                created = false;
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 每次切换fragment时调用的方法
        if (isVisibleToUser) {
            if (created) {
                onLoad();
                created = false;
            }
        }
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public abstract void onBaseCreate(Bundle savedInstanceState);

    public abstract int getLayout();

    public View getRootView() {
        return rootView;
    }


}