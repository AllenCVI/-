package com.kuxuan.moneynote.db;

import com.kuxuan.sqlite.dao.UserDBDao;
import com.kuxuan.sqlite.db.UserDB;

import java.util.List;

/**
 * 用户DB操作类
 * Created by xieshengqi on 2018/4/2.
 */

public class UserDBOperator {

    private static  UserDBOperator mInstance;


    private UserDBOperator(){

    }


    public static UserDBOperator getInstance(){
        if(mInstance==null){
            synchronized (UserDBOperator.class){
                mInstance = new UserDBOperator();

            }
        }
        return mInstance;
    }


    /**
     * 插入数据
     * @param user_id
     */
    public synchronized void insertUserId(int user_id){
        if(!checkIsIn(user_id)){
        DbManager.getInstance().getmDaoSession().getUserDBDao().insert(new UserDB(user_id,System.currentTimeMillis()));
        }
    }


    /**
     * 查看user_id是否存在于库中
     * @param user_id
     * @return
     */
    public synchronized boolean checkIsIn(int user_id){
        List<UserDB> list = DbManager.getInstance().getmDaoSession().getUserDBDao().queryBuilder().where(UserDBDao.Properties.User_id.eq(user_id)).list();
        if(list==null||list.size()==0){
            return false;
        }else{
            return true;
        }
    }

    public synchronized UserDB getUserBean(int user_id){
        List<UserDB> list = DbManager.getInstance().getmDaoSession().getUserDBDao().queryBuilder().where(UserDBDao.Properties.User_id.eq(user_id)).list();
        if(list==null||list.size()==0){
            return null;
        }else{
            return list.get(0);
        }
    }

    /**
     * 更新同步时间
     * @param user_id
     */
    public synchronized void upLoadUserData(int user_id){
        UserDBDao userDBDao = DbManager.getInstance().getmDaoSession().getUserDBDao();
        UserDB userBean = getUserBean(user_id);
        if(userBean!=null){
            userBean.setSyncTime(System.currentTimeMillis());
            userDBDao.update(userBean);
        }
    }
}
