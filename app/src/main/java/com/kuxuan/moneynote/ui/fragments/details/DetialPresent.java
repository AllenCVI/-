package com.kuxuan.moneynote.ui.fragments.details;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.BillData;
import com.kuxuan.moneynote.json.DetialJson;
import com.kuxuan.moneynote.json.TimeJson;
import com.kuxuan.moneynote.json.TypeDataJson;
import com.kuxuan.moneynote.json.UserAllBillJson;
import com.kuxuan.moneynote.json.YearData;
import com.kuxuan.moneynote.json.netbody.RES;
import com.kuxuan.moneynote.json.netbody.SkinBean;
import com.kuxuan.moneynote.listener.MVPListener;
import com.kuxuan.moneynote.ui.adapter.DetialAdapter;
import com.kuxuan.moneynote.ui.weight.KuxuanLoadMoreView;
import com.kuxuan.moneynote.ui.weight.LoadBottomView;
import com.kuxuan.moneynote.ui.weight.LoaddingHeadView;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.PickerUtil;
import com.kuxuan.moneynote.utils.TimeUtlis;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.addapp.pickers.picker.DatePicker;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public class DetialPresent extends DetialContract.DetialPresent implements View.OnClickListener {

    DetialAdapter adapter;

    //选择的当前年，当前月
    private int currentYear, currentMonth;


    /**
     * 开始时间
     */
    private YearData startData;
    /**
     * 结束时间
     */
    private YearData endData;
    private Context mContext;

    private HashMap<String, Integer> db_maps;

    @Override
    void initRecyclerView(Context context, RecyclerView recyclerView) {
        mContext = context;
        ArrayList<DetialJson> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            DetialJson detialJson = new DetialJson("名字" + i);
            if (i % 2 == 0) {
                detialJson.setType(i);
            }
            data.add(detialJson);

        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new DetialAdapter(R.layout.item_detial_layout);
        adapter.bindToRecyclerView(recyclerView);
//        adapter.setEmptyView(R.layout.empty_view);
        recyclerView.setAdapter(adapter);
        initAdapter();
    }

    @Override
    void initRefreshLayout(Context context, TwinklingRefreshLayout refreshLayout) {
        refreshLayout.setHeaderView(new LoaddingHeadView(context));
        refreshLayout.setBottomView(new LoadBottomView(context));
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (isdownMonth()) {
                    getDataLists(currentYear + "", currentMonth + "");

                } else {
                    refreshLayout.finishRefreshing();
                }

            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (isupMonth()) {
                    getDataLists(currentYear + "", currentMonth + "");
                } else {
                    refreshLayout.finishLoadmore();
                }

            }
        });
    }


    /**
     * 上拉查看上月数据
     */
    private boolean isupMonth() {
        boolean isCanRefresh = false;
        if (startData == null) {
            return isCanRefresh;
        }
        if (currentMonth == 1) {
            currentMonth = 12;
            currentYear--;
        } else {
            currentMonth--;
        }
        if (currentYear == startData.getYear()) {
            if (currentMonth >= startData.getMonth()) {
                isCanRefresh = true;
            }
        } else if (currentYear > startData.getYear()) {
            isCanRefresh = true;
        } else {
            isCanRefresh = false;
        }
        if (!isCanRefresh) {
            //不能刷新又变回原来的
            if (currentMonth == 12) {
                currentMonth = 1;
                currentYear++;
            } else {
                currentMonth++;
            }
        }
        return isCanRefresh;
    }

    /**
     * 下拉查看下月数据
     */
    private boolean isdownMonth() {
        boolean isCanRefresh = false;
        if (endData == null) {
            return isCanRefresh;
        }
        if (currentMonth == 12) {
            currentMonth = 1;
            currentYear++;
        } else {
            currentMonth++;
        }
        if (currentYear == endData.getYear()) {
            if (currentMonth <= endData.getMonth()) {
                isCanRefresh = true;
            }
        } else if (currentYear < endData.getYear()) {
            isCanRefresh = true;
        } else {
            isCanRefresh = false;
        }
        if (!isCanRefresh) {
            //不能刷新又变回原来的
            if (currentMonth == 1) {
                currentMonth = 12;
                currentYear--;
            } else {
                currentMonth--;
            }
        }
        return isCanRefresh;
    }

    private void initAdapter() {
        adapter.setLoadMoreView(new KuxuanLoadMoreView());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View v, int position) {
                TypeDataJson da = (TypeDataJson) adapter.getItem(position);
                view.goToEdit(da);
            }
        });
        adapter.setUpFetchEnable(false);
        adapter.setUpFetchListener(new BaseQuickAdapter.UpFetchListener() {
            @Override
            public void onUpFetch() {

            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

            }
        });

    }

    @Override
    public void initListener(View textView) {
        textView.setOnClickListener(this);
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }

    @Override
    void getDataLists(final String year, final String month) {
        Observable.create(new ObservableOnSubscribe<HashMap<String, Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<HashMap<String, Integer>> e) throws Exception {
                db_maps = CategoryDaoOperator.newInstance().getMaxAndMinTimeForUserId(-1);
                e.onNext(db_maps);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HashMap<String, Integer>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HashMap<String, Integer> stringIntegerHashMap) {
                currentYear = Integer.parseInt(year);
                currentMonth = Integer.parseInt(month);
                getDataForDB(year, month, stringIntegerHashMap);
            }

            @Override
            public void onError(Throwable e) {
                currentYear = Integer.parseInt(year);
                currentMonth = Integer.parseInt(month);
                getDataForDB(year, month, db_maps);
            }

            @Override
            public void onComplete() {

            }
        });


