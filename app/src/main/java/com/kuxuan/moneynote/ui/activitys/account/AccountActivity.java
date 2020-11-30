package com.kuxuan.moneynote.ui.activitys.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.mvpbase.MVPFragmentActivity;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.db.BillCategoreDaoOperator;
import com.kuxuan.moneynote.json.CategoryList;
import com.kuxuan.moneynote.json.netbody.BillBody;
import com.kuxuan.moneynote.ui.activitys.eventbus.RefreshEvent;
import com.kuxuan.moneynote.ui.activitys.login.LoginActivity;
import com.kuxuan.moneynote.ui.adapter.AccountAdapterOffline;
import com.kuxuan.moneynote.ui.weight.CaculatorLayout;
import com.kuxuan.moneynote.ui.weight.MoneyChoosePop;
import com.kuxuan.moneynote.utils.DisplayUtil;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.NetWorkUtil;
import com.kuxuan.moneynote.utils.PickerUtil;
import com.kuxuan.moneynote.utils.TimeUtlis;
import com.kuxuan.moneynote.utils.ToastUtil;
import com.kuxuan.moneynote.utils.UIHelper;
import com.kuxuan.sqlite.db.BillCategoreDB;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.addapp.pickers.picker.DatePicker;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class AccountActivity extends MVPFragmentActivity<AccountPresenter, AccountModel> implements AccountContract.AccountView, AccountPresenter.ClickListener, CaculatorLayout.Sum {
    @Bind(R.id.recycler)
    RecyclerView mRecyclerView;

    @Bind(R.id.keybord)
    CaculatorLayout keybord;

    MoneyChoosePop popupWindow;
    private String category_id;
    //支出还是收入
    private int mType;
    @Bind(R.id.num_edit)
    EditText mNumEdit;
    @Bind(R.id.calculator_result_btn)
    TextView mResult;
    int mYear;
    int mMonth;
    int mDay;
    private static final String TAG = "AccountActivity";

    public static final String NUM = "NUM";
    public static final String TIME = "TIME";
    public static final String ID = "ID";
    public static final String ISEDIT = "isedit";
    public static final String TYPE = "type";
    public static final String DEMO = "demo";
    public static final String BILL_ID = "bill_id";
    public static final String BILL_DB_ID = "bill_db_id";
    public static final String ICON_URL = "icon_url";
    private static final String EQUAL = "=";
    private static final String COMPLETE = "完成";
    public static final String NAME = "name";
    private static final int LENGTH_2 = 2;

    private String icon_url;
    private String numNumber;
    private String time;
    private String id;

    /**
     * 更新账单时用到的
     */
    private String bill_id;
    private String bill_db_id;

    private boolean isEdit;


    /**
     * 符号标志位
     */
    int symbol;

    @Bind(R.id.num)
    TextView num;
    String numText = "";

    @Bind(R.id.num_day)
    TextView numDay;


    String daytime;

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 类别名字
     */
    private String name;

    /**
     * @param context
     * @param num     金额
     * @param time    时间   xxxx年xx月xx日
     * @param id      category_id;
     */
    public static void show(Context context, String num, String time, String id) {
        Intent intent = new Intent(context, AccountActivity.class);
        intent.putExtra(NUM, num);
        intent.putExtra(TIME, time);
        intent.putExtra(ID, id);
        context.startActivity(intent);
    }

    @Override
    public void showProgress() {
        showProgressDialog(getResources().getString(R.string.loadding));
    }

    @Override
    public void hideProgress() {
        closeProgressDialog();
    }

    String demo;


    @Override
    public void initView() {
        getTitleView(1).setTitle("支出").setRightText("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //出场动画
                overridePendingTransition(R.anim.activity_stay, R.anim.push_bottom_out);
            }
        });
        Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.icon_sanjiao_white);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        getTitleView(1).getTitle_text().setCompoundDrawablePadding(DisplayUtil.dip2px(5));
        getTitleView(1).getTitle_text().setCompoundDrawables(null, null, drawable, null);
        getTitleView(1).getTitle_text().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPop();
            }
        });
        mPresenter.setmContext(this);
        boolean networkAvailable = NetWorkUtil.isNetworkAvailable(this);
        if (networkAvailable) {
            onLine = 0;
        } else {
            onLine = 1;
        }

        if (onLine == 0) {
            mPresenter.initRecyclerView(this, mRecyclerView, 1, this);
            if (LoginStatusUtil.isLoginin()) {
                mPresenter.getCategoryList("2");
            } else {
                mPresenter.getNoLoginCategoryList("2");
            }
        } else {
            initOffLineRecycerView(2);
        }


        Calendar c = Calendar.getInstance();
        //TODO
        mYear = c.get(Calendar.YEAR); // 获取当前年份
        mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        daytime = mYear + "年" + mMonth + "月" + mDay + "日";
        Intent intent = getIntent();
        if (intent != null) {
            numNumber = intent.getStringExtra(NUM);
            id = intent.getStringExtra(ID);
            time = intent.getStringExtra(TIME);
            demo = intent.getStringExtra(DEMO);
        }

        if (demo != null) {
            keybord.setBeizhu(demo);
        }

        if (time != null) {
            numDay.setTextSize(14);
            long lingtime = TimeUtlis.parseTime(time, TimeUtlis.TIME_FORMAT_18);
            daytime = time;
            String createTime = TimeUtlis.getCreateTime(lingtime);
            numDay.setText(createTime);
        }
        if (numNumber != null) {
            num.setText(numNumber);
            keybord.setNumText("0");
            keybord.setShowNumText("0");
            keybord.setCompleteNumtext("0");
        }
        //编辑页跳转过来用到
        isEdit = intent.getBooleanExtra(ISEDIT, false);
        if (isEdit) {
            bill_id = intent.getStringExtra(BILL_ID);
            category_id = intent.getStringExtra(ID);
            keybord.setVisibility(View.VISIBLE);
            icon_url = intent.getStringExtra(ICON_URL);
            bill_db_id = intent.getStringExtra(BILL_DB_ID);
            name = intent.getStringExtra(NAME);
        }
        mType = intent.getIntExtra(TYPE, 2);
        keybord.setSum(this);


    }


    RecyclerView recyclerView1;
    List<BillCategoreDB> billCategoreDBList;
    AccountAdapterOffline accountAdapterOffline_adapter;


    private void initOffLineRecycerView(int type) {


        recyclerView1 = (RecyclerView) findViewById(R.id.recycler);
        GridLayoutManager mgr = new GridLayoutManager(this, 4);
        recyclerView1.setLayoutManager(mgr);
        BillCategoreDaoOperator billCategoreDaoOperator = BillCategoreDaoOperator.newInstance();

        billCategoreDBList = billCategoreDaoOperator.getType(type);
        billCategoreDBList.add(new BillCategoreDB());

        accountAdapterOffline_adapter = new AccountAdapterOffline(R.layout.cell_account_item, billCategoreDBList);
        recyclerView1.setAdapter(accountAdapterOffline_adapter);


        accountAdapterOffline_adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (position == billCategoreDBList.size() - 1) {

                    boolean isLogin = LoginStatusUtil.isLoginin();
                    if (isLogin) {
                        ToastUtil.show(getApplicationContext(), "请设置网络");
                    } else {
                        UIHelper.openActivity(AccountActivity.this, LoginActivity.class);
                    }
                    return;
                }

                for (int i = 0; i < billCategoreDBList.size(); i++) {
                    BillCategoreDB billCategoreDB = billCategoreDBList.get(i);
                    if (i == position) {
                        billCategoreDB.setClick(true);
                    } else {
                        billCategoreDB.setClick(false);
                    }
                }
                accountAdapterOffline_adapter.notifyDataSetChanged();

                click(billCategoreDBList.get(position).getId() + "", billCategoreDBList.get(position).getType(), billCategoreDBList.get(position).getIcon(), billCategoreDBList.get(position).getName());
            }
        });


    }

    private void showPop() {
        if (popupWindow == null) {
            popupWindow = new MoneyChoosePop(this);
            popupWindow.setOnPopClickListener(new MoneyChoosePop.OnPopClickListener() {
                @Override
                public void onClick(int type) {
                    //todo 选择支出还是收入
                    switch (type) {
                        case MoneyChoosePop.ZHICHU:
                            //支出
                            if (onLine == 0) {
                                getTitleView(1).setTitle("支出");
                                mPresenter.initRecyclerView(AccountActivity.this, mRecyclerView, 1, AccountActivity.this);
                                if (LoginStatusUtil.isLoginin())
                                    mPresenter.getCategoryList("2");
                                else
                                    mPresenter.getNoLoginCategoryList("2");
                                mType = 2;
                            } else {

                                initOffLineRecycerView(2);

                            }
                            break;
                        case MoneyChoosePop.SHOURU:
                            //收入
                            if (onLine == 0) {
                                getTitleView(1).setTitle("收入");
                                mPresenter.initRecyclerView(AccountActivity.this, mRecyclerView, 2, AccountActivity.this);
                                if (LoginStatusUtil.isLoginin())
                                    mPresenter.getCategoryList("1");
                                else
                                    mPresenter.getNoLoginCategoryList("1");
                                mType = 1;
                            } else {

                                initOffLineRecycerView(1);

                            }
                            break;
                        default:
                            break;
                    }
                }
            });
        }
        popupWindow.showAsDropDown(findViewById(R.id.activity_account_title_layout), 0, 0);

    }

    @Override
    public int getLayout() {
        return R.layout.activity_account;
    }

    public static void show(Context context) {
        Intent intent = new Intent(context, AccountActivity.class);
        context.startActivity(intent);
    }


    int onLine = 0;

    public static void show(Context context, int online_OR_Offline) {

//        onLine = online_OR_Offline;
        Intent intent = new Intent(context, AccountActivity.class);
        context.startActivity(intent);

    }


    private void click(String id, int type, String detill_url, String name) {

        this.category_id = id;
        this.mType = type;
        this.icon_url = detill_url;
        this.name = name;
        keybord.setVisibility(View.VISIBLE);

    }


    @Override
    public void click(String category_id, int type, String icon_url, CategoryList categoryList) {

        this.category_id = category_id;
        this.mType = type;
        this.icon_url = icon_url;
        this.name = categoryList.getName();
        keybord.setVisibility(View.VISIBLE);

    }


    @OnClick(R.id.num_day)
    void num_dayClick() {
        Log.e(TAG, daytime);
        PickerUtil.onYearMonthDayPicker("选择日期", mYear, mMonth, mDay, this, new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String s, String s1, String s2) {
                if (!(s.equals(String.valueOf(mYear)) && s1.equals(String.valueOf(mMonth)) && s2.equals(String.valueOf(mDay)))) {
                    numDay.setTextSize(14);
                    numDay.setText(s + "/" + s1 + "/" + s2);
                } else {
                    numDay.setText("今天");
                }
                daytime = s + "年" + s1 + "月" + s2 + "日";
            }
        });
    }


    public static String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(user_time + " 00:00:00");
            long l = d.getTime() / 1000;
            String str = String.valueOf(l);
