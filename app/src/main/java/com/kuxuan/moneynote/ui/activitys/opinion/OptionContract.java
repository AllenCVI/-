package com.kuxuan.moneynote.ui.activitys.opinion;

import com.kuxuan.moneynote.base.mvpbase.BaseModel;
import com.kuxuan.moneynote.base.mvpbase.BasePresent;
import com.kuxuan.moneynote.base.mvpbase.BaseView;
import com.kuxuan.moneynote.listener.MVPListener;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public interface OptionContract {

    interface  OptionView extends BaseView {
        /**
         * 发送成功
         * @param message
         */
        void sendSuccess(String message);

    }



    interface OptionModel extends BaseModel {
        /**
         * 发送message
         * @param listMVPListener
         * @param message
         */
        void sendMessage(MVPListener<Object> listMVPListener,String message);
    }


    abstract  class  OptionPresent extends BasePresent<OptionModel,OptionView> {
        /**
         * 发送用户留言
         * @param message
         */
        abstract void sendMessage(String message);
    }

}
