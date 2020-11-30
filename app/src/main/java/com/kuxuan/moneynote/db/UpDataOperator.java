package com.kuxuan.moneynote.db;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.kuxuan.moneynote.json.UpJson;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.sqlite.db.CategoryDB;

import java.util.ArrayList;

/**
 * Created by xieshengqi on 2018/4/8.
 */

public class UpDataOperator {

    private static UpDataOperator mInstance;


    private UpDataOperator() {

    }


    public static UpDataOperator getInstance() {
        if (mInstance == null) {
            synchronized (UpDataOperator.class) {
                mInstance = new UpDataOperator();

            }
        }
        return mInstance;
    }


    /**
     * 获得上传的数据
     *
     * @param list
     * @return
     */
    public String getJson(ArrayList<CategoryDB> list) {
        ArrayList<UpJson> jsonlis = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            CategoryDB categoryDB = list.get(i);
            UpJson uploadDbjson = new UpJson();
            uploadDbjson.setAccount(categoryDB.getAccount() + "");
            uploadDbjson.setCategory_id(categoryDB.getCategory_id() + "");
            uploadDbjson.setCategory_name(categoryDB.getName());
            uploadDbjson.setDemo(categoryDB.getDemo());
            uploadDbjson.setIdentification(categoryDB.getBill_id());
            uploadDbjson.setStatus(categoryDB.getStatus() + "");
            String createTime = categoryDB.getCreateTime() + "";
            String updateTime = categoryDB.getUpdateTime() + "";
            String time = categoryDB.getTime() + "";
            if (!TextUtils.isEmpty(time)) {
                if (time.length() >= 13) {
                    uploadDbjson.setTime(categoryDB.getTime() / 1000 + "");
                } else {
                    uploadDbjson.setTime(time);
                }
            } else {
//                uploadDbjson.
                if (createTime.length() >= 13) {
                    uploadDbjson.setTime(categoryDB.getCreateTime() / 1000 + "");
                    uploadDbjson.setCreated_at(categoryDB.getCreateTime() / 1000 + "");
                } else {
                    uploadDbjson.setTime(categoryDB.getCreateTime() + "");
                    uploadDbjson.setCreated_at(categoryDB.getCreateTime() + "");
                }
            }
            if (createTime.length() >= 13) {
//                uploadDbjson.setTime(categoryDB.getCreateTime() / 1000 + "");
                uploadDbjson.setCreated_at(categoryDB.getCreateTime() / 1000 + "");
            } else {
//                uploadDbjson.setTime(categoryDB.getCreateTime() + "");
                uploadDbjson.setCreated_at(categoryDB.getCreateTime() + "");
            }
            if (updateTime.length() >= 13) {
                uploadDbjson.setUpdated_at(categoryDB.getUpdateTime() / 1000 + "");
//                uploadDbjson.setTime(categoryDB.getUpdateTime() / 1000 + "");
            } else {
                uploadDbjson.setUpdated_at(categoryDB.getUpdateTime() + "");
//                uploadDbjson.setTime(categoryDB.getUpdateTime() + "");
            }
            uploadDbjson.setUser_id(LoginStatusUtil.getLoginUserId());
            uploadDbjson.setType(categoryDB.getType() + "");
            jsonlis.add(uploadDbjson);
        }
        Gson gson = new Gson();
        return gson.toJson(jsonlis);

    }
}
