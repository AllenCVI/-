package com.kuxuan.moneynote.db;

import android.util.Log;

import com.kuxuan.moneynote.MyApplication;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.BillJsonList;
import com.kuxuan.moneynote.json.TimeJson;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.SPUtil;
import com.kuxuan.moneynote.utils.TextSetUtil;
import com.kuxuan.moneynote.utils.TimeUtlis;
import com.kuxuan.sqlite.dao.CategoryDBDao;
import com.kuxuan.sqlite.db.CategoryDB;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 数据库获取数据类
 * Created by xieshengqi on 2018/3/27.
 */

public class CategoryDaoOperator {

    private final String TAG = "CategoryDaoOperator";

    public static final int SUCCESS = 1;

    OnDBChangeListener mListener;


    private static CategoryDaoOperator mInstance;


    public void setOnDBChangeListener(OnDBChangeListener changeListener) {
        mListener = changeListener;
    }

    private CategoryDaoOperator() {

    }


    public interface OnDBChangeListener {


        void onStart();


        void onSuccess(long code);


        void fail();


    }


    public static CategoryDaoOperator newInstance() {

        if (mInstance == null) {
            synchronized (CategoryDaoOperator.class) {
                mInstance = new CategoryDaoOperator();
            }
        }
        return mInstance;

    }


    /**
     * 插入数据
     *
     * @param categoryDBbean
     * @param isSync         是不是网络同步
     * @return
     */
    public synchronized boolean insert(CategoryDBbean categoryDBbean, boolean isSync) {
        Log.i(TAG, "insert");
        start();
        boolean isSuccess = false;
        DbManager.getInstance().getmDaoSession().getCategoryDBDao().insertInTx();
        CategoryDBDao categoryDBDao = DbManager.getInstance().getmDaoSession().getCategoryDBDao();
//        while (!isInDb(categoryDBbean.getBill_id())) {
//// TODO: 2018/3/27  修改账单id变得唯一
//
//        }
        Integer id = (Integer) SPUtil.get(MyApplication.getInstance(), Constant.DbInfo.DB_ID_COUNT, -1);
//        if (count == -1) {
//            count = 1;
//        } else {
//            count++;
//        }
        id = id + 1;
        SPUtil.putAndApply(MyApplication.getInstance(), Constant.DbInfo.DB_ID_COUNT, id);
        String bill_id = null;
        boolean isNeedSync = true;
        if (isSync) {
            isNeedSync = false;
            bill_id = categoryDBbean.getBill_id() + "";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            TimeJson currentTime = TimeUtlis.getCurrentTimeForhhmmss();
            long createTime = currentTime.getCurrentTime();
            if (String.valueOf(createTime).length() < 13) {
                createTime = createTime * 1000;
            }
            String s = String.valueOf(createTime);
            //毫秒4位数
            String ms = s.substring(s.length() - 4, s.length());
            int month = currentTime.getMonth();
            int day = currentTime.getDay();
            String stringMonth = null;
            String stringDay = null;
            if (month < 10) {
                stringMonth = "0" + month;
            } else {
                stringMonth = month + "";
            }
            if (day < 10) {
                stringDay = "0" + day;
            } else {
                stringDay = day + "";
            }
            stringBuilder.append(currentTime.getYear() + "" + stringMonth + "" + stringDay + currentTime.getHh() + "" + currentTime.getMm() + "" + currentTime.getSs() + ms + ((int) ((Math.random() * 9 + 1) * 1000)));
            bill_id = stringBuilder.toString();
        }
        try {
//            if (isInDB(bill_id, categoryDBbean.getUser_id())) {
//                updateData(categoryDBbean);
//                Log.e("更新数据",categoryDBbean.toString());
//            } else {
//                CategoryDB categoryDB = new CategoryDB((long) id, bill_id, categoryDBbean.getDemo(), categoryDBbean.getName(), categoryDBbean.getType(), categoryDBbean.getType_imagepath(), categoryDBbean.getAccount(), categoryDBbean.getCategory_id(), categoryDBbean.getYear(), categoryDBbean.getMonth(), categoryDBbean.getDay(), categoryDBbean.getCreateTime(), categoryDBbean.getUpdateTime(), categoryDBbean.getStatus(), categoryDBbean.getUser_id(), isNeedSync);
//                Log.e("插入数据",categoryDB.toString());
//                long insert = categoryDBDao.insertOrReplace(categoryDB);
//                isSuccess = true;
//                onSuccess(insert);
//            }
            CategoryDB categoryDB = new CategoryDB((long) id, bill_id, categoryDBbean.getDemo(), categoryDBbean.getName(), categoryDBbean.getType(), categoryDBbean.getType_imagepath(), categoryDBbean.getAccount(), categoryDBbean.getCategory_id(), categoryDBbean.getYear(), categoryDBbean.getMonth(), categoryDBbean.getDay(), categoryDBbean.getCreateTime(), categoryDBbean.getUpdateTime(), categoryDBbean.getTime(), categoryDBbean.getStatus(), categoryDBbean.getUser_id(), isNeedSync);
            Log.e("插入数据", categoryDB.toString());
            long insert = categoryDBDao.insertOrReplace(categoryDB);
            isSuccess = true;
            onSuccess(insert);
        } catch (Exception e) {
            //更新数据库（已经存在）
//            updateData(categoryDBbean);

        }
        return isSuccess;
    }

