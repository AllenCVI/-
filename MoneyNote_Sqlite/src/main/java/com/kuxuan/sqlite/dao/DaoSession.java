package com.kuxuan.sqlite.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.kuxuan.sqlite.db.BillCategoreDB;
import com.kuxuan.sqlite.db.CategoryDB;
import com.kuxuan.sqlite.db.Test;
import com.kuxuan.sqlite.db.UserDB;

import com.kuxuan.sqlite.dao.BillCategoreDBDao;
import com.kuxuan.sqlite.dao.CategoryDBDao;
import com.kuxuan.sqlite.dao.TestDao;
import com.kuxuan.sqlite.dao.UserDBDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig billCategoreDBDaoConfig;
    private final DaoConfig categoryDBDaoConfig;
    private final DaoConfig testDaoConfig;
    private final DaoConfig userDBDaoConfig;

    private final BillCategoreDBDao billCategoreDBDao;
    private final CategoryDBDao categoryDBDao;
    private final TestDao testDao;
    private final UserDBDao userDBDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        billCategoreDBDaoConfig = daoConfigMap.get(BillCategoreDBDao.class).clone();
        billCategoreDBDaoConfig.initIdentityScope(type);

        categoryDBDaoConfig = daoConfigMap.get(CategoryDBDao.class).clone();
        categoryDBDaoConfig.initIdentityScope(type);

        testDaoConfig = daoConfigMap.get(TestDao.class).clone();
        testDaoConfig.initIdentityScope(type);

        userDBDaoConfig = daoConfigMap.get(UserDBDao.class).clone();
        userDBDaoConfig.initIdentityScope(type);

        billCategoreDBDao = new BillCategoreDBDao(billCategoreDBDaoConfig, this);
        categoryDBDao = new CategoryDBDao(categoryDBDaoConfig, this);
        testDao = new TestDao(testDaoConfig, this);
        userDBDao = new UserDBDao(userDBDaoConfig, this);

        registerDao(BillCategoreDB.class, billCategoreDBDao);
        registerDao(CategoryDB.class, categoryDBDao);
        registerDao(Test.class, testDao);
        registerDao(UserDB.class, userDBDao);
    }
    
    public void clear() {
        billCategoreDBDaoConfig.clearIdentityScope();
        categoryDBDaoConfig.clearIdentityScope();
        testDaoConfig.clearIdentityScope();
        userDBDaoConfig.clearIdentityScope();
    }

    public BillCategoreDBDao getBillCategoreDBDao() {
        return billCategoreDBDao;
    }

    public CategoryDBDao getCategoryDBDao() {
        return categoryDBDao;
    }

    public TestDao getTestDao() {
        return testDao;
    }

    public UserDBDao getUserDBDao() {
        return userDBDao;
    }

}
