package com.kuxuan.moneynote.db;

import com.kuxuan.sqlite.dao.BillCategoreDBDao;
import com.kuxuan.sqlite.db.BillCategoreDB;

import java.util.List;

/**
 * Created by Allence on 2018/3/30 0030.
 */

public class BillCategoreDaoOperator {


    public static BillCategoreDaoOperator newInstance(){

        return new BillCategoreDaoOperator();
    }


    /**
     * 添加一条数据
     * @param billCategoreDB
     * @return
     */
    public synchronized boolean insert(BillCategoreDB billCategoreDB){

        boolean isSuccess = false;
        BillCategoreDBDao billCategoreDBDao = DbManager.getInstance().getmDaoSession().getBillCategoreDBDao();

        try {

            long insert = billCategoreDBDao.insertOrReplace(billCategoreDB);

        } catch (Exception e) {

        }
        return isSuccess;
    }


    /**
     * 添加一堆数据
     * @param billCategoreDBList
     * @return
     */

    public synchronized boolean insertList(List<BillCategoreDB> billCategoreDBList){

        boolean isSuccess = false;
        BillCategoreDBDao billCategoreDBDao = DbManager.getInstance().getmDaoSession().getBillCategoreDBDao();

        try {
            billCategoreDBDao.insertOrReplaceInTx(billCategoreDBList);
            isSuccess = true;
        } catch (Exception e) {

        }

        return isSuccess;
    }


    /**
     * 根据id查询
     * @return
     */
    public synchronized BillCategoreDB getCategoreDataById(int id){

        List<BillCategoreDB> billCategoreDBList=null;
        BillCategoreDB billCategoreDB=null;

        try {
            BillCategoreDBDao billCategoreDBDao = DbManager.getInstance().getmDaoSession().getBillCategoreDBDao();
            billCategoreDBList = billCategoreDBDao.queryBuilder().where(BillCategoreDBDao.Properties.Id.eq(id)).list();
            billCategoreDB = billCategoreDBList.get(0);

        } catch (Exception e) {

        }

        return billCategoreDB;
    }



    public synchronized String getDetaillIconUrlById(int id){

        String detail_icon = null;
        List<BillCategoreDB> billCategoreDBList=null;
        BillCategoreDB billCategoreDB=null;

        try {
            BillCategoreDBDao billCategoreDBDao = DbManager.getInstance().getmDaoSession().getBillCategoreDBDao();
            billCategoreDBList = billCategoreDBDao.queryBuilder().where(BillCategoreDBDao.Properties.Id.eq(id)).list();
            billCategoreDB = billCategoreDBList.get(0);
            detail_icon = billCategoreDB.getDetail_icon();
        } catch (Exception e) {
            return "file:///android_asset/zidingyi.png";
        }

        return  detail_icon;
    }





    /**
     * 获得所有的数据
     * @return
     */
    public synchronized List<BillCategoreDB> getAll(){

        List<BillCategoreDB> billCategoreDBList=null;

        try {
            BillCategoreDBDao billCategoreDBDao = DbManager.getInstance().getmDaoSession().getBillCategoreDBDao();
            billCategoreDBList = billCategoreDBDao.loadAll();

        } catch (Exception e) {

        }
        return billCategoreDBList;
    }


    /**
     * 通过type查数据
     * @param type
     * @return
     */
    public synchronized List<BillCategoreDB> getType(int type){

        List<BillCategoreDB> billCategoreDBList=null;

        try {
            BillCategoreDBDao billCategoreDBDao = DbManager.getInstance().getmDaoSession().getBillCategoreDBDao();
            billCategoreDBList = billCategoreDBDao.queryBuilder().where(BillCategoreDBDao.Properties.Type.eq(type)).list();

        } catch (Exception e) {

        }

        return billCategoreDBList;

    }










}
