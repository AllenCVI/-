package com.kuxuan.moneynote.ui.activitys.opinion;

import com.kuxuan.moneynote.listener.MVPListener;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class OptionPresenter  extends OptionContract.OptionPresent {

    @Override
    void sendMessage(String message) {
        mModel.sendMessage(new MVPListener<Object>() {
            @Override
            public void onSuccess(Object content) {
                if(view!=null){
                    view.sendSuccess("发送成功");
                }

            }

            @Override
            public void onFail(String msg) {
                view.sendSuccess(msg);
            }
        },message);
    }
}
