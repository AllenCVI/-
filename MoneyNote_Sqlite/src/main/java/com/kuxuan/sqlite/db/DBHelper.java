package com.kuxuan.sqlite.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.kuxuan.sqlite.dao.CategoryDBDao;
import com.kuxuan.sqlite.dao.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by xieshengqi on 2018/4/26.
 */

public class DBHelper extends DaoMaster.OpenHelper {
    public interface onUpdataListener {
        void onUpdata();
    }


    onUpdataListener listener;

    public DBHelper(Context context, String name) {
        super(context, name);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);

    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, onUpdataListener listener) {
        super(context, name, factory);
        this.listener = listener;

    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        if (newVersion > oldVersion) {
            if (newVersion == 4) {
                if (listener != null)
                    listener.onUpdata();
            }
        }
        MigrationHelper.migrate(db, CategoryDBDao.class);

    }
}