//            re_time = str.substring(0, 10);
            re_time = str;

        } catch (ParseException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return re_time;
    }


    @Override
    public void getCategorySuccess() {
        mPresenter.setCategory(ID);
    }

    @Override
    public void finishActivity() {
        EventBus.getDefault().post(new RefreshEvent());
        finish();
    }

    @Override
    public void setResultData(BillBody billBody) {
        Intent intent = new Intent();
        intent.putExtra(NUM, billBody.getAccount());
        intent.putExtra(ID, billBody.getCategory_id());
        intent.putExtra(TIME, billBody.getTime());
        intent.putExtra(TYPE, billBody.getType());
        intent.putExtra(DEMO, billBody.getDemo());
        intent.putExtra(NAME, name);
        intent.putExtra(ICON_URL, icon_url);
        setResult(Constant.Code_Request.RESULTCODE, intent);
        EventBus.getDefault().post(new RefreshEvent());
        finish();
    }

    @Override
    public void showMsg(String msg) {
        ToastUtil.show(this, msg);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void complete() {

        String numtext = num.getText().toString();
        try {
            double sumDouble = Double.parseDouble(numtext);
            if (sumDouble <= 0) {
                return;
            }
            if (COMPLETE.equals(mResult.getText())) {
                if (isEdit) {
                    if (category_id.equals("0")) {
                        return;
                    }
                    BillBody billBody = new BillBody(category_id, mType, num.getText().toString(), getTime(daytime), mNumEdit.getText().toString(), bill_id);
                    mPresenter.uploadBill(billBody, onLine, this.name, bill_db_id);
                } else {
                    BillBody billBody = new BillBody(category_id, mType, num.getText().toString(), getTime(daytime), mNumEdit.getText().toString());
                    mPresenter.addAccount(billBody, onLine, this.name);
                }
            }
        } catch (Exception e) {

        }

    }
}
