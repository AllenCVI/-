package com.kuxuan.moneynote.ui.activitys.bill;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.BillJson;
import com.kuxuan.moneynote.json.BillJsonList;
import com.kuxuan.moneynote.json.Time;
import com.kuxuan.moneynote.listener.MVPListener;
import com.kuxuan.moneynote.ui.adapter.BillAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class BillPresenter extends BillContract.BillPresent {
    BillAdapter mAdapter;
    Calendar ca;
    Context mContext;
    List<BillJsonList> billJsonList;
    private static final String TAG = "BillPresenter";

    @Override
    void initRecyclerView(Context context, RecyclerView recyclerView) {
        mContext = context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new BillAdapter(R.layout.item_billl_layout);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    void addData(Time time) {

    }

    @Override
    void getBillData(int year) {
        billJsonList = new ArrayList<>();
        for(int i=1;i<=12;i++){
            billJsonList.add(new BillJsonList("0","0","0",i+"月"));
        }
        view.setData();
        Log.e(TAG,"---------");
        ca = Calendar.getInstance();
        final int month = ca.get(Calendar.MONTH)+1;
        mModel.getBillData(new MVPListener<BillJson>() {
            @Override
            public void onSuccess(BillJson content) {
                if(view!=null){
                    view.hideProgress();
                }
                if(content!=null){
                    if(view!=null){
                        if(content.getTotal_balance()==null){

                            mAdapter.setNewData(billJsonList);
                        }else{
                            mAdapter.setNewData(content.getmBillJsonList());
                            view.setBillData(content);
                        }
                    }

                }else{
                    view.setData();
                }
            }

            @Override
            public void onFail(String msg) {

            }
        },String.valueOf(year));
    }

    @Override
    void getOfflineData(int year) {

        Calendar cal = Calendar.getInstance();
        int Current_year = cal.get(Calendar.YEAR);
        int Current_month = cal.get(Calendar.MONTH)+1;

        int monthCount = 12;

        if(year == Current_year){
            monthCount = Current_month;
        }

        CategoryDaoOperator categoryDaoOperator  = CategoryDaoOperator.newInstance();

        List<BillJsonList> billJsonLists=new ArrayList<>();

        for (int i=monthCount;i>=1;i--){

            BillJsonList billJsonList = categoryDaoOperator.getMonthData(year,i);
            billJsonLists.add(billJsonList);

        }
        mAdapter.setNewData(billJsonLists);
       view.setOffLineBillData(billJsonLists);

    }
}
