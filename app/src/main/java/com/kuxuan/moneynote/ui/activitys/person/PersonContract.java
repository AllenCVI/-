package com.kuxuan.moneynote.ui.activitys.person;

import com.kuxuan.moneynote.base.mvpbase.BaseModel;
import com.kuxuan.moneynote.base.mvpbase.BasePresent;
import com.kuxuan.moneynote.base.mvpbase.BaseView;
import com.kuxuan.moneynote.json.MineJson;
import com.kuxuan.moneynote.json.netbody.PersonBody;
import com.kuxuan.moneynote.listener.MVPListener;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public interface PersonContract {

    interface  PersonView extends BaseView {
        /**
         * 设置我的界面的数据
         * @param mineModel
         */
        void setMineData(MineJson mineModel);


        /**
         * 设置我的页面的头像
         */
        void setMineHeadImg();



    }



    interface PersonModel extends BaseModel {
        /**
         * 获取我的个人信息数据
         * @param listMVPListener
         */
        void getMineData(MVPListener<MineJson> listMVPListener);


        /**
         * 修改头像
         * @param listMVPListener
         * @param personBody
         */
        void updateAvatar(MVPListener<Object> listMVPListener, PersonBody personBody);

        /**
         * 修改个人信息
         * @param listMVPListener
         * @param personBody
         */
        void updatePersonData(MVPListener<Object> listMVPListener, PersonBody personBody);
    }


    abstract  class  PersonPresent extends BasePresent<PersonModel,PersonView> {
        /**
         * 获取我的的数据
         */
        abstract void getMineData();

        /**
         * 更新我的信息
         * @param personBody
         */
        abstract void updateAvatar(PersonBody personBody);

        /**
         * 更新我的信息
         * @param personBody
         */
        abstract void updatePersonData(PersonBody personBody);
    }
}
