package com.kuxuan.moneynote.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseActivity;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.TypeDataJson;
import com.kuxuan.moneynote.ui.activitys.edit.EditBillActivity;
import com.kuxuan.moneynote.ui.activitys.eventbus.RefreshEvent;
import com.kuxuan.moneynote.ui.adapter.SearchAdapter;
import com.kuxuan.moneynote.utils.DrawableUtil;
import com.kuxuan.moneynote.utils.ToastUtil;
import com.kuxuan.sqlite.db.CategoryDB;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 搜索数据
 * Created by xieshengqi on 2018/4/25.
 */

public class SearchActivity extends BaseActivity {
    @Bind(R.id.activity_search_edittext)
    EditText activitySearchEdittext;
    @Bind(R.id.activity_search_clear_img)
    ImageView activitySearchClearImg;
    @Bind(R.id.activity_search_cancle_text)
    TextView activitySearchCancleText;
    @Bind(R.id.activity_search_recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.activity_search_title_layout)
    LinearLayout title_layout;

    @Override
    public int getLayout() {
        return R.layout.activity_search_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        EventBus.getDefault().register(this);
        title_layout.setBackgroundColor(DrawableUtil.getSkinColor(this));
        initEdit();
        initAdapter();
    }

    @OnClick({R.id.activity_search_clear_img, R.id.activity_search_cancle_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_search_clear_img:
                activitySearchEdittext.setText("");
                break;
            case R.id.activity_search_cancle_text:
                finish();
                break;
        }
    }

