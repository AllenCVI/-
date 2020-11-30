package com.kuxuan.moneynote.base.mvpbase;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kuxuan.moneynote.base.BaseFragmentActivity;
import com.kuxuan.moneynote.utils.CreateUtil;


/**
 * Created by xieshengqi on 2017/7/3.
 */

public abstract class MVPFragmentActivity< P extends BasePresent, M extends BaseModel> extends BaseFragmentActivity {


    protected P mPresenter;
    protected M mModel;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //内部获取第一个类型参数的真实类型  ，反射new出对象
        mPresenter = CreateUtil.getT(this, 0);
        //内部获取第二个类型参数的真实类型  ，反射new出对象
        mModel = CreateUtil.getT(this, 1);
        //使得P层绑定M层和V层，持有M和V的引用
        mPresenter.attachModelView(mModel, this);
        initView();
    }




    @Override
    protected void onDestroy() {
        mPresenter.onDettach();
        super.onDestroy();

    }

    //子类Activity实现
    public abstract void initView();

}