//        mModel.getDataLists(year, month, new MVPListener<UserAllBillJson>() {
//            @Override
//            public void onSuccess(UserAllBillJson content) {
//                view.finishRefresh();
//                view.showContentLayout();
//                changeData(content);
//            }
//
//            @Override
//            public void onFail(String msg) {
//                view.finishRefresh();
//                if (msg.equals("网络错误")) {
//                    view.showError();
//                } else if (msg.equals("网络连接失败，请检测网络")) {
//                    view.showNoNetWork();
//                } else {
//                    view.showError();
//                }
//            }
//        });
    }

    private void getDataForDB(String year, String month, HashMap<String, Integer> maps) {
        mModel.getDataListsForDB(year, month, maps, new MVPListener<UserAllBillJson>() {
            @Override
            public void onSuccess(UserAllBillJson content) {
                view.finishRefresh();
                view.showContentLayout();
                changeData(content);
            }

            @Override
            public void onFail(String msg) {
                view.finishRefresh();
                view.showContentLayout();
                if (msg.equals("网络错误")) {
                    view.showError();
                } else if (msg.equals("网络连接失败，请检测网络")) {
                    view.showNoNetWork();
                } else {
                    view.showError();
                }
            }
        });
    }

    private boolean isLogin;

    @Override
    void loginCheck() {
        isLogin = LoginStatusUtil.isLoginin();
        if (isLogin) {
            //获取数据
            view.showLogin();
        } else {
            view.showNoLogin();
        }
        view.getDataFirst();
    }

    @Override
    void InitPopWindow() {

        mModel.getPopWindowData(new MVPListener<SkinBean>() {
            @Override
            public void onSuccess(SkinBean content) {

                Gson gson = new Gson();

                String json = gson.toJson(content.getRes());

                view.showSkinData(content);
            }

            @Override
            public void onFail(String msg) {


            }
        });


    }





    @Override
    void downLoadPic(final RES res, final View background, final ProgressBar progressBar, String url, final int size, final int position) {


        mModel.downLoadPic(url, size, position, new DetialContract.DownloadListener() {
            @Override
            public void onStart() {


            }

            @Override
            public void onProgress(int currentLength) {

                if(size==2){
                    view.showDownLoad(res,size,position,background,progressBar,currentLength);
                }

            }

            @Override
            public void onFinish(String localPath) {
                if(size!=2) {
                    view.showDownLoad(res, size, position, background, progressBar,0);
                }
            }

            @Override
            public void onFailure() {

                Log.e("allence","失败了");

            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_detial_time_layout:
            case R.id.fragment_detial_year_text:
            case R.id.fragment_detial_month:
                //日期选择器
//                if (isLogin)
                showYearAndMonth();
                break;
        }
    }

    /**
     * 修改数据
     *
     * @param userAllBillJsonBaseJson
     */
    private void changeData(UserAllBillJson userAllBillJsonBaseJson) {
        view.showContentLayout();
        startData = userAllBillJsonBaseJson.getStart_data();
        if (startData == null || startData.getYear() == 0) {
            TimeJson currentTime = TimeUtlis.getCurrentTime();
            startData = new YearData(currentTime.getYear(), currentTime.getMonth());
        }
        endData = userAllBillJsonBaseJson.getEnd_data();
        if (endData == null || endData.getYear() == 0) {
            TimeJson currentTime = TimeUtlis.getCurrentTime();
            endData = new YearData(currentTime.getYear(), currentTime.getMonth());
        }
        ArrayList<TypeDataJson> dataLists = new ArrayList<TypeDataJson>();
        if (userAllBillJsonBaseJson != null) {
            ArrayList<BillData> bill_data = userAllBillJsonBaseJson.getBill_data();
            if (bill_data != null) {
                for (int i = 0; i < bill_data.size(); i++) {
                    BillData billData = bill_data.get(i);
                    List<TypeDataJson> day_data = billData.getDay_data();
                    if (day_data != null) {
                        for (int j = day_data.size() - 1; j >= 0; j--) {
                            if (j == day_data.size() - 1) {
                                day_data.get(j).setFirst(true);
                            }
                            day_data.get(j).setDay_type(billData.getTime());
                            day_data.get(j).setTag(billData);
                            day_data.get(j).setTrueData(true);
                            day_data.get(j).setCurrentYear(currentYear);
                            day_data.get(j).setCurrentMonth(currentMonth);
                            dataLists.add(day_data.get(j));
                        }
                    }
                }
            }

            if (dataLists.size() == 0) {
                view.showNoData();
                //为了触发上拉框
                dataLists.add(new TypeDataJson());
                dataLists.add(new TypeDataJson());
                dataLists.add(new TypeDataJson());
                dataLists.add(new TypeDataJson());
                dataLists.add(new TypeDataJson());
                dataLists.add(new TypeDataJson());
//                view.setScrollForNodata();
            } else {
                view.showHaveData();
            }
            adapter.setNewData(dataLists);
            view.setData(currentYear + "", currentMonth + "", userAllBillJsonBaseJson.getIncome_account(), userAllBillJsonBaseJson.getPay_account());
        }
    }


    DatePicker onYearMonthPicker;

    /**
     * 展示日历选择器（根据返回的数据选择年月范围）
     */
    private void showYearAndMonth() {
        TimeJson currentTime = TimeUtlis.getCurrentTime();
        int startYear = 2010;
        int startMonth = 1;
        int endYear = currentTime.getYear();
        int endMonth = currentTime.getMonth();
//        if (endData != null) {
//            endYear = endData.getYear();
//            endMonth = endData.getMonth();
//        }
        if (onYearMonthPicker == null) {
            onYearMonthPicker = PickerUtil.getOnYearMonthPicker((Activity) mContext, startYear, startMonth, endYear, endMonth, currentYear, currentMonth, new DatePicker.OnYearMonthPickListener() {
                @Override
                public void onDatePicked(String year, String month) {
                    currentYear = Integer.parseInt(year);
                    currentMonth = Integer.parseInt(month);
                    view.showYearAndMonth(year, month);
                    getDataLists(currentYear + "", currentMonth + "");
                }
            });
        } else {
            onYearMonthPicker.show();
        }

    }


}
