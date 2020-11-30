package com.kuxuan.moneynote.ui.activitys.account;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.json.CategoryJson;
import com.kuxuan.moneynote.json.CategoryList;
import com.kuxuan.moneynote.json.netbody.BillBody;
import com.kuxuan.moneynote.listener.MVPListener;
import com.kuxuan.moneynote.listener.UpDBListener;
import com.kuxuan.moneynote.ui.activitys.category.CategoryActivity;
import com.kuxuan.moneynote.ui.activitys.login.LoginActivity;
import com.kuxuan.moneynote.ui.adapter.AccountAdapter;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.NetWorkUtil;
import com.kuxuan.moneynote.utils.UIHelper;
import com.kuxuan.moneynote.utils.UpDBDataOpertor;
import com.kuxuan.sqlite.db.CategoryDB;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class AccountPresenter extends AccountContract.AccountPresent {
    AccountAdapter mAdapter;
    Context mContext;
    ClickListener mClickListener;
    List<CategoryList> mCategoryList;
    int a[] = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
    private static final String TAG = "AccountPresenter";


    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void initRecyclerView(final Context context, final RecyclerView recyclerView, int type, final ClickListener mClickListener) {
        mContext = context;
        GridLayoutManager mgr = new GridLayoutManager(context, 4);
        recyclerView.setLayoutManager(mgr);
        this.mClickListener = mClickListener;
        mAdapter = new AccountAdapter(R.layout.cell_account_item, mCategoryList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e(TAG, "onItemClick: ");
//                changeColor(recyclerView,position);
                if (position == mCategoryList.size() - 1) {
                    if (LoginStatusUtil.isLoginin()) {
                        UIHelper.openActivity(mContext, CategoryActivity.class);
                    } else {
                        UIHelper.openActivity(mContext, LoginActivity.class);
                    }
                    return;
                }
                for (int i = 0; i < mCategoryList.size(); i++) {
                    CategoryList account = mCategoryList.get(i);
                    if (i == position) {
                        account.setClick(true);
                    } else {
                        account.setClick(false);
                    }
                }
                mAdapter.notifyDataSetChanged();
                mClickListener.click(mCategoryList.get(position).getId(), mCategoryList.get(position).getType(), mCategoryList.get(position).getIcon(), mCategoryList.get(position));
            }
        });

    }

    @Override
    void getCategoryList(String type) {
        mModel.getCategoryList(new MVPListener<CategoryJson>() {
            @Override
            public void onSuccess(CategoryJson content) {
                view.hideProgress();
                mCategoryList = content.getSystem();
                if (content.getCustom() != null)
                    mCategoryList.addAll(content.getCustom());
                mCategoryList.add(new CategoryList("0", "设置", 12, "11", 11));
                mAdapter.setNewData(mCategoryList);
                mAdapter.notifyDataSetChanged();
//                view.getCategorySuccess();
            }

            @Override
            public void onFail(String msg) {
                Log.e("msg", "ss");
            }
        }, type);
    }


    @Override
    void getNoLoginCategoryList(String type) {
        mModel.getNoLoginCategoryList(new MVPListener<CategoryJson>() {
            @Override
            public void onSuccess(CategoryJson content) {
                view.hideProgress();
                mCategoryList = content.getSystem();
                if (content.getCustom() != null)
                    mCategoryList.addAll(content.getCustom());
                mCategoryList.add(new CategoryList("0", "设置", 12, "11", 11));
                mAdapter.setNewData(mCategoryList);
                mAdapter.notifyDataSetChanged();
//                view.getCategorySuccess();
            }

            @Override
            public void onFail(String msg) {
                Log.e("msg", "ss");
            }
        }, type);
    }

    @Override
    void addAccount(BillBody billBody, int online_OR_Offline, String name) {
        view.showProgress();
        // TODO: 2018/4/3 需要做没有网络和有网络的处理


        //添加到网上
//        mModel.addAccount(new MVPListener<Object>() {
//            @Override
//            public void onSuccess(Object content) {
//                view.hideProgress();
//                view.finishActivity();
//
//            }
//
//            @Override
//            public void onFail(String msg) {
//                view.hideProgress();
//                view.showMsg(msg);
//            }
//        }, billBody);
        //添加到数据库
        mModel.addAccountForDB(new MVPListener<Object>() {
            @Override
            public void onSuccess(Object content) {

                if (LoginStatusUtil.isLoginin()) {
                    //同步一下
                    syncForAdd();
                } else {
                    view.hideProgress();
                    view.finishActivity();
                }
            }

            @Override
            public void onFail(String msg) {
                view.hideProgress();
                view.showMsg(msg);
            }
        }, billBody, name);
    }

    private void syncForUpdata(final BillBody billBody) {
        if (LoginStatusUtil.isLoginin()) {
            if (!NetWorkUtil.isNetworkAvailable(mContext.getApplicationContext())) {
                view.hideProgress();
                view.setResultData(billBody);
                return;
            }
            UpDBDataOpertor.getInstance().onUpData(new UpDBListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess() {
                    view.hideProgress();
                    view.setResultData(billBody);
                }

                @Override
                public void onFail() {
                    view.hideProgress();
                    view.setResultData(billBody);
                }

                @Override
                public void onexitLoginFail(ArrayList<CategoryDB> needUpdataJson) {

                }

                @Override
                public void onNoNeedUpdata() {
                    view.hideProgress();
                    view.setResultData(billBody);
                }
            });
//            final CategoryDaoOperator categoryDaoOperator = CategoryDaoOperator.newInstance();
//            final ArrayList<CategoryDB> needUpdataJson = categoryDaoOperator.getNeedUpdataJson();
//            if (needUpdataJson != null && needUpdataJson.size() != 0) {
//                String json = UpDataOperator.getInstance().getJson(needUpdataJson);
//                RetrofitClient.getApiService().upData(new UpDataBody(0, json)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
//                    @Override
//                    public void onError(ExceptionHandle.ResponeThrowable e) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(BaseJson<Object> objectBeanNewJson) {
//                        //更新数据库
//                        if (objectBeanNewJson != null && objectBeanNewJson.getCode() == 0) {
//                            for (CategoryDB categoryDB : needUpdataJson) {
//                                if (categoryDB.getStatus() == 1) {
//                                    categoryDaoOperator.deleteData(categoryDB.getBill_id());
//                                } else {
//                                    categoryDaoOperator.updataNoNeedSynce(categoryDB.getBill_id());
//                                }
//                            }
//                        } else {
//                            for (CategoryDB categoryDB : needUpdataJson) {
//                                categoryDaoOperator.updataNeedSynce(categoryDB.getBill_id());
//                            }
//                        }
//
//
//                    }
//                });
//            } else {
//
//            }
        }
    }

    private void syncForAdd() {
        if (LoginStatusUtil.isLoginin()) {
            if (!NetWorkUtil.isNetworkAvailable(mContext.getApplicationContext())) {
                view.hideProgress();
                view.finishActivity();
                return;
            }
            UpDBDataOpertor.getInstance().onUpData(new UpDBListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess() {
                    view.hideProgress();
                    view.finishActivity();
                }

                @Override
                public void onFail() {
                    view.hideProgress();
                    view.finishActivity();
                }

                @Override
                public void onexitLoginFail(ArrayList<CategoryDB> needUpdataJson) {

                }

                @Override
                public void onNoNeedUpdata() {
                    view.hideProgress();
                    view.finishActivity();
                }
            });
//            final CategoryDaoOperator categoryDaoOperator = CategoryDaoOperator.newInstance();
//            final ArrayList<CategoryDB> needUpdataJson = categoryDaoOperator.getNeedUpdataJson();
//            if (needUpdataJson != null && needUpdataJson.size() != 0) {
//                String json = UpDataOperator.getInstance().getJson(needUpdataJson);
//                RetrofitClient.getApiService().upData(new UpDataBody(0, json)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
//                    @Override
//                    public void onError(ExceptionHandle.ResponeThrowable e) {
//                        view.hideProgress();
//                        view.finishActivity();
//                    }
//
//                    @Override
//                    public void onSuccess(BaseJson<Object> objectBeanNewJson) {
//                        //更新数据库
//                        if (objectBeanNewJson != null && objectBeanNewJson.getCode() == 0) {
//                            for (CategoryDB categoryDB : needUpdataJson) {
//                                if (categoryDB.getStatus() == 1) {
//                                    categoryDaoOperator.deleteData(categoryDB.getBill_id());
//                                } else {
//                                    categoryDaoOperator.updataNoNeedSynce(categoryDB.getBill_id());
//                                }
//                            }
//                        } else {
//                            for (CategoryDB categoryDB : needUpdataJson) {
//                                categoryDaoOperator.updataNeedSynce(categoryDB.getBill_id());
//                            }
//                        }
//                        view.hideProgress();
//                        view.finishActivity();
//                    }
//                });
//            } else {
//                view.hideProgress();
//                view.finishActivity();
//            }
        }
    }

    @Override
    void uploadBill(final BillBody billBody, int online_OR_Offline, String name, String bill_id) {
// TODO: 2018/4/3 需要做没有网络和有网络的处理

//        view.showProgress();
//        mModel.uploadBill(new MVPListener<Object>() {
//            @Override
//            public void onSuccess(Object content) {
//                view.hideProgress();
//                view.setResultData(billBody);
//            }
//
//            @Override
//            public void onFail(String msg) {
//                view.hideProgress();
//                view.showMsg(msg);
//            }
//        }, billBody);

        //数据库更新
        mModel.uploadBillForDB(new MVPListener<Object>() {
            @Override
            public void onSuccess(Object content) {
                if (LoginStatusUtil.isLoginin()) {
                    syncForUpdata(billBody);
                } else {
                    view.hideProgress();
                    view.setResultData(billBody);
                }
            }

            @Override
            public void onFail(String msg) {
                view.hideProgress();
                view.showMsg(msg);
            }
        }, billBody, name, bill_id);
    }

    public void setCategory(String category_id) {
        CategoryList account = null;
        for (int i = 0; i < mCategoryList.size(); i++) {
            account = mCategoryList.get(i);
            if (account.getId().equals(category_id)) {
                account.setClick(true);
            } else {
                account.setClick(false);
            }
        }
        mAdapter.notifyDataSetChanged();
        mClickListener.click(account.getId(), account.getType(), account.getIcon(), account);
    }

    interface ClickListener {
        void click(String category_id, int type, String icon_url, CategoryList categoryList);
    }

}
