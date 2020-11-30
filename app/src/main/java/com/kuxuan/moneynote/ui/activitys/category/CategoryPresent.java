package com.kuxuan.moneynote.ui.activitys.category;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.CategoryJson;
import com.kuxuan.moneynote.json.CategoryList;
import com.kuxuan.moneynote.listener.MVPListener;
import com.kuxuan.moneynote.ui.activitys.eventbus.RefreshEvent;
import com.kuxuan.moneynote.ui.adapter.CategoryAddAdapter;
import com.kuxuan.moneynote.ui.adapter.CategoryRemoveAdapter;
import com.kuxuan.moneynote.ui.weight.DividerItemDecoration;
import com.kuxuan.moneynote.ui.weight.MyLayoutManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class CategoryPresent extends CategoryContract.CategoryPresent {
    CategoryRemoveAdapter mAdapter;
    CategoryAddAdapter mAddAdapter;
    String inOrOutType;

    Context mContext;
    //用来判断我要删除的是否是自定义的
    private String customtype;
    List<CategoryList> mCategoryList;
    List<CategoryList> mRemoveList;
    List<CategoryList> mCustomList;
    List<CategoryList> mSystemList;
    private static final String TAG = "CategoryPresent";

    @Override
    void getCategoryList(String type) {
        this.inOrOutType = type;
        mModel.getCategoryList(new MVPListener<CategoryJson>() {
            @Override
            public void onSuccess(CategoryJson content) {
                view.hideProgress();
                mCategoryList = new ArrayList<CategoryList>();
                mSystemList = content.getSystem();
                mCustomList = content.getCustom();
                mRemoveList = content.getRemoved_system();
                mCategoryList.addAll(mSystemList);
                if (mCustomList == null) {
                    mCustomList = new ArrayList<>();
                }
                if (mRemoveList == null) {
                    mRemoveList = new ArrayList<>();
                }
                mCategoryList.addAll(mCustomList);
                mAdapter.setNewData(mCategoryList);
                mAddAdapter.setNewData(mRemoveList);
                mAdapter.notifyDataSetChanged();
                mAddAdapter.notifyDataSetChanged();
                if (mRemoveList.size() < 1) {
                    view.getNullOrNot(false);
                } else {
                    view.getNullOrNot(true);
                }

            }

            @Override
            public void onFail(String msg) {
view.hideProgress();
            }
        }, type);
    }

    @Override
    void initRecyclerRemove(Context context, RecyclerView recyclerView) {
        mContext = context;
        MyLayoutManager myLayoutManager = new MyLayoutManager(context);
        myLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(myLayoutManager);
        mAdapter = new CategoryRemoveAdapter(R.layout.item_category_list, mCategoryList);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < mCustomList.size(); i++) {
                    if (mCategoryList.get(position).getId() == mCustomList.get(i).getId()) {
                        checkDialog(mCategoryList.get(position).getId(), 2, position);
                        return;
                    }
                }
                checkDialog(mCategoryList.get(position).getId(), 1, position);

            }
        });
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

// 开启拖拽
        mAdapter.enableDragItem(itemTouchHelper, R.id.lin, true);


    }

    @Override
    void initRecyclerAdd(Context context, RecyclerView recyclerView) {
        mContext = context;
        MyLayoutManager myLayoutManager = new MyLayoutManager(context);
        myLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(myLayoutManager);
        mAddAdapter = new CategoryAddAdapter(R.layout.item_category_list_add, mRemoveList);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(mAddAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAddAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                addCategory(position);
            }
        });
    }

    /**
     * 删除类别
     *
     * @param position     位置
     * @param customType   自定义还是系统
     * @param needDeleteDb 是否需要删除数据库中的类别数据
     */
    @Override
    void deleteCategory(final int position, final int customType, final boolean needDeleteDb) {
        String name = null;
        String type = null;
        CategoryList categoryList = mCategoryList.get(position);
        if (customType == 2) {
            name = categoryList.getName();
            type = categoryList.getType() + "";
        }
        mModel.removeCategory(new MVPListener<Object>() {
            @Override
            public void onSuccess(Object content) {
                //判断是不是自定义类别，如果是就直接删除
                int id = Integer.parseInt(mCategoryList.get(position).getId());
                if (needDeleteDb) {
                    deleteDBData(id);
                }
                if (customType == 2) {
                    mCategoryList.remove(position);
                    mAdapter.notifyDataSetChanged();
                    return;
                }
                mRemoveList.add(0, mCategoryList.get(position));
                mCategoryList.remove(position);
                mAddAdapter.notifyItemInserted(0);

                mAdapter.notifyItemRemoved(position);
                if (mRemoveList.size() < 1) {
                    view.getNullOrNot(false);
                } else {
                    view.getNullOrNot(true);
                }

            }

            @Override
            public void onFail(String msg) {

            }
        }, mCategoryList.get(position).getId(), customType, name, type);

    }


    /**
     * 删除数据库中的数据
     * @param category_id
     */
    private void deleteDBData(final int category_id) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                CategoryDaoOperator.newInstance().deleteCategoryData(category_id);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                EventBus.getDefault().post(new RefreshEvent());
            }
        });
    }

    @Override
    void addCategory(final int position) {
//增加系统类别
        mModel.addCategory(new MVPListener<Object>() {
            @Override
            public void onSuccess(Object content) {
                mCategoryList.add(mRemoveList.get(position));
                mRemoveList.remove(position);
                mAdapter.notifyItemInserted(mCategoryList.size() - 1);
                mAddAdapter.notifyItemRemoved(position);
                if (mRemoveList.size() < 1) {
                    view.getNullOrNot(false);
                } else {
                    view.getNullOrNot(true);
                }

            }

            @Override
            public void onFail(String msg) {

            }
        }, mRemoveList.get(position).getId());

    }

    @Override
    void addCustomCategory(String name, String type) {
        //增加自定义类别
        mModel.addCustomCategory(new MVPListener<Object>() {
            @Override
            public void onSuccess(Object content) {
                getCategoryList(inOrOutType);
            }

            @Override
            public void onFail(String msg) {

            }
        }, name, type);
    }

    @Override
    void checkDialog(String categoryId, final int type, final int position) {
        mModel.checkBillCategory(new MVPListener<Boolean>() {

            @Override
            public void onSuccess(Boolean content) {
                if (content) {
                    view.checkDialog(true, position, type);
                } else {
                    view.checkDialog(false, position, type);
                }
            }

            @Override
            public void onFail(String msg) {
            }
        }, type, categoryId);
    }


}
