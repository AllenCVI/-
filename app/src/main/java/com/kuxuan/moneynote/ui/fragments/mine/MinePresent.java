package com.kuxuan.moneynote.ui.fragments.mine;

import com.kuxuan.moneynote.json.BillJson;
import com.kuxuan.moneynote.json.MineJson;
import com.kuxuan.moneynote.listener.MVPListener;
import com.kuxuan.moneynote.utils.LoginStatusUtil;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class MinePresent extends MineContract.MinePresent {
    @Override
    void getMineData() {

        mModel.getMineData(new MVPListener<MineJson>() {
            @Override
            public void onSuccess(MineJson content) {
                if (view != null) {
                    view.hideProgress();
                }
                if (content != null) {
                    //保存到本地
                    LoginStatusUtil.setUserInfo(content);
                    if (view != null) {
                        view.setMineData(content);
                    }

                }

            }

            @Override
            public void onFail(String msg) {
                if (view != null) {
                    view.hideProgress();
                }
                view.showErrorMsg(msg);
            }
        });
    }

    @Override
    void getMineBill(String year) {
        mModel.getMineBillData(new MVPListener<BillJson>() {
            @Override
            public void onSuccess(BillJson content) {
                if (view != null) {
                    view.hideProgress();
                }
                if (content != null) {
                    if (view != null) {
                        view.setMineBillData(content);
                    }

                }
            }

            @Override
            public void onFail(String msg) {
                if (view != null) {
                    view.hideProgress();
                }
            }
        }, year);
    }
}