    /**
     * 插入数据
     *
     * @param bill_id
     * @param demo
     * @param name
     * @param type
     * @param type_img
     * @param account
     * @param category_id
     * @param year
     * @param month
     * @param day
     * @param createTime
     * @param updateTime
     * @param status
     * @param user_id
     * @param sync
     */
    public synchronized void insert(Long id, String bill_id, String demo, String name, int type, String type_img, double account, int category_id, int year, int month, int day, long createTime, long updateTime, long time, int status, int user_id, boolean sync) {
        CategoryDBDao categoryDBDao = DbManager.getInstance().getmDaoSession().getCategoryDBDao();
        CategoryDB categoryDB = new CategoryDB(id, bill_id, demo, name, type, type_img, account, category_id, year, month, day, createTime, updateTime, time, status, user_id, sync);
        long insert = categoryDBDao.insertOrReplace(categoryDB);
    }

    /**
     * 批量插入
     *
     * @param list
     */
    public synchronized void insert(ArrayList<CategoryDB> list) {
        CategoryDBDao categoryDBDao = DbManager.getInstance().getmDaoSession().getCategoryDBDao();
        categoryDBDao.insertOrReplaceInTx(list);
    }

    /**
     * 判断该账单是否存在
     *
     * @param bill_id
     * @param user_id
     * @return
     */
    private synchronized boolean isInDB(String bill_id, int user_id) {
        List<CategoryDB> list = DbManager.getInstance().getmDaoSession().getCategoryDBDao().queryBuilder().where(CategoryDBDao.Properties.User_id.eq(user_id), CategoryDBDao.Properties.Bill_id.eq(bill_id)).list();
        if (list == null || list.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void start() {
        if (mListener != null) {
            mListener.onStart();
        }
    }

    private void onSuccess(long code) {
        if (mListener != null) {
            mListener.onSuccess(code);
        }
    }


    /**
     * 看看账单是否存在数据库
     *
     * @param bill_id
     * @return
     */
    private boolean isInDb(long bill_id) {
        CategoryDBDao categoryDBDao = DbManager.getInstance().getmDaoSession().getCategoryDBDao();
        List<CategoryDB> list = categoryDBDao.queryBuilder().where(CategoryDBDao.Properties.Bill_id.eq(bill_id)).list();
        if (list != null && list.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据id查找数据
     *
     * @param bill_id
     * @return
     */
    private CategoryDB getCategory(String bill_id) {
        CategoryDBDao categoryDBDao = DbManager.getInstance().getmDaoSession().getCategoryDBDao();
        List<CategoryDB> list = categoryDBDao.queryBuilder().where(CategoryDBDao.Properties.Bill_id.eq(bill_id)).list();
        if (list != null && list.size() != 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 更新账单
     *
     * @param categoryDBbean
     * @return
     */
    public synchronized void updateData(CategoryDBbean categoryDBbean) {
//        Log.i(TAG,"updateData");
        start();
        CategoryDBDao categoryDBDao = DbManager.getInstance().getmDaoSession().getCategoryDBDao();
        CategoryDB category = getCategory(categoryDBbean.getBill_id() + "");
        category.setAccount(categoryDBbean.getAccount());
        category.setCategory_id(categoryDBbean.getCategory_id());
        category.setDemo(categoryDBbean.getDemo());
        category.setName(categoryDBbean.getName());
        category.setType(categoryDBbean.getType());
        //需要同步
        category.setIsNeedSync(true);
        category.setImage_path(BillCategoreDaoOperator.newInstance().getDetaillIconUrlById(category.getCategory_id()));
        category.setDay(categoryDBbean.getDay());
        category.setMonth(categoryDBbean.getMonth());
        category.setYear(categoryDBbean.getYear());
//        if(category.getCreateTime()!=categoryDBbean.getCreateTime()){
//            category.setCreateTime(categoryDBbean.getCreateTime());
//        }
        category.setTime(categoryDBbean.getTime());
        category.setUpdateTime(categoryDBbean.getUpdateTime());
        categoryDBDao.update(category);
        onSuccess(SUCCESS);
    }


    /**
     * 设置数据库的数据不用同步
     *
     * @param bill_id
     */
    public synchronized void updataNoNeedSynce(String bill_id) {
//        Log.i(TAG,"updataNoNeedSynce");
        CategoryDB category = getCategory(bill_id);
        category.setIsNeedSync(false);
        category.setUser_id(LoginStatusUtil.getLoginUserId());
        DbManager.getInstance().getmDaoSession().getCategoryDBDao().update(category);
    }

    /**
     * 设置数据库数据需要同步
     *
     * @param bill_id
     */
    public synchronized void updataNeedSynce(String bill_id) {
//        Log.i(TAG,"updataNeedSynce");
        CategoryDB category = getCategory(bill_id);
        category.setIsNeedSync(true);
        category.setUser_id(LoginStatusUtil.getLoginUserId());
        DbManager.getInstance().getmDaoSession().getCategoryDBDao().update(category);
    }

    /**
     * 设置此项是否为删除项
     *
     * @param bill_id
     * @param isDelete
     */
    public synchronized void updataIsDelete(String bill_id, boolean isDelete) {
//        Log.i(TAG,"updataIsDelete");
        CategoryDB category = getCategory(bill_id);
        //修改是否删除
        category.setStatus(isDelete ? 1 : 0);
        //需要同步
        category.setIsNeedSync(true);
        category.setUpdateTime(System.currentTimeMillis() / 1000);
        DbManager.getInstance().getmDaoSession().getCategoryDBDao().update(category);
    }

    /**
     * 判断该账户下该类别是不是有账单
     *
     * @param type
     */
    public synchronized boolean checkIsHaveTypelists(String type) {
        List<CategoryDB> list = DbManager.getInstance().getmDaoSession().getCategoryDBDao().queryBuilder().where(CategoryDBDao.Properties.User_id.eq(LoginStatusUtil.getLoginUserId()), CategoryDBDao.Properties.Status.eq(0), CategoryDBDao.Properties.Category_id.eq(type)).limit(1).list();
        if (list == null || list.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 删除该类别下的用户账单
     *
     * @param category_id
     */
    public synchronized void deleteCategoryData(int category_id) {
        CategoryDBDao categoryDBDao = DbManager.getInstance().getmDaoSession().getCategoryDBDao();
        List<CategoryDB> list = categoryDBDao.queryBuilder().where(CategoryDBDao.Properties.User_id.eq(LoginStatusUtil.getLoginUserId()), CategoryDBDao.Properties.Status.eq(0), CategoryDBDao.Properties.Category_id.eq(category_id)).list();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                categoryDBDao.delete(list.get(i));
            }
        }
    }


    /**
     * 删除账单
     *
     * @param bill_id
     */
    public synchronized void deleteData(String bill_id) {
//        Log.i(TAG,"deleteData");
        start();
        try {
            DbManager.getInstance().getmDaoSession().getCategoryDBDao().delete(getCategory(bill_id));
        } catch (Exception e) {
        }
        onSuccess(SUCCESS);
    }


    //自定义查找返回所需要的数据类型

    /**
     * 获取最大和最小的时间(慎用，操作太多)
     */
    public synchronized HashMap<String, Integer> getMaxAndMinTime(int type, int category_id) {
//        Log.i(TAG,"getMaxAndMinTime");
        start();
        CategoryDBDao categoryDBDao = DbManager.getInstance().getmDaoSession().getCategoryDBDao();
        List<CategoryDB> list = null;
        if (category_id != -1) {
            list = getQuilder().where(CategoryDBDao.Properties.Type.eq(type), CategoryDBDao.Properties.Category_id.eq(category_id)).orderAsc(CategoryDBDao.Properties.Year).list();
        } else {
            list = getQuilder().where(CategoryDBDao.Properties.Type.eq(type)).orderAsc(CategoryDBDao.Properties.Year).list();
        }
        int startYear = 0;
        int endYear = 0;
        HashMap<String, Integer> map = null;
        if (list == null || list.size() == 0) {
            return map;
        } else {
            map = new HashMap<>();
            startYear = list.get(0).getYear();
            endYear = list.get(list.size() - 1).getYear();
        }
//查找开始年最小的年
        int startMonth = 1;
        int endMonth = 1;
        int startDay = 1;
        int endDay = 1;
        if (startYear == endYear) {
            List<CategoryDB> monthLists = null;
            if (category_id != -1) {
                monthLists = getQuilder().where(CategoryDBDao.Properties.Type.eq(type), CategoryDBDao.Properties.Year.eq(startYear), CategoryDBDao.Properties.Category_id.eq(category_id)).orderAsc(CategoryDBDao.Properties.Month).list();
            } else {
                monthLists = getQuilder().where(CategoryDBDao.Properties.Type.eq(type), CategoryDBDao.Properties.Year.eq(startYear)).orderAsc(CategoryDBDao.Properties.Month).list();
            }
            startMonth = monthLists.get(0).getMonth();
            endMonth = monthLists.get(monthLists.size() - 1).getMonth();
        } else {
            List<CategoryDB> monthLists1 = null;
            if (category_id != -1) {
                monthLists1 = getQuilder().where(CategoryDBDao.Properties.Type.eq(type), CategoryDBDao.Properties.Year.eq(startYear), CategoryDBDao.Properties.Category_id.eq(category_id)).orderAsc(CategoryDBDao.Properties.Month).list();

            } else {
                monthLists1 = getQuilder().where(CategoryDBDao.Properties.Type.eq(type), CategoryDBDao.Properties.Year.eq(startYear)).orderAsc(CategoryDBDao.Properties.Month).list();

            }
            startMonth = monthLists1.get(0).getMonth();
            List<CategoryDB> monthLists2 = null;
            if (category_id != -1) {
                monthLists2 = getQuilder().where(CategoryDBDao.Properties.Type.eq(type), CategoryDBDao.Properties.Year.eq(endYear), CategoryDBDao.Properties.Category_id.eq(category_id)).orderAsc(CategoryDBDao.Properties.Month).list();

            } else {
                monthLists2 = getQuilder().where(CategoryDBDao.Properties.Type.eq(type), CategoryDBDao.Properties.Year.eq(endYear)).orderAsc(CategoryDBDao.Properties.Month).list();

            }
            endMonth = monthLists2.get(monthLists2.size() - 1).getMonth();
        }
        //查找开始日期和结束日期
        List<CategoryDB> daylist1 = null;
        if (category_id != -1) {
            daylist1 = getQuilder().where(CategoryDBDao.Properties.Type.eq(type), CategoryDBDao.Properties.Year.eq(startYear), CategoryDBDao.Properties.Month.eq(startMonth), CategoryDBDao.Properties.Category_id.eq(category_id)).orderAsc(CategoryDBDao.Properties.Day).list();
        } else {
            daylist1 = getQuilder().where(CategoryDBDao.Properties.Type.eq(type), CategoryDBDao.Properties.Year.eq(startYear), CategoryDBDao.Properties.Month.eq(startMonth)).orderAsc(CategoryDBDao.Properties.Day).list();

        }
        startDay = daylist1.get(0).getDay();
        List<CategoryDB> daylist2 = null;
        if (category_id != -1) {
            daylist2 = getQuilder().where(CategoryDBDao.Properties.Type.eq(type), CategoryDBDao.Properties.Year.eq(endYear), CategoryDBDao.Properties.Month.eq(endMonth), CategoryDBDao.Properties.Category_id.eq(category_id)).orderAsc(CategoryDBDao.Properties.Day).list();
        } else {
            daylist2 = getQuilder().where(CategoryDBDao.Properties.Type.eq(type), CategoryDBDao.Properties.Year.eq(endYear), CategoryDBDao.Properties.Month.eq(endMonth)).orderAsc(CategoryDBDao.Properties.Day).list();

        }
        endDay = daylist2.get(daylist2.size() - 1).getDay();

        map.put("startYear", startYear);
        map.put("endYear", endYear);
        map.put("startMonth", startMonth);
        map.put("endMonth", endMonth);
        map.put("startDay", startDay);
        map.put("endDay", endDay);
        onSuccess(SUCCESS);
        return map;
    }

    /**
     * 明细页数据时间段 当user_id为-1时代表未登录用户,当user_id为其他值时，代表用户已经登录
     * 获取最大和最小的时间(慎用，操作太多)
     */
    public synchronized HashMap<String, Integer> getMaxAndMinTimeForUserId(int userId) {
//        Log.i(TAG,"getMaxAndMinTimeForUserId");
        start();
        CategoryDBDao categoryDBDao = DbManager.getInstance().getmDaoSession().getCategoryDBDao();
        List<CategoryDB> list = null;
        list = getQuilder().orderAsc(CategoryDBDao.Properties.Year).list();
        int startYear = 0;
        int endYear = 0;
        HashMap<String, Integer> map = null;
        if (list == null || list.size() == 0) {
            return map;
        } else {
            map = new HashMap<>();
            startYear = list.get(0).getYear();
            endYear = list.get(list.size() - 1).getYear();
        }
//查找开始年最小的年
        int startMonth = 1;
        int endMonth = 1;
        int startDay = 1;
        int endDay = 1;
        if (startYear == endYear) {
            List<CategoryDB> monthLists = getQuilder().where(CategoryDBDao.Properties.Year.eq(startYear)).orderAsc(CategoryDBDao.Properties.Month).list();
            startMonth = monthLists.get(0).getMonth();
            endMonth = monthLists.get(monthLists.size() - 1).getMonth();
        } else {
            List<CategoryDB> monthLists1 = getQuilder().where(CategoryDBDao.Properties.Year.eq(startYear)).orderAsc(CategoryDBDao.Properties.Month).list();
            startMonth = monthLists1.get(0).getMonth();
            List<CategoryDB> monthLists2 = getQuilder().where(CategoryDBDao.Properties.Year.eq(endYear)).orderAsc(CategoryDBDao.Properties.Month).list();
            endMonth = monthLists2.get(monthLists2.size() - 1).getMonth();
        }
        //查找开始日期和结束日期
        List<CategoryDB> daylist1 = getQuilder().where(CategoryDBDao.Properties.Year.eq(startYear), CategoryDBDao.Properties.Month.eq(startMonth)).orderAsc(CategoryDBDao.Properties.Day).list();
        startDay = daylist1.get(0).getDay();
        List<CategoryDB> daylist2 = getQuilder().where(CategoryDBDao.Properties.Year.eq(endYear), CategoryDBDao.Properties.Month.eq(endMonth)).orderAsc(CategoryDBDao.Properties.Day).list();
        endDay = daylist2.get(daylist2.size() - 1).getDay();

        map.put("startYear", startYear);
        map.put("endYear", endYear);
        map.put("startMonth", startMonth);
        map.put("endMonth", endMonth);
        map.put("startDay", startDay);
        map.put("endDay", endDay);
        onSuccess(SUCCESS);
        return map;
    }

    /**
     * 获取周的一天的账单
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public ArrayList<CategoryDB> getWeekData(int year, int month, int day, int type, int category_id) {
        if (category_id != -1) {
            return (ArrayList<CategoryDB>) getQuilder().where(CategoryDBDao.Properties.Year.eq(year), CategoryDBDao.Properties.Month.eq(month), CategoryDBDao.Properties.Day.eq(day), CategoryDBDao.Properties.Category_id.eq(category_id), CategoryDBDao.Properties.Type.eq(type)).orderDesc(CategoryDBDao.Properties.Account).list();

        } else {
            return (ArrayList<CategoryDB>) getQuilder().where(CategoryDBDao.Properties.Year.eq(year), CategoryDBDao.Properties.Month.eq(month), CategoryDBDao.Properties.Day.eq(day), CategoryDBDao.Properties.Type.eq(type)).orderDesc(CategoryDBDao.Properties.Account).list();

        }
    }


    /**
     * 获取某年某月支出或者收入的数据
     *
     * @param year
     * @param month
     * @param type
     * @return
     */
    public ArrayList<CategoryDB> getMonthData(int year, int month, int type) {

        ArrayList<CategoryDB> list = null;

        try {
            list = (ArrayList<CategoryDB>) getQuilder().where(CategoryDBDao.Properties.Year.eq(year), CategoryDBDao.Properties.Month.eq(month), CategoryDBDao.Properties.Type.eq(type)).list();
        } catch (Exception e) {
        }

        return list;
    }


    /**
     * 获取某个类别的一个月的数据
     *
     * @param year
     * @param month
     * @return
     */
    public ArrayList<CategoryDB> getMonthData(int year, int month, int type, int category_id) {
        List<CategoryDB> list = null;
        if (category_id != -1) {
            list = getQuilder().where(CategoryDBDao.Properties.Year.eq(year), CategoryDBDao.Properties.Month.eq(month), CategoryDBDao.Properties.Category_id.eq(category_id), CategoryDBDao.Properties.Type.eq(type)).orderDesc(CategoryDBDao.Properties.Account).list();
        } else {
            list = getQuilder().where(CategoryDBDao.Properties.Year.eq(year), CategoryDBDao.Properties.Month.eq(month), CategoryDBDao.Properties.Type.eq(type)).orderDesc(CategoryDBDao.Properties.Account).list();

        }
        return (ArrayList<CategoryDB>) list;
    }

    /**
     * 我的界面获取某月数据
     *
     * @param year
     * @param month
     * @return
     */
    public BillJsonList getMonthData(int year, int month) {

        BillJsonList billJsonList = null;

        CategoryDaoOperator categoryDaoOperator = CategoryDaoOperator.newInstance();

        //收入 type ==1
        ArrayList<CategoryDB> categoryDBArrayList_Income = categoryDaoOperator.getMonthData(year, month, 1);

        double income = 0;

        for (int i = 0; i < categoryDBArrayList_Income.size(); i++) {

            income = income + categoryDBArrayList_Income.get(i).getAccount();

        }

        ArrayList<CategoryDB> categoryDBArrayList_expenditure = categoryDaoOperator.getMonthData(year, month, 2);

        double expenditure = 0;

        for (int i = 0; i < categoryDBArrayList_expenditure.size(); i++) {

            expenditure = expenditure + categoryDBArrayList_expenditure.get(i).getAccount();

        }

        double surplus = 0;

        surplus = income - expenditure;


        String incomeStr = TextSetUtil.formatFloatNumber(income);
        String expenditureStr = TextSetUtil.formatFloatNumber(expenditure);
        String surplusStr = TextSetUtil.formatFloatNumber(surplus);


        return new BillJsonList(incomeStr, expenditureStr, surplusStr, month + "");
    }


    /**
     * 获取某年的支出或者收入的数据
     *
     * @param year
     * @param type
     * @return
     */
    public ArrayList<CategoryDB> getYearData(int year, int type) {


        ArrayList<CategoryDB> list = null;

        try {
            list = (ArrayList<CategoryDB>) getQuilder().where(CategoryDBDao.Properties.Year.eq(year), CategoryDBDao.Properties.Type.eq(type)).list();
        } catch (Exception e) {

        }

        return list;
    }


    /**
     * 获取某个类别的一年月的数据
     *
     * @param year
     * @param type
     * @param category_id
     * @return
     */
    public ArrayList<CategoryDB> getYearData(int year, int type, int category_id) {
        List<CategoryDB> list = null;
        if (category_id != -1) {
            list = getQuilder().where(CategoryDBDao.Properties.Year.eq(year), CategoryDBDao.Properties.Category_id.eq(category_id), CategoryDBDao.Properties.Type.eq(type)).orderDesc(CategoryDBDao.Properties.Account).list();

        } else {
            list = getQuilder().where(CategoryDBDao.Properties.Year.eq(year), CategoryDBDao.Properties.Type.eq(type)).orderDesc(CategoryDBDao.Properties.Account).list();

        }
        return (ArrayList<CategoryDB>) list;
    }

    /**
     * 获取这一年中用户数据
     *
     * @param year
     * @return
     */
    public synchronized ArrayList<CategoryDB> getYearData(int year) {
        List<CategoryDB> list = null;
        list = getQuilder().where(CategoryDBDao.Properties.Year.eq(year)).list();
        return (ArrayList<CategoryDB>) list;
    }

    /**
     * 获取某年某月某日数据
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public synchronized ArrayList<CategoryDB> getYMDData(int year, int month, int day) {
        List<CategoryDB> list = null;
        list = getQuilder().where(CategoryDBDao.Properties.Year.eq(year), CategoryDBDao.Properties.Month.eq(month), CategoryDBDao.Properties.Day.eq(day)).list();
        return (ArrayList<CategoryDB>) list;
    }

    /**
     * 获取某年某月数据
     *
     * @param year
     * @param month
     * @return
     */
    public synchronized ArrayList<CategoryDB> getYMData(int year, int month) {
        List<CategoryDB> list = null;
        list = getQuilder().where(CategoryDBDao.Properties.Year.eq(year), CategoryDBDao.Properties.Month.eq(month)).list();
        return (ArrayList<CategoryDB>) list;
    }


    /**
     * 根据账单名字获得数据
     *
     * @param data
     * @return
     */
    public synchronized ArrayList<CategoryDB> getTypeName(String data) {
        List<CategoryDB> list = null;
        list = getQuilder().where(CategoryDBDao.Properties.Name.eq(data)).list();
        return (ArrayList<CategoryDB>) list;
    }

    /**
     * 查询是否包含于data的数据
     *
     * @param data
     * @return
     */
    public synchronized ArrayList<CategoryDB> getDemoName(String data) {
        List<CategoryDB> list = null;
        list = getQuilder().where(CategoryDBDao.Properties.Demo.like("%" + data + "%")).list();
        return (ArrayList<CategoryDB>) list;
    }

    /**
     * 获取明细页面的账单
     *
     * @param year
     * @param monnth
     * @param day
     * @return
     */
    public ArrayList<CategoryDB> getCategoryForDay(int year, int monnth, int day) {
        List<CategoryDB> list = getQuilder().where(CategoryDBDao.Properties.Year.eq(year), CategoryDBDao.Properties.Month.eq(monnth), CategoryDBDao.Properties.Day.eq(day)).orderAsc(CategoryDBDao.Properties.UpdateTime).list();
        return (ArrayList<CategoryDB>) list;
    }

    /**
     * 共同的查询条件
     */
    private QueryBuilder<CategoryDB> getQuilder() {
        boolean loginin = LoginStatusUtil.isLoginin();
        if (loginin) {
            //如果登录查询的是所有这个用户和未标记的数据
            return DbManager.getInstance().getmDaoSession().getCategoryDBDao().queryBuilder().where(CategoryDBDao.Properties.Status.eq(0)).whereOr(CategoryDBDao.Properties.User_id.eq(LoginStatusUtil.getLoginUserId()), CategoryDBDao.Properties.User_id.eq(-1));
        } else {
            //只查询未标记的数据
            return DbManager.getInstance().getmDaoSession().getCategoryDBDao().queryBuilder().where(CategoryDBDao.Properties.User_id.eq(LoginStatusUtil.getLoginUserId()), CategoryDBDao.Properties.Status.eq(0));
        }
    }

    /**
     * 返回需要上传的数据
     *
     * @return
     */
    public ArrayList<CategoryDB> getNeedUpdataJson() {
        List<CategoryDB> list = DbManager.getInstance().getmDaoSession().getCategoryDBDao().queryBuilder().whereOr(CategoryDBDao.Properties.User_id.eq(LoginStatusUtil.getLoginUserId()), CategoryDBDao.Properties.User_id.eq(-1)).where(CategoryDBDao.Properties.IsNeedSync.eq(true)).list();
        return (ArrayList<CategoryDB>) list;
    }


    /**
     * 获取最后一条id
     *
     * @return
     */
    public synchronized int getLastID() {
        long id = 0l;
        List<CategoryDB> list = DbManager.getInstance().getmDaoSession().getCategoryDBDao().queryBuilder().orderDesc(CategoryDBDao.Properties.Id).limit(1).list();
        if (list != null && list.size() != 0) {
            id = list.get(0).getId();
//            Log.e("最后一条数据是", id + "");
        }
        return (int) id;
    }


}
