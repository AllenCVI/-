package com.kuxuan.moneynote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kuxuan.moneynote.MyApplication;
import com.kuxuan.sqlite.dao.DaoMaster;
import com.kuxuan.sqlite.dao.DaoSession;


public class DbManager {

    // 是否加密
    public static final boolean ENCRYPTED = true;

    private static final String DB_NAME = "moneynote";
    private static DbManager mDbManager;
    private static DaoMaster.DevOpenHelper mDevOpenHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    private Context mContext;

    private DbManager(Context context) {
        this.mContext = context;
        // 初始化数据库信息
        mDevOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME);
        getDaoMaster();
        getmDaoSession();
    }

    public static DbManager getInstance() {
        if (null == mDbManager) {
            synchronized (DbManager.class) {
                if (null == mDbManager) {
                    mDbManager = new DbManager(MyApplication.getInstance());
                }
            }
        }
        return mDbManager;
    }

    /**
     * 获取可读数据库
     *
     * @param context
     * @return
     */
    public static SQLiteDatabase getReadableDatabase(Context context) {
        if (null == mDevOpenHelper) {
            getInstance();
        }
        return mDevOpenHelper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     *
     * @param context
     * @return
     */
    public static SQLiteDatabase getWritableDatabase(Context context) {
        if (null == mDevOpenHelper) {
            getInstance();
        }
        return mDevOpenHelper.getWritableDatabase();
    }

    /**
     * 获取DaoMaster
     * <p>
     * 判断是否存在数据库，如果没有则创建数据库
     *
     * @return
     */
    public  DaoMaster getDaoMaster() {
        if (null == mDaoMaster) {
            synchronized (DbManager.class) {
                if (null == mDaoMaster) {
                    MyOpenHelper helper = new MyOpenHelper(MyApplication.getInstance(), DB_NAME, null);
                    mDaoMaster = new DaoMaster(helper.getWritableDatabase());
                }
            }
        }
        return mDaoMaster;
    }

//    /**
//     * 获取DaoMaster
//     *
//     * @param context
//     * @return
//     */
//    public static DaoMaster getDaoMaster(Context context) {
//        if (null == mDaoMaster) {
//            synchronized (DbManager.class) {
//                if (null == mDaoMaster) {
//
//                    mDaoMaster = new DaoMaster(getWritableDatabase(context));
//                }
//            }
//        }
//        return mDaoMaster;
//    }

    /**
     * 获取DaoSession
     *
     * @return
     */
    public  DaoSession getmDaoSession() {
        if (null == mDaoSession) {
            synchronized (DbManager.class) {
                mDaoSession = getDaoMaster().newSession();
            }
        }

        return mDaoSession;
    }
}