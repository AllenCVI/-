package com.kuxuan.moneynote.ui.activitys.edit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseActivity;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.TypeDataJson;
import com.kuxuan.moneynote.listener.UpDBListener;
import com.kuxuan.moneynote.ui.activitys.account.AccountActivity;
import com.kuxuan.moneynote.ui.activitys.eventbus.RefreshEvent;
import com.kuxuan.moneynote.ui.weight.CustormDialog;
import com.kuxuan.moneynote.utils.CalanderUtil;
import com.kuxuan.moneynote.utils.GlideUtil;
import com.kuxuan.moneynote.utils.JavaFormatUtils;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.NetWorkUtil;
import com.kuxuan.moneynote.utils.TimeUtlis;
import com.kuxuan.moneynote.utils.UIHelper;
import com.kuxuan.moneynote.utils.UpDBDataOpertor;
import com.kuxuan.sqlite.db.CategoryDB;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 编辑类别
 * Created by xieshengqi on 2017/10/25.
 */

public class EditBillActivity extends BaseActivity {
    @Bind(R.id.activity_edit_type)
    TextView activityEditType;
    @Bind(R.id.activity_edit_money)
    TextView activityEditMoney;
    @Bind(R.id.activity_edit_time)
    TextView activityEditTime;
    @Bind(R.id.activity_edit_beizhu)
    TextView activityEditBeizhu;

    public static final String DATA = "data";

    private String timeData;


    private CategoryDaoOperator categoryDaoOperator;


    /**
     * 账单id(必须有的)数据库里面的
     */
    private String bill_db_id;

    @Override
    public int getLayout() {
        return R.layout.activity_edit;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private TypeDataJson dayDataEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getTitleView(2).changeEditLayout();
        getTitleView(2).getEdit_back().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.dayDataEntity = (TypeDataJson) getIntent().getSerializableExtra(DATA);
        initData(dayDataEntity);
        categoryDaoOperator = CategoryDaoOperator.newInstance();
    }


    private void initData(TypeDataJson dayDataEntity) {
        bill_db_id = dayDataEntity.getBill_id();
        try {
            //格式为MM月dd日 星期几
            String[] da = dayDataEntity.getDay_type().split(" ");
            timeData = dayDataEntity.getCurrentYear() + "年" + da[0];
            activityEditTime.setText(timeData + " " + da[da.length - 1]);
        } catch (Exception e) {
            e.printStackTrace();
            //格式为YYYY-MM-DD
            String type = dayDataEntity.getDay();
            String[] splits = type.split("-");
            timeData = splits[0] + "年" + splits[1] + "月" + splits[2] + "日";
            String dayofweek = CalanderUtil.getDayofweek(type, "yyyy-MM-dd");
            activityEditTime.setText(timeData + " " + dayofweek);
        }

        if (dayDataEntity.getType() == 1) {
            activityEditType.setText("收入");
            String account = JavaFormatUtils.formatFloatNumber(Double.parseDouble(dayDataEntity.getAccount()));
            activityEditMoney.setText(account);
        } else {
            String account = JavaFormatUtils.formatFloatNumber(Double.parseDouble(dayDataEntity.getAccount().replace("-", "")));
            activityEditMoney.setText(account);
            activityEditType.setText("支出");
        }
        GlideUtil.setImageWithNoCache(this, dayDataEntity.getSmall_icon(), getTitleView(2).getEdit_image());
        activityEditBeizhu.setText(dayDataEntity.getDemo());
        getTitleView(2).getEdit_text().setText(dayDataEntity.getName());
        getTitleView(2).getEdit_image().setImageResource(R.mipmap.category_custom_selected);
    }

