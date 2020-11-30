package com.kuxuan.moneynote.ui.fragments.reportsingle;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.CategoryDataJson;
import com.kuxuan.moneynote.json.ChartData;
import com.kuxuan.moneynote.json.DayJson;
import com.kuxuan.moneynote.json.LineJson;
import com.kuxuan.moneynote.json.NewCategoryJson;
import com.kuxuan.moneynote.json.NewChartData;
import com.kuxuan.moneynote.json.PopCharData;
import com.kuxuan.moneynote.json.ReportJson;
import com.kuxuan.moneynote.json.TimeDataJson;
import com.kuxuan.moneynote.json.TimeJson;
import com.kuxuan.moneynote.json.TypeDataJson;
import com.kuxuan.moneynote.listener.MVPListener;
import com.kuxuan.moneynote.ui.adapter.ReportAdapter;
import com.kuxuan.moneynote.ui.weight.ChartLayout;
import com.kuxuan.moneynote.ui.weight.TabLayoutOpertor;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.TabLayoutUtil;
import com.kuxuan.moneynote.utils.TimeUtlis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.kuxuan.moneynote.utils.CalanderUtil.getMonthDay;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public class ReportSinglePresent extends ReportSingleContract.RepPresent {
    /**
     * 1代表周，2代表月，3代表年
     */
    private int type = 1;


    private CategoryDaoOperator categoryDaoOperator;


    /**
     * 收入支出
     * 2代表支出，1代表收入
     */
    private int moneyType = 2;

    /**
     * 图表还是圆环
     */
    private int charType = ChartLayout.LINE;


    private String title_text;

    /**
     * tab当前的pos
     */
    private int tabCurrentPosition = 0;

    /**
     * 类别详情
     */
    private int catrogy_id = -1;

    public int getType() {
        return type;
    }


    public int getCharType() {
        return charType;
    }

    public void setCharType(int charType) {
        this.charType = charType;
    }

    private FragmentActivity mActivity;


    private WeakHashMap<Integer, ArrayList<ChartData>> mapLists;

    /**
     * 离线存储用到的map
     */
    private HashMap<String, Integer> db_maps;


    public void init(Context context, int charType, int catrogy_id, int money_type, int radio_type) {
        mActivity = (FragmentActivity) context;
        this.charType = charType;
        this.catrogy_id = catrogy_id;
        this.moneyType = money_type;
        this.type = radio_type;
    }

    public String getTitle_text() {
        return title_text;
    }

    public void setTitle_text(String title_text) {
        this.title_text = title_text;
    }

    @Override
    void initRadioGroup(RadioGroup radioGroup) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                if (!isLogin) {
//                    return;
//                }
                switch (checkedId) {
                    case R.id.activity_baobiao_rabtn_week:
                        type = 1;
//                        view.showTypeText(checkedId, TimeUtlis.getCreateTime(System.currentTimeMillis()));
                        break;
                    case R.id.activity_baobiao_rabtn_month:
//选择月
                        type = 2;
//                        view.showTypeText(checkedId, "一月");
                        break;
                    case R.id.activity_baobiao_rabtn_year:
//选择周
                        type = 3;
//                        view.showWeekLayout();
                        break;
                }
                view.getRadioData(type);
            }
        });
