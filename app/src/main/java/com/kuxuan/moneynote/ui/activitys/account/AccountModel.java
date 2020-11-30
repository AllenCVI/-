package com.kuxuan.moneynote.ui.activitys.account;

import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.db.BillCategoreDaoOperator;
import com.kuxuan.moneynote.db.CategoryDBbean;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.CategoryJson;
import com.kuxuan.moneynote.json.netbody.AllCategoryBody;
import com.kuxuan.moneynote.json.netbody.BillBody;
import com.kuxuan.moneynote.listener.MVPListener;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.TimeUtlis;

import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class AccountModel implements AccountContract.AccountModel {
    @Override
    public void getCategoryList(final MVPListener<CategoryJson> listMVPListener, String type) {
        RetrofitClient.getApiService().getCategoryData(new AllCategoryBody(type)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObsever<BaseJson<CategoryJson>>() {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        listMVPListener.onFail(e.message);

                    }

                    @Override
                    public void onSuccess(BaseJson<CategoryJson> arrayListBaseJson) {
                        if (arrayListBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                            listMVPListener.onSuccess(arrayListBaseJson.getData());

                        } else {
                            listMVPListener.onFail(arrayListBaseJson.getError().get(0));

                        }
                    }
                });
    }

    @Override
    public void getNoLoginCategoryList(final MVPListener<CategoryJson> listMVPListener, String type) {
        RetrofitClient.getApiService().getCategoryDataNoLogin(new AllCategoryBody(type)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObsever<BaseJson<CategoryJson>>() {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        listMVPListener.onFail(e.message);

                    }

                    @Override
                    public void onSuccess(BaseJson<CategoryJson> arrayListBaseJson) {
                        if (arrayListBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                            listMVPListener.onSuccess(arrayListBaseJson.getData());

                        } else {
                            listMVPListener.onFail(arrayListBaseJson.getError().get(0));

                        }
                    }
                });
    }

    @Override
    public void addAccount(final MVPListener<Object> listMVPListener, BillBody billBody) {
        RetrofitClient.getApiService().addBill(billBody).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObsever<BaseJson<Object>>() {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        listMVPListener.onFail(e.message);

                    }

                    @Override
                    public void onSuccess(BaseJson<Object> arrayListBaseJson) {
                        if (arrayListBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                            listMVPListener.onSuccess(arrayListBaseJson.getData());

                        } else {
                            listMVPListener.onFail(arrayListBaseJson.getError().get(0));

                        }
                    }
                });
    }

    @Override
    public void uploadBill(final MVPListener<Object> listMVPListener, BillBody billBody) {
        RetrofitClient.getApiService().uploadBill(billBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                listMVPListener.onFail(e.message);
            }

            @Override
            public void onSuccess(BaseJson<Object> objectBaseJson) {
                if (objectBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                    listMVPListener.onSuccess(objectBaseJson.getData());

                } else {
                    listMVPListener.onFail(objectBaseJson.getError().get(0));

                }
            }
        });
    }

    @Override
    public void uploadBillForDB(MVPListener<Object> listMVPListener, BillBody billBody, String name, String bill_id) {
        CategoryDBbean categoryDBbean = new CategoryDBbean();
        categoryDBbean.setType(billBody.getType());
        categoryDBbean.setCategory_id(Integer.parseInt(billBody.getCategory_id()));
        String data = TimeUtlis.getData(Long.parseLong(billBody.getTime() + "000"));
        String[] split = data.split("-");
        categoryDBbean.setYear(Integer.parseInt(split[0]));
        categoryDBbean.setMonth(Integer.parseInt(split[1]));
        categoryDBbean.setDay(Integer.parseInt(split[2]));
        categoryDBbean.setUpdateTime(System.currentTimeMillis());
        categoryDBbean.setTime(Long.parseLong(billBody.getTime() + "000"));
//        categoryDBbean.setCreateTime(Long.parseLong(billBody.getTime() + "000"));
        categoryDBbean.setDemo(billBody.getDemo());
        categoryDBbean.setName(name);
        categoryDBbean.setType_imagepath(BillCategoreDaoOperator.newInstance().getDetaillIconUrlById(Integer.parseInt(billBody.getCategory_id())));
        categoryDBbean.setAccount(Double.parseDouble(billBody.getAccount()));
        categoryDBbean.setBill_id(bill_id);
        CategoryDaoOperator.newInstance().updateData(categoryDBbean);
        listMVPListener.onSuccess(new Object());

    }

    @Override
    public void addAccountForDB(MVPListener<Object> listMVPListener, BillBody billBody, String name) {
        CategoryDBbean categoryDBbean = new CategoryDBbean();
        categoryDBbean.setType(billBody.getType());
        categoryDBbean.setCategory_id(Integer.parseInt(billBody.getCategory_id()));
        String time = billBody.getTime();
        //存储时间
        if (System.currentTimeMillis() - Long.parseLong(billBody.getTime() + "000") < 24 * 60 * 60 * 1000) {
            //在今天
            billBody.setTime(System.currentTimeMillis() + "");
        } else {
            long l = Long.parseLong(time);
            long l1 = l + 22 * 60 * 60;
            billBody.setTime(l1 + "000");
        }
        String data = TimeUtlis.getData(Long.parseLong(billBody.getTime()));
        String[] split = data.split("-");
        categoryDBbean.setYear(Integer.parseInt(split[0]));
        categoryDBbean.setMonth(Integer.parseInt(split[1]));
        categoryDBbean.setDay(Integer.parseInt(split[2]));
        categoryDBbean.setCreateTime(Long.parseLong(billBody.getTime()));
        categoryDBbean.setTime(Long.parseLong(billBody.getTime()));
        categoryDBbean.setUpdateTime(Long.parseLong(billBody.getTime()));
        categoryDBbean.setDemo(billBody.getDemo());
        categoryDBbean.setName(name);
        categoryDBbean.setUser_id(LoginStatusUtil.getLoginUserId());
        categoryDBbean.setType_imagepath(BillCategoreDaoOperator.newInstance().getDetaillIconUrlById(Integer.parseInt(billBody.getCategory_id())));
        categoryDBbean.setAccount(Double.parseDouble(billBody.getAccount()));
        Random random = new Random();
        boolean insert = CategoryDaoOperator.newInstance().insert(categoryDBbean, false);
        if (insert) {
            listMVPListener.onSuccess(new Object());
        } else {
            listMVPListener.onFail("添加失败");
        }
//        categoryDBbean.getUser_id()

    }
}
