package com.kuxuan.moneynote.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuxuan.moneynote.MyApplication;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.InsertDbJSON;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 用户标志位获取类（用于获取每个用户从服务器拉取数据时的开始值）
 * Created by xieshengqi on 2018/4/28.
 */

public class DBMaxIdOpertor {

    private static DBMaxIdOpertor mInstance;

    ArrayList<InsertDbJSON> list;

    private DBMaxIdOpertor() {
        String json = (String) SPUtil.get(MyApplication.getInstance(), Constant.DbInfo.DB_USER_LISTS, "");
        Gson gson = new Gson();
        Type tp = new TypeToken<ArrayList<InsertDbJSON>>() {
        }.getType();
        try {
            list = gson.fromJson(json, tp);
        } catch (Exception e) {
            list = new ArrayList<>();
        }
        if (list == null) {
            list = new ArrayList<>();
        }
    }

    public static DBMaxIdOpertor getInstance() {
        if (mInstance == null) {
            synchronized (DBMaxIdOpertor.class) {
                mInstance = new DBMaxIdOpertor();
            }
        }
        return mInstance;
    }


    /**
     * 获取用户当前取到多少值了
     *
     * @param userId
     * @return
     */
    public int getMaxId(String userId) {
        int pos = checkInLists(userId);
        if (pos == -1) {
            //加入数据中
            list.add(new InsertDbJSON(userId, 0));
            return 0;
        } else {
            return list.get(pos).getMaxId();
        }
    }


    /**
     * 插入用户当前值
     *
     * @param userId
     * @param maxId
     */
    public void inserData(String userId, int maxId) {
        int pos = checkInLists(userId);
        if (pos == -1) {
            //加入数据中
            list.add(new InsertDbJSON(userId, maxId));
        } else {
            list.get(pos).setMaxId(maxId);
        }
        setDataToSp();
    }

    /**
     * 清空所有
     *
     */
    public void clearAllData() {
        Gson gson = new Gson();
        list.clear();
        String json = gson.toJson(list);
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.DbInfo.DB_USER_LISTS, json);
    }

    /**
     * 把数据存在sp中
     */
    private void setDataToSp() {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.DbInfo.DB_USER_LISTS, json);
    }


    /**
     * 检测在不在缓存中
     *
     * @param userId
     * @return
     */
    private int checkInLists(String userId) {
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUser_id().equals(userId)) {
                position = i;
                break;
            }
        }
        return position;
    }

}
