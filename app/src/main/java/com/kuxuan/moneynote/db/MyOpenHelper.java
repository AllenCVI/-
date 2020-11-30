package com.kuxuan.moneynote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kuxuan.moneynote.MyApplication;
import com.kuxuan.moneynote.servier.ServiceUtil;
import com.kuxuan.moneynote.utils.DBMaxIdOpertor;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.ToastUtil;
import com.kuxuan.sqlite.dao.CategoryDBDao;
import com.kuxuan.sqlite.dao.DaoMaster;

import org.greenrobot.greendao.database.Database;


public class MyOpenHelper extends DaoMaster.OpenHelper {

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * 数据库升级
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //操作数据库的更新 有几个表升级都可以传入到下面
        if (oldVersion < newVersion) {
//            ToastUtil.show(MyApplication.getInstance(), "数据库升级了");
            MigrationHelper.getInstance().migrate(db, CategoryDBDao.class);
            DBMaxIdOpertor.getInstance().clearAllData();
            ToastUtil.show(MyApplication.getInstance(), DBMaxIdOpertor.getInstance().getMaxId(LoginStatusUtil.getLoginUserId() + "") + "");
            ServiceUtil.startDownLoadData(MyApplication.getInstance());
        }

    }

}