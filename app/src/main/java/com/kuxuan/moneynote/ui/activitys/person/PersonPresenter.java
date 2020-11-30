package com.kuxuan.moneynote.ui.activitys.person;

import com.kuxuan.moneynote.json.MineJson;
import com.kuxuan.moneynote.json.netbody.PersonBody;
import com.kuxuan.moneynote.listener.MVPListener;
import com.kuxuan.moneynote.utils.LoginStatusUtil;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class PersonPresenter  extends PersonContract.PersonPresent{
    @Override
    void getMineData() {
        mModel.getMineData(new MVPListener<MineJson>() {
            @Override
            public void onSuccess(MineJson content) {
                if(view!=null){
                    view.hideProgress();
                }
                if(content!=null){
                    LoginStatusUtil.setUserInfo(content);
                    if(view!=null){
                        view.setMineData(content);
                    }

                }

            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    @Override
    void updateAvatar(PersonBody personBody) {
        mModel.updateAvatar(new MVPListener<Object>() {
            @Override
            public void onSuccess(Object content) {
                if(view!=null){
                    view.hideProgress();
                }
                if(content!=null){
                    if(view!=null){
                        view.setMineHeadImg();
                    }

                }

            }

            @Override
            public void onFail(String msg) {

            }
        },personBody);
    }

    @Override
    void updatePersonData(PersonBody personBody) {
        mModel.updatePersonData(new MVPListener<Object>() {
            @Override
            public void onSuccess(Object content) {
                if(view!=null){
                    view.hideProgress();
                }
            }

            @Override
            public void onFail(String msg) {

            }
        },personBody);
    }
}