    private void initEdit() {
        activitySearchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (activitySearchEdittext.getText().toString().length() != 0) {
                    activitySearchClearImg.setVisibility(View.VISIBLE);
                } else {
                    activitySearchClearImg.setVisibility(View.INVISIBLE);
                }
            }
        });

        activitySearchEdittext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    // 先隐藏键盘event
                    Log.e("event", "search");
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    String string = activitySearchEdittext.getText().toString();
                    if (TextUtils.isEmpty(string)) {
                        ToastUtil.show(SearchActivity.this, "请输入内容");
                    } else {
                        searchData(string);
                    }
                }

                return false;
            }
        });


    }


    SearchAdapter adapter;
    private int index = 0;
    private int maxCount = 10;

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchAdapter(R.layout.item_search_layout, new ArrayList<TypeDataJson>());
        adapter.bindToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.setEmptyView(R.layout.empty_view_nodata);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (adapterLists.size() > adapter.getItemCount()) {
//还有下一页
                    adapter.addData(getLists());
                    adapter.loadMoreComplete();
                } else {
                    adapter.loadMoreEnd();
                }

            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SearchActivity.this, EditBillActivity.class);
                TypeDataJson item = (TypeDataJson) adapter.getItem(position);
                intent.putExtra(EditBillActivity.DATA, item);
                startActivity(intent);
            }
        });
    }


    private ArrayList<TypeDataJson> getLists() {
        ArrayList<TypeDataJson> dataJsons = new ArrayList<>();
        for (int i = 0; i < maxCount; i++) {
            if (index < adapterLists.size()) {
                dataJsons.add(adapterLists.get(index));
                index++;
            } else {
                break;
            }
        }
        return dataJsons;
    }

    ArrayList<TypeDataJson> adapterLists;

    /**
     * 数据库搜索数据
     *
     * @param data
     */
    private void searchData(final String data) {
        index = 0;
        showProgressDialog("加载中...");
        Observable.create(new ObservableOnSubscribe<ArrayList<TypeDataJson>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<TypeDataJson>> e) throws Exception {
                int i = searchType(data);
                ArrayList<CategoryDB> mLists = new ArrayList<>();
                switch (i) {
                    case 1:
                        if (data.length() == 4) {
                            //只有年
                            Integer year = Integer.parseInt(data);
                            ArrayList<CategoryDB> yearData = CategoryDaoOperator.newInstance().getYearData(year);
                            if (yearData != null)
                                mLists.addAll(yearData);
                        } else if (data.length() == 10) {
                            //年月日
                            String[] split = data.split("/");
                            int year = Integer.parseInt(split[0]);
                            int month = Integer.parseInt(split[1]);
                            int day = Integer.parseInt(split[2]);
                            ArrayList<CategoryDB> yearData = CategoryDaoOperator.newInstance().getYMDData(year, month, day);
                            if (yearData != null)
                                mLists.addAll(yearData);
                        }
                        break;
                    case 2:
                        if (data.length() > 4) {
                            //只搜索备注
                            ArrayList<CategoryDB> demoName = CategoryDaoOperator.newInstance().getDemoName(data);
                            if (demoName != null) {
                                mLists.addAll(demoName);
                            }
                        } else {
                            //搜索类别和备注
                            ArrayList<CategoryDB> typeName = CategoryDaoOperator.newInstance().getTypeName(data);
                            if (typeName != null) {
                                mLists.addAll(typeName);
                            }
                            ArrayList<CategoryDB> demoName = CategoryDaoOperator.newInstance().getDemoName(data);
                            if (demoName != null) {
                                mLists.addAll(demoName);
                            }
                        }
                        break;
                    case 3:
                        e.onError(new Throwable());
                        break;
                }
                ArrayList<TypeDataJson> typeJson = getTypeJson(mLists);
                e.onNext(typeJson);
                e.onComplete();


            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList<TypeDataJson>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ArrayList<TypeDataJson> dataJsons) {
                adapterLists = dataJsons;
                adapter.setNewData(getLists());
            }

            @Override
            public void onError(Throwable e) {
                closeProgressDialog();
                ToastUtil.show(SearchActivity.this, "日期格式输入有误");
            }

            @Override
            public void onComplete() {
                closeProgressDialog();
            }
        });


    }


    /**
     * 获得搜索类型
     * 1先判断是不是数字
     *
     * @return (1代表日期，2代表备注或者类别，3代表错误日期格式)
     */
    private int searchType(String data) {
        int type = 1;
        try {
            Long da = Long.parseLong(data);
            if (da < 10000 && da >= 1970) {
                //日期中的年
                type = 1;
            } else {
                //属于备注或者自定义类别
                type = 3;
            }
        } catch (Exception e) {
            //属于备注或者自定义类别
            int length = data.length();
            if (length <= 10 && data.contains("/")) {
                boolean equals = false;
                try {
                    equals = data.substring(4, 5).equals("/");
                    //有可能是日期格式
                } catch (Exception oth) {

                }
                if (equals) {
                    try {
                        boolean validDate = isValidDate(data);
                        if (validDate) {
                            type = 1;
                        } else {
                            type = 3;
                        }

                    } catch (Exception ex) {
                        type = 2;
                    }
                } else {
                    type = 2;
                }

            } else {
                //备注或者类别（先看是不是类别，不是类别就是备注）
                type = 2;
            }

        }
        return type;

    }

    //判断是不是日期
    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }


    /**
     * 数据转化
     *
     * @param list
     * @return
     */
    private ArrayList<TypeDataJson> getTypeJson(ArrayList<CategoryDB> list) {
        ArrayList<TypeDataJson> datas = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TypeDataJson typeDataJson = new TypeDataJson();
            typeDataJson.setBill_id(list.get(i).getBill_id());
            typeDataJson.setCategory_id(list.get(i).getCategory_id());
            typeDataJson.setType(list.get(i).getType());
            typeDataJson.setDetail_icon(list.get(i).getImage_path());
            typeDataJson.setSmall_icon(list.get(i).getImage_path());
            typeDataJson.setAccount(list.get(i).getAccount() + "");
            typeDataJson.setName(list.get(i).getName());
            typeDataJson.setDemo(list.get(i).getDemo());
            typeDataJson.setDay(list.get(i).getYear() + "-" + list.get(i).getMonth() + "-" + list.get(i).getDay());
            datas.add(typeDataJson);
        }
        return datas;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshEvent event) {
        String string = activitySearchEdittext.getText().toString();
        if (TextUtils.isEmpty(string)) {
        } else {
            searchData(string);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