    private void initNewData() {
        String[] da = dayDataEntity.getDay_type().split(" ");
        timeData = da[0];
        if (dayDataEntity.getType() == 1) {
            activityEditType.setText("收入");
            String account = JavaFormatUtils.formatFloatNumber(Double.parseDouble(dayDataEntity.getAccount()));
            activityEditMoney.setText(account);
        } else {
            String account = JavaFormatUtils.formatFloatNumber(Double.parseDouble(dayDataEntity.getAccount().replace("-", "")));
            activityEditMoney.setText(account);
            activityEditType.setText("支出");
        }
        activityEditTime.setText(dayDataEntity.getDay_type());
        activityEditBeizhu.setText(dayDataEntity.getDemo());
        getTitleView(2).getEdit_text().setText(dayDataEntity.getName());
        GlideUtil.setImageWithNoCache(this, dayDataEntity.getSmall_icon(), getTitleView(2).getEdit_image());
//        getTitleView().getEdit_image().setImageResource(R.mipmap.category_custom_selected);
    }

    @OnClick({R.id.activity_edit_edit_text, R.id.activity_edit_delete_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_edit_edit_text:
                //// TODO: 2017/10/25 跳转记账页面
                Intent intent = new Intent();
                intent.putExtra(AccountActivity.NUM, dayDataEntity.getAccount());
                intent.putExtra(AccountActivity.BILL_ID, dayDataEntity.getId() + "");
                intent.putExtra(AccountActivity.BILL_DB_ID, bill_db_id);
                intent.putExtra(AccountActivity.TYPE, dayDataEntity.getType());
                intent.putExtra(AccountActivity.ID, dayDataEntity.getCategory_id() + "");
                intent.putExtra(AccountActivity.TIME, timeData);
                intent.putExtra(AccountActivity.DEMO, dayDataEntity.getDemo());
                intent.putExtra(AccountActivity.ISEDIT, true);
                intent.putExtra(AccountActivity.NAME, dayDataEntity.getName());
                intent.putExtra(AccountActivity.ICON_URL, dayDataEntity.getSmall_icon());
                UIHelper.openActivityWithIntentForResult(EditBillActivity.this, AccountActivity.class, intent, Constant.Code_Request.REQUESTCODE);
                break;
            case R.id.activity_edit_delete_text:
//删除
                makeSureDialog();
                break;
        }
    }

    CustormDialog dialog;

    private void makeSureDialog() {
        if (dialog == null) {
            dialog = new CustormDialog(this)
                    .builder()
                    .setTitle("确认删除？")
                    .setMsg("删除后数据不可恢复")
                    .setPositiveButton("删除", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //填写事件
                            deleteBill();
                        }
                    })
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //填写事件

                        }
                    });
        }

        dialog.show();
    }


    private void deleteBill() {
        showProgressDialog(getResources().getString(R.string.delteteing));
//        if (NetWorkUtil.isNetworkAvailable(this)) {
//            RetrofitClient.getApiService().deleteUserBill(new DeleteBody(dayDataEntity.getId() + "")).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
//                @Override
//                public void onError(ExceptionHandle.ResponeThrowable e) {
//                    closeProgressDialog();
//                    categoryDaoOperator.updataIsDelete(bill_db_id, true);
//                    EventBus.getDefault().post(new RefreshEvent());
//                    finish();
//                }
//
//                @Override
//                public void onSuccess(BaseJson<Object> objectBaseJson) {
//                    closeProgressDialog();
//                    if (objectBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
//                        //刷新主界面
//                        categoryDaoOperator.deleteData(bill_db_id);
//                    } else {
//                        ToastUtil.show(EditBillActivity.this, objectBaseJson.getError().get(0));
//                        categoryDaoOperator.updataIsDelete(bill_db_id, true);
//                    }
//                    EventBus.getDefault().post(new RefreshEvent());
//                    finish();
//                }
//            });
//
//        } else {

        //在没登录的状态下是真删，否则是假删
        categoryDaoOperator.updataIsDelete(bill_db_id, true);
        if (LoginStatusUtil.isLoginin()) {
            if (NetWorkUtil.isNetworkAvailable(this))
                sync();
            else{
                EventBus.getDefault().post(new RefreshEvent());
                closeProgressDialog();
                finish();
            }
        } else {
            categoryDaoOperator.deleteData(bill_db_id);
            EventBus.getDefault().post(new RefreshEvent());
            closeProgressDialog();
            finish();
        }

//        }
    }

    /**
     * 同步数据
     */
    private void sync() {
        if (LoginStatusUtil.isLoginin()) {
            UpDBDataOpertor.getInstance().onUpData(new UpDBListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess() {
                    EventBus.getDefault().post(new RefreshEvent());
                    closeProgressDialog();
                    finish();
                }

                @Override
                public void onFail() {
                    categoryDaoOperator.updataIsDelete(bill_db_id, true);
                    EventBus.getDefault().post(new RefreshEvent());
                    closeProgressDialog();
                    finish();
                }

                @Override
                public void onexitLoginFail(ArrayList<CategoryDB> needUpdataJson) {

                }

                @Override
                public void onNoNeedUpdata() {
                    EventBus.getDefault().post(new RefreshEvent());
                    closeProgressDialog();
                    finish();
                }
            });
//            final CategoryDaoOperator categoryDaoOperator = CategoryDaoOperator.newInstance();
//            final ArrayList<CategoryDB> needUpdataJson = categoryDaoOperator.getNeedUpdataJson();
//            if (needUpdataJson != null && needUpdataJson.size() != 0) {
//                String json = UpDataOperator.getInstance().getJson(needUpdataJson);
//                RetrofitClient.getApiService().upData(new UpDataBody(0, json)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
//                    @Override
//                    public void onError(ExceptionHandle.ResponeThrowable e) {
//                        categoryDaoOperator.updataIsDelete(bill_db_id, true);
//                        EventBus.getDefault().post(new RefreshEvent());
//                        closeProgressDialog();
//                        finish();
//                    }
//
//                    @Override
//                    public void onSuccess(BaseJson<Object> objectBeanNewJson) {
//                        //更新数据库
//                        if(objectBeanNewJson!=null&&objectBeanNewJson.getCode()==0){
//                            for (CategoryDB categoryDB : needUpdataJson) {
//                                if (categoryDB.getStatus() == 1) {
//                                    categoryDaoOperator.deleteData(categoryDB.getBill_id());
//                                } else {
//                                    categoryDaoOperator.updataNoNeedSynce(categoryDB.getBill_id());
//                                }
//                            }
//                        }else{
//                            for (CategoryDB categoryDB : needUpdataJson) {
//                                categoryDaoOperator.updataNeedSynce(categoryDB.getBill_id());
//                            }
//                        }
//
//                        EventBus.getDefault().post(new RefreshEvent());
//                        closeProgressDialog();
//                        finish();
//                    }
//                });
//            }else{
//                EventBus.getDefault().post(new RefreshEvent());
//                closeProgressDialog();
//                finish();
//            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.Code_Request.REQUESTCODE) {
            if (resultCode == Constant.Code_Request.RESULTCODE) {
                if (data != null) {
                    String account = data.getStringExtra(AccountActivity.NUM);
                    String catorgy_id = data.getStringExtra(AccountActivity.ID);
                    //时间戳
                    String time = data.getStringExtra(AccountActivity.TIME);
                    //转化为yyyy年MM月dd日
                    String s = TimeUtlis.TimeStamp2Date(time, TimeUtlis.TIME_FORMAT_18);
                    //转化为yyyy年MM月dd日 星期日
                    String dayofweek = CalanderUtil.getDayofweek(s);
                    int type = data.getIntExtra(AccountActivity.TYPE, -1);
                    dayDataEntity.setAccount(account);
                    dayDataEntity.setDemo(data.getStringExtra(AccountActivity.DEMO));
                    dayDataEntity.setCategory_id(Integer.parseInt(catorgy_id));
                    dayDataEntity.setDay_type(s + " " + dayofweek);
                    dayDataEntity.setSmall_icon(data.getStringExtra(AccountActivity.ICON_URL));
                    if (type != -1)
                        dayDataEntity.setType(type);
                    String name = data.getStringExtra(AccountActivity.NAME);
                    if (!TextUtils.isEmpty(name))
                        dayDataEntity.setName(name);
                    initNewData();
                }
            }

        }


    }
}