//        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(type - 1);
//        radioButton.setChecked(true);
        if (type == 1) {
            radioGroup.check(R.id.activity_baobiao_rabtn_week);
        } else if (type == 2) {
            radioGroup.check(R.id.activity_baobiao_rabtn_month);
        } else
            radioGroup.check(R.id.activity_baobiao_rabtn_year);
    }

    private TabLayoutOpertor opertor;

    @Override
    void initTabLayout(Context context, final TabLayout tabLayout) {
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabCurrentPosition = tab.getPosition();
                title_text = tab.getText().toString();
                DayJson tag = (DayJson) tab.getTag();
                NewChartData newChartData = tag.getNewChartData();
                //新版数据
                getNewCategoryData(newChartData);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 点击tab的时候获取新的数据
     */
    private void getNewCategoryData(final NewChartData newChartData) {
        Log.e("tabSelect——currentPos", newChartData.getKey());
        int statistic_type = newChartData.getStatistic_type();
        String year = null;
        String month = null;
        String start_date = null;
        String end_date = null;
        if (statistic_type == 2 || statistic_type == 3) {
            //当 statistic_type 为 2，3 必传 例如 2018
            year = newChartData.getYear();
            if (statistic_type == 2) {
                //当 statistic_type 为 2 必传 例如 8 或 10
                month = newChartData.getMonth();
            }
        } else if (statistic_type == 1) {
            //当 statistic_type 为1 时 必传 例 2018-01-01
            start_date = newChartData.getTime_range().get(0);
            end_date = newChartData.getTime_range().get(newChartData.getTime_range().size() - 1);
        }
        // TODO: 2018/4/3 需要做没有网络和有网络的处理
        getDataCatgoryForDB(statistic_type, moneyType, newChartData);
//        mModel.getDataCategotyLists(moneyType, statistic_type, catrogy_id, year, month, start_date, end_date, new MVPListener<NewCategoryJson>() {
//            @Override
//            public void onSuccess(NewCategoryJson content) {
//                // TODO: 2018/3/6 获取数据
//                view.showData();
//                view.setChartNewData(newChartData, content);
//            }
//
//            @Override
//            public void onFail(String msg) {
//// TODO: 2018/3/6 获取数据失败
//                view.showNoData();
//
//            }
//        });

    }

    /**
     * 从数据库中获取数据
     *
     * @param statistic_type
     * @param newChartData
     */
    private void getDataCatgoryForDB(int statistic_type, int type, final NewChartData newChartData) {
        mModel.getDataCatgoryForDB(statistic_type, type, catrogy_id, categoryDaoOperator, newChartData, new MVPListener<NewCategoryJson>() {
            @Override
            public void onSuccess(NewCategoryJson content) {
                if (content != null) {
                    view.showData();
                    view.setChartNewData(newChartData, content);
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });

    }

    private void initChartAdapter(ArrayList<ChartData> chartDatas) {
        view.refreshData(chartDatas);
    }

    private void initNewChartAdapter(ArrayList<NewChartData> chartDatas) {
        view.refreshNewData(chartDatas);
    }

    @Override
    void setTabLayoutData(ArrayList<String> data) {
        if (opertor != null) {
            opertor.setDataLists(data);
        }
    }

    @Override
    void changeChart(int type) {
    }

    @Override
    void changeData(int type, final int statis_type, int catrogy_id) {
        view.showLoadding();
        this.moneyType = type;
        // TODO: 2018/4/3 需要做没有网络和有网络的处理
        getDataForDB(statis_type, type);
        //获取报表数据
//        mModel.getNewChartDataLists(type, statis_type, catrogy_id, new MVPListener<ArrayList<NewChartData>>() {
//            @Override
//            public void onSuccess(ArrayList<NewChartData> content) {
//                view.hideProgress();
//                view.showContentLayout();
//                if (content != null) {
//                    for (NewChartData chartData : content) {
//                        chartData.setStatistic_type(statis_type);
//                    }
//                }
//                initNewChartAdapter(content);
//            }
//
//            @Override
//            public void onFail(String msg) {
//                view.hideProgress();
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

    /**
     * 从本地数据库中获取数据
     *
     * @param statis_type
     */
    private void getDataForDB(final int statis_type, final int type) {
        if (categoryDaoOperator == null) {
            categoryDaoOperator = CategoryDaoOperator.newInstance();
            categoryDaoOperator.setOnDBChangeListener(new CategoryDaoOperator.OnDBChangeListener() {
                @Override
                public void onStart() {
                    Log.e("db_startTime", System.currentTimeMillis() / 1000 + "");
                }

                @Override
                public void onSuccess(long code) {
                    Log.e("db_endTime", System.currentTimeMillis() / 1000 + "");
                }

                @Override
                public void fail() {

                }
            });
        }
//        if (db_maps == null) {
//        db_maps = categoryDaoOperator.getMaxAndMinTime(moneyType, catrogy_id);
////        }
//        if (db_maps == null) {
////            view.hideProgress();
//            view.showNoDBdata();
//            return;
//        }


        Observable<HashMap<String, Integer>> observable = io.reactivex.Observable.create(new ObservableOnSubscribe<HashMap<String, Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<HashMap<String, Integer>> e) throws Exception {
                db_maps = categoryDaoOperator.getMaxAndMinTime(moneyType, catrogy_id);
                if (db_maps != null) {
                    e.onNext(db_maps);
                    e.onComplete();
                } else {
                    e.onError(new Throwable());
                }
            }
        });
        observable.observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HashMap<String, Integer>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HashMap<String, Integer> stringIntegerHashMap) {
                getRealDataForDB(statis_type, type);
            }

            @Override
            public void onError(Throwable e) {
                view.showContentLayout();
                view.showNoDBdata();
            }

            @Override
            public void onComplete() {

            }
        });


    }


    /**
     * 获取数据
     *
     * @param statis_type
     * @param type
     */
    private void getRealDataForDB(int statis_type, int type) {
        mModel.getDataForDB(db_maps, statis_type, type, new MVPListener<ArrayList<NewChartData>>() {
            @Override
            public void onSuccess(ArrayList<NewChartData> content) {
                view.hideProgress();
                view.showContentLayout();
                if (content != null) {
                    initNewChartAdapter(content);
                } else {
                    view.showNoData();
                }
            }

            @Override
            public void onFail(String msg) {
                view.showNoData();
                view.showContentLayout();
            }
        });
    }

    /**
     * 判断是否需要刷新网络
     *
     * @param type
     * @param statis_type
     * @return
     */
    @Override
    boolean jugeNeedGetData(int type, int statis_type) {
        boolean isNeedRefresh = false;
        if (mapLists == null) {
            mapLists = new WeakHashMap<>();
        }
        try {
            ArrayList<ChartData> chartDatas = mapLists.get(type);
            if (chartDatas != null) {
                //刷新adapter和tablayout
                isNeedRefresh = false;
                initChartAdapter(chartDatas);
            } else {
                isNeedRefresh = true;
            }
        } catch (Exception e) {
            //请求网络刷新layout
            isNeedRefresh = true;
        }
        return isNeedRefresh;
    }

    @Override
    void refreshTabLayout(TabLayout tabLayout, int type, String title, ArrayList<ChartData> datas) {
        //获取得到的
        if (datas.size() == 0) {
            view.showNoData();
            return;
        } else {
            view.showContentLayout();
        }
        //防止刷新后会为0的情况
//        int currentPos = tabCurrentPosition;
        tabLayout.removeAllTabs();
        int currentPos = -1;
        int count = 0;
        for (int i = datas.size() - 1; i >= 0; i--) {
            DayJson dayJson = new DayJson();
            if (title.equals(datas.get(i).getTime())) {
                currentPos = count;
            }

            dayJson.setData(datas.get(i));
            datas.get(i).setTrueData(true);
            TabLayout.Tab tab = tabLayout.newTab().setText(datas.get(i).getTime());
            tab.setTag(dayJson);
            tabLayout.addTab(tab);
            count++;
        }
//        if (datas.size() != 0) {
//            if (currentPos < datas.size())
//                tabLayout.getTabAt(currentPos).select();
//            else {
        if (currentPos != -1)
            tabLayout.getTabAt(currentPos).select();
        else
            tabLayout.getTabAt(datas.size() - 1).select();
    }

    private int currentPos = -1;

    @Override
    void refreshNewTabLayout(Context context, final TabLayout tabLayout, int type, String title, final ArrayList<NewChartData> datas) {
        //获取得到的
        if (datas.size() == 0) {
            view.showNoData();
            return;
        } else {
            view.showContentLayout();
        }
        //防止刷新后会为0的情况
//        int currentPos = tabCurrentPosition;
        currentPos = -1;
        tabLayout.removeAllTabs();
        int count = 0;
        for (int i = 0; i < datas.size(); i++) {
            boolean isSelect = false;
            DayJson dayJson = new DayJson();
            if (title.equals(datas.get(i).getKey())) {
                currentPos = count;
                isSelect = true;
            }
            dayJson.setNewChartData(datas.get(i));
//            datas.get(i).setTrueData(true);
            dayJson.setChartData(datas);
            TabLayout.Tab tab = tabLayout.newTab().setText(datas.get(i).getKey());
            tab.setTag(dayJson);
            if (i == 0 && datas.size() != 1) {
                //为了默认不去加载第一页，会出现数据错乱3
                tabLayout.addTab(tab, false);
            } else {
                tabLayout.addTab(tab);

            }
            count++;
        }
        if (currentPos != -1) {
            tabLayout.getTabAt(currentPos).select();
        } else {
            tabLayout.getTabAt(datas.size() - 1).select();
        }
        TabLayoutUtil.recomputeTlOffset1(tabLayout, tabLayout.getSelectedTabPosition(), datas, context);
//        tabLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                if (currentPos != -1) {
//                    tabLayout.getTabAt(currentPos).select();
//                    TabLayoutUtil.recomputeTlOffset1(tabLayout, currentPos, datas);
//                } else {
//                    tabLayout.getTabAt(datas.size() - 1).select();
//                    TabLayoutUtil.recomputeTlOffset1(tabLayout, datas.size() - 1, datas);
//                }
//            }
//        });
    }

    @Override
    void clearMap() {
        if (mapLists != null)
            mapLists.clear();
    }

    ReportAdapter adapter;

    @Override
    void setChartData(ChartLayout chartLayout, int type, ArrayList<LineJson> datas) {
        chartLayout.setLineData(datas);
    }

    @Override
    void initRecyclerView(Context context, RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (catrogy_id == -1)
            adapter = new ReportAdapter(R.layout.item_report_layout, false);
        else
            adapter = new ReportAdapter(R.layout.item_report_layout, true);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.empty_view_nodata);
        recyclerView.setAdapter(adapter);
