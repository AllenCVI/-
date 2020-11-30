package com.kuxuan.moneynote.listener;

import com.kuxuan.sqlite.db.CategoryDB;

import java.util.ArrayList;

/**
 * 上传数据监听回调
 * Created by xieshengqi on 2018/5/8.
 */

public interface UpDBListener {

    void onStart();

    void onSuccess();

    void onFail();


    /**
     * 登出账号时出现更新数据失败时回调
     * @param needUpdataJson
     */
    void onexitLoginFail(ArrayList<CategoryDB> needUpdataJson);

    /**
     * 不需要更新数据
     */
    void onNoNeedUpdata();
}