//        testAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View v, int position) {
                ReportJson reportJson = (ReportJson) adapter.getData().get(position);
                if (catrogy_id == -1) {
                    //外层，跳转详情
                    TypeDataJson json = (TypeDataJson) reportJson.getTag();
                    view.goToCatrogyDetial(json.getCategory_id());
                } else {
                    //跳转编辑页
                    TypeDataJson typeDataJson = (TypeDataJson) reportJson.getTag();
                    view.goToEdit(typeDataJson);
                }
            }
        });
    }

    @Override
    void setLineData(ChartData chartData) {
        jugeDataType(chartData);
    }

    @Override
    void setLineData(NewChartData chartData, NewCategoryJson newCategoryJson) {
        jugeDataType(chartData, newCategoryJson);
    }

    /**
     * 圆环数据分析
     *
     * @param chartData
     */
    @Override
    void setCircleData(ChartData chartData) {
        jugeDataCircleType(chartData);
    }

    @Override
    void setCircleData(NewChartData chartData, NewCategoryJson newCategoryJson) {
        jugeDataCircleType(chartData, newCategoryJson);
    }

    private boolean isLogin;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    @Override
    void loginCheck() {
        isLogin = LoginStatusUtil.isLoginin();
//        if (isLogin) {
//            //获取数据
//            view.getDataFirst();
//        } else {
//            view.showNoLogin();
//        }
        view.getDataFirst();
    }


    /**
     * 数据分类
     *
     * @param chartData
     */
    private void jugeDataType(NewChartData chartData, NewCategoryJson newCategoryJson) {
        if (newCategoryJson == null) {
            view.showNoData();
            return;
        }

        switch (chartData.getStatistic_type()) {
            case ChartData.WEEK:
                //周
                setWeekData(chartData, newCategoryJson);
                break;
            case ChartData.YEAR:
                //年
                setYearData(chartData, newCategoryJson);
                break;
            case ChartData.MONTH:
                //月
                setMonth(chartData, newCategoryJson);
                break;
        }
        /**
         * 设置下面的数据
         */
        getAdapterLists(newCategoryJson);
    }

    /**
     * 数据分类
     *
     * @param chartData
     */
    private void jugeDataType(ChartData chartData) {
        switch (chartData.getStatistic_type()) {
            case ChartData.WEEK:
                //周
                setWeekData(chartData);
                break;
            case ChartData.YEAR:
                //年
                setYearData(chartData);
                break;
            case ChartData.MONTH:
                //月
                setMonth(chartData);
                break;
        }
        /**
         * 设置下面的数据
         */
        getAdapterLists(chartData);
    }

    /**
     * 圆环数据分类
     *
     * @param chartData
     */
    private void jugeDataCircleType(ChartData chartData) {
        if (chartData.getCategory_data() != null) {
            for (CategoryDataJson categoryDataJson : chartData.getCategory_data()) {
                categoryDataJson.setAllAccount(chartData.getAccount());
            }
        }
        view.setCircleData((ArrayList<CategoryDataJson>) chartData.getCategory_data());
        getAdapterLists(chartData);
    }

    /**
     * 圆环数据分类
     *
     * @param chartData
     */
    private void jugeDataCircleType(NewChartData chartData, NewCategoryJson newCategoryJson) {
        if (newCategoryJson == null) {
            view.showNoData();
            return;
        }
        ArrayList<CategoryDataJson> list = new ArrayList<>();
        if (catrogy_id != -1) {
            if (newCategoryJson.getDetial_data() != null) {
                for (TypeDataJson typeDataJson : newCategoryJson.getDetial_data()) {
                    CategoryDataJson categoryDataJson = new CategoryDataJson();
                    categoryDataJson.setName(typeDataJson.getName());
                    categoryDataJson.setAllAccount(Double.parseDouble(newCategoryJson.getAccount()));
                    categoryDataJson.setCategory_account(Double.parseDouble(typeDataJson.getAccount()));
                    list.add(categoryDataJson);
                }
            }
        } else {
            if (newCategoryJson.getCategory() != null) {
                for (TypeDataJson typeDataJson : newCategoryJson.getCategory()) {
                    CategoryDataJson categoryDataJson = new CategoryDataJson();
                    categoryDataJson.setName(typeDataJson.getName());
                    categoryDataJson.setAllAccount(Double.parseDouble(newCategoryJson.getAccount()));
                    categoryDataJson.setCategory_account(Double.parseDouble(typeDataJson.getAccount()));
                    list.add(categoryDataJson);
                }
            }

        }
        view.setCircleData(list);
        getAdapterLists(newCategoryJson);
    }


    /**
     * 设置周的图表
     *
     * @param chartData
     */

    private void setWeekData(ChartData chartData) {
        String time = chartData.getTime();
        List<String> days = chartData.getDays();
        String mMdd = TimeUtlis.getMMdd();
        String[] datas = new String[days.size()];
        ArrayList<LineJson> lineLists = new ArrayList<>();
        //设置x的值
        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).equals(mMdd)) {
                datas[i] = "今天";
            } else
                datas[i] = days.get(i).substring(5, days.get(i).length());
            lineLists.add(new LineJson(datas[i], getYvalue(days.get(i), chartData.getTime_data())).setPopData(getChartPopData(days.get(i), chartData.getTime_data())));
        }
        view.setLineData(lineLists);
    }

    /**
     * 新版的
     *
     * @param chartData
     * @param categoryJson
     */
    private void setWeekData(NewChartData chartData, NewCategoryJson categoryJson) {
        List<String> days = chartData.getTime_range();
        String mMdd = TimeUtlis.getMMdd();
        String[] datas = new String[days.size()];
        ArrayList<LineJson> lineLists = new ArrayList<>();
        //设置x的值
        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).equals(mMdd)) {
                datas[i] = "今天";
            } else
                datas[i] = days.get(i).substring(5, days.get(i).length());
            lineLists.add(new LineJson(datas[i], getYvalue(days.get(i).substring(5, days.get(i).length()), categoryJson.getTime_data())).setPopData(getChartPopData(days.get(i).substring(5, days.get(i).length()), categoryJson.getTime_data())));
        }
        view.setLineData(lineLists);
    }

    /**
     * 获取当前时间的pop集合
     *
     * @param key
     * @param timeDataJsons
     * @return
     */
    private ArrayList<PopCharData> getChartPopData(String key, List<TimeDataJson> timeDataJsons) {
        if (timeDataJsons == null || timeDataJsons.size() == 0) {
            return null;
        }
        List<PopCharData> dataJsons = new ArrayList<>();
        for (int i = 0; i < timeDataJsons.size(); i++) {
            if (key.equals(timeDataJsons.get(i).getTime())) {
                dataJsons.addAll(timeDataJsons.get(i).getData());
                break;
            }

        }
        return (ArrayList<PopCharData>) dataJsons;
    }

    /**
     * 获取当前时间年的pop集合
     *
     * @param key
     * @param timeDataJsons
     * @return
     */
    private ArrayList<PopCharData> getChartPopDataForYear(String key, List<TimeDataJson> timeDataJsons) {
        if (timeDataJsons == null || timeDataJsons.size() == 0) {
            return null;
        }
        List<PopCharData> dataJsons = new ArrayList<>();
        for (int i = 0; i < timeDataJsons.size(); i++) {
            if (timeDataJsons.get(i).getTime().startsWith(key)) {
                dataJsons.addAll(timeDataJsons.get(i).getData());
                break;
            }

        }
        return (ArrayList<PopCharData>) dataJsons;
    }

    /**
     * 设置adapter
     *
     * @param chartData
     */
    private void getAdapterLists(ChartData chartData) {
        ArrayList<ReportJson> reportJsons = new ArrayList<>();
        List<CategoryDataJson> category_data = chartData.getCategory_data();
        if (category_data != null) {
            if (catrogy_id == -1) {
                for (int i = 0; i < category_data.size(); i++) {
                    ReportJson reportJson = new ReportJson();
                    if (moneyType == 2) {
                        reportJson.setOut(true);
                        reportJson.setOutmoney((long) category_data.get(i).getCategory_account());
                    } else {
                        reportJson.setOut(false);
                        reportJson.setInmoney((long) category_data.get(i).getCategory_account());
                    }
                    reportJson.setAllMoney((long) chartData.getAccount());
                    reportJson.setTag(category_data.get(i));
                    reportJsons.add(reportJson);
                }
            } else {
                //细分
                for (int i = 0; i < category_data.size(); i++) {
                    CategoryDataJson categoryDataJson = category_data.get(i);
                    List<TypeDataJson> data = categoryDataJson.getData();
                    if (data != null) {
                        for (int j = 0; j < data.size(); j++) {
                            ReportJson reportJson = new ReportJson();
                            if (moneyType == 2) {
                                reportJson.setOut(true);
                                reportJson.setOutmoney((long) Float.parseFloat(data.get(j).getAccount()));
                            } else {
                                reportJson.setOut(false);
                                reportJson.setInmoney((long) Float.parseFloat(data.get(j).getAccount()));
                            }
                            reportJson.setAllMoney((long) categoryDataJson.getCategory_account());
                            reportJson.setTag(data.get(j));
                            reportJsons.add(reportJson);
                        }
                    }
                }
            }
        }
        Log.e("List排序前的集合", reportJsons.toString());
        //排序
        Collections.sort(reportJsons);
        Log.e("List排序后的集合", reportJsons.toString());
        adapter.setNewData(reportJsons);
    }

    /**
     * 设置adapter
     *
     * @param chartData
     */
    private void getAdapterLists(NewCategoryJson chartData) {
        ArrayList<ReportJson> reportJsons = new ArrayList<>();
        if (chartData == null) {
            return;
        }
        if (catrogy_id == -1) {
            List<TypeDataJson> category_data = chartData.getCategory();
            if (category_data != null) {
                for (int i = 0; i < category_data.size(); i++) {
                    ReportJson reportJson = new ReportJson();
                    if (moneyType == 2) {
                        reportJson.setOut(true);
                        reportJson.setOutmoney(Double.parseDouble(category_data.get(i).getAccount()));
                    } else {
                        reportJson.setOut(false);
                        reportJson.setInmoney(Double.parseDouble(category_data.get(i).getAccount()));
                    }
                    reportJson.setAllMoney(Double.parseDouble(chartData.getAccount()));
                    reportJson.setTag(category_data.get(i));
                    reportJsons.add(reportJson);
                }
            }

        } else {
            List<TypeDataJson> category_data = chartData.getDetial_data();
            if (category_data != null) {
                for (int i = 0; i < category_data.size(); i++) {
                    ReportJson reportJson = new ReportJson();
                    if (moneyType == 2) {
                        reportJson.setOut(true);
                        reportJson.setOutmoney(Double.parseDouble(category_data.get(i).getAccount()));
                    } else {
                        reportJson.setOut(false);
                        reportJson.setInmoney(Double.parseDouble(category_data.get(i).getAccount()));
                    }
                    reportJson.setAllMoney(Double.parseDouble(chartData.getAccount()));
                    reportJson.setTag(category_data.get(i));
                    reportJsons.add(reportJson);
                }
            }
        }
//        Log.e("List排序前的集合", reportJsons.toString());
//        //排序
        Collections.sort(reportJsons);
//        Log.e("List排序后的集合", reportJsons.toString());
        adapter.setNewData(reportJsons);
    }


    /**
     * 设置月的时间
     *
     * @param chartData
     */
    private void setMonth(ChartData chartData) {
        //判断当月有多少天
        String time = chartData.getTime();
        TimeJson currentTime = TimeUtlis.getCurrentTime();
        int year = currentTime.getYear();
        int month = currentTime.getMonth();
        int dayCount = 0;
        if (time.equals("本月")) {
            dayCount = getMonthDay(year, month);
        } else if (time.equals("上月")) {
            if (month == 12) {
                month = 1;
                year--;
            } else {
                month--;
            }
            dayCount = getMonthDay(year, month);
        } else {
            String[] split = null;
            try {
                split = time.split("-");
                year = Integer.parseInt(split[0]);
                String m = split[1];
                if (m.startsWith("0")) {
                    month = Integer.parseInt(m.substring(1, m.length() - 1));
                } else {
                    month = Integer.parseInt(m.substring(0, m.length() - 1));
                }
                dayCount = getMonthDay(year, month);
            } catch (Exception e) {
                String substring = time.substring(0, time.length() - 1);
                month = Integer.parseInt(substring);
                dayCount = getMonthDay(year, month);
            }
        }
        String[] datas = new String[dayCount];
        ArrayList<LineJson> lineLists = new ArrayList<>();
        for (int i = 0; i < dayCount; i++) {
            datas[i] = (i + 1) + "";
            if (month < 10) {
                if (i + 1 < 10)
                    lineLists.add(new LineJson(datas[i], getYvalue(year + "-0" + month + "-0" + (i + 1), chartData.getTime_data())).setPopData(getChartPopData(year + "-0" + month + "-0" + (i + 1), chartData.getTime_data())));
                else
                    lineLists.add(new LineJson(datas[i], getYvalue(year + "-0" + month + "-" + (i + 1), chartData.getTime_data())).setPopData(getChartPopData(year + "-0" + month + "-" + (i + 1), chartData.getTime_data())));

            } else {
                if (i + 1 < 10)
                    lineLists.add(new LineJson(datas[i], getYvalue(year + "-" + month + "-0" + (i + 1), chartData.getTime_data())).setPopData(getChartPopData(year + "-" + month + "-0" + (i + 1), chartData.getTime_data())));
                else
                    lineLists.add(new LineJson(datas[i], getYvalue(year + "-" + month + "-" + (i + 1), chartData.getTime_data())).setPopData(getChartPopData(year + "-" + month + "-" + (i + 1), chartData.getTime_data())));

            }


        }
        view.setLineData(lineLists);
    }


    /**
     * 设置月的时间
     *
     * @param chartData
     */
    private void setMonth(NewChartData chartData, NewCategoryJson newCategoryJson) {
        //判断当月有多少天
        String time = chartData.getMonth();
        TimeJson currentTime = TimeUtlis.getCurrentTime();
        int year = currentTime.getYear();
        int month = currentTime.getMonth();
        int dayCount = 0;
        dayCount = getMonthDay(Integer.parseInt(chartData.getYear()), Integer.parseInt(chartData.getMonth()));
//        if (time.equals("本月")) {
//            dayCount = getMonthDay(year, month);
//        } else if (time.equals("上月")) {
//            if (month == 12) {
//                month = 1;
//                year--;
//            } else {
//                month--;
//            }
//            dayCount = getMonthDay(year, month);
//        } else {
//            String[] split = null;
//            try {
//                split = time.split("-");
//                year = Integer.parseInt(split[0]);
//                String m = split[1];
//                if (m.startsWith("0")) {
//                    month = Integer.parseInt(m.substring(1, m.length() - 1));
//                } else {
//                    month = Integer.parseInt(m.substring(0, m.length() - 1));
//                }
//                dayCount = getMonthDay(year, month);
//            } catch (Exception e) {
//                String substring = time.substring(0, time.length() - 1);
//                month = Integer.parseInt(substring);
//                dayCount = getMonthDay(year, month);
//            }
//        }

        String[] datas = new String[dayCount];
        ArrayList<LineJson> lineLists = new ArrayList<>();
        for (int i = 0; i < dayCount; i++) {
            datas[i] = (i + 1) + "";
            if (month < 10) {
                if (i + 1 < 10)
                    lineLists.add(new LineJson("0" + datas[i], getYvalue("0" + datas[i], newCategoryJson.getTime_data())).setPopData(getChartPopData("0" + datas[i], newCategoryJson.getTime_data())));
                else
                    lineLists.add(new LineJson(datas[i], getYvalue(datas[i], newCategoryJson.getTime_data())).setPopData(getChartPopData(datas[i], newCategoryJson.getTime_data())));

            } else {
                if (i + 1 < 10)
                    lineLists.add(new LineJson("0" + datas[i], getYvalue("0" + datas[i] + "", newCategoryJson.getTime_data())).setPopData(getChartPopData("0" + datas[i], newCategoryJson.getTime_data())));
                else
                    lineLists.add(new LineJson(datas[i], getYvalue(datas[i], newCategoryJson.getTime_data())).setPopData(getChartPopData(datas[i], newCategoryJson.getTime_data())));

            }


        }
        view.setLineData(lineLists);
    }

    /**
     * 设置年的值
     *
     * @param chartData
     */
    private void setYearData(ChartData chartData) {
        TimeJson currentTime = TimeUtlis.getCurrentTime();
        int year = currentTime.getYear();
        String time = chartData.getTime();
        if (time.equals("今年")) {

        } else if (time.equals("去年")) {
            year--;
        } else {
            year = Integer.parseInt(time.substring(0, time.length() - 1));
        }
        String[] datas = new String[12];
        ArrayList<LineJson> lineLists = new ArrayList<>();
        for (int i = 0; i < datas.length; i++) {
            int time1 = i + 1;
            datas[i] = time1 + "月";
            if (time1 < 10) {
                lineLists.add(new LineJson(datas[i], getYvalueForYear(year + "-0" + time1, chartData.getTime_data())).setPopData(getChartPopDataForYear(year + "-0" + time1, chartData.getTime_data())));
            } else {
                lineLists.add(new LineJson(datas[i], getYvalueForYear(year + "-" + time1, chartData.getTime_data())).setPopData(getChartPopDataForYear(year + "-" + time1, chartData.getTime_data())));
            }
        }
        view.setLineData(lineLists);
    }

    /**
     * 设置年的值
     *
     * @param chartData
     */
    private void setYearData(NewChartData chartData, NewCategoryJson newCategoryJson) {
//        TimeJson currentTime = TimeUtlis.getCurrentTime();
//        int year = currentTime.getYear();
//        String time = chartData.getTime();
//        if (time.equals("今年")) {
//
//        } else if (time.equals("去年")) {
//            year--;
//        } else {
//            year = Integer.parseInt(time.substring(0, time.length() - 1));
//        }

        String[] datas = new String[12];
        ArrayList<LineJson> lineLists = new ArrayList<>();
        for (int i = 0; i < datas.length; i++) {
            int time1 = i + 1;
            datas[i] = time1 + "月";
            if (time1 < 10) {
                lineLists.add(new LineJson(datas[i], getYvalueForYear("0" + time1, newCategoryJson.getTime_data())).setPopData(getChartPopDataForYear("0" + time1, newCategoryJson.getTime_data())));
            } else {
                lineLists.add(new LineJson(datas[i], getYvalueForYear(time1 + "", newCategoryJson.getTime_data())).setPopData(getChartPopDataForYear(time1 + "", newCategoryJson.getTime_data())));
            }
        }
        view.setLineData(lineLists);
    }


    /**
     * 获取Y的值
     *
     * @param key
     * @param list
     * @return
     */
    private float getYvalue(String key, List<TimeDataJson> list) {
        float y = 0;
        if (list == null) {
            return 0;
        }
        for (TimeDataJson dataJson : list) {
            if (key.equals(dataJson.getTime())) {
                y = (float) dataJson.getAccount();
                break;
            }
        }
        return y;
    }


    /**
     * 获取Y的值(年)
     *
     * @param key
     * @param list
     * @return
     */
    private float getYvalueForYear(String key, List<TimeDataJson> list) {
        double y = 0;
        if (list == null) {
            return 0;
        }
        for (TimeDataJson dataJson : list) {
            if (dataJson.getDay().startsWith(key)) {
                y += dataJson.getAccount();
            }
        }
        return (float) y;
    }
}
