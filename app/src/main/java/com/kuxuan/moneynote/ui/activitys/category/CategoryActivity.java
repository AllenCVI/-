package com.kuxuan.moneynote.ui.activitys.category;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.mvpbase.MVPFragmentActivity;
import com.kuxuan.moneynote.ui.activitys.eventbus.LoginOutEvent;
import com.kuxuan.moneynote.ui.weight.CustormDialog;
import com.kuxuan.moneynote.utils.DrawableUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author hfrx
 */
public class CategoryActivity extends MVPFragmentActivity<CategoryPresent, CategoryModel> implements CategoryContract.CategoryView {
    @Bind(R.id.recycler_system)
    RecyclerView mRecyclerSystem;
    @Bind(R.id.activity_category_group)
    RadioGroup mRadioGroup;
    @Bind(R.id.activity_category_pay)
    RadioButton pay_btn;
    @Bind(R.id.activity_category_income_rgbtn)
    RadioButton income_btn;
    String type = "2";
    @Bind(R.id.recycler_remove)
    RecyclerView mRecyclerRemove;
    @Bind(R.id.more)
    TextView more;
    @Bind(R.id.activity_category_radio_layout)
    LinearLayout radio_layout;

    public static void show(Context context) {
        Intent intent = new Intent(context, CategoryActivity.class);
        context.startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void showProgress() {
        showProgressDialog(getResources().getString(R.string.loadding));
    }

    @Override
    public void hideProgress() {
        closeProgressDialog();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {
        showProgress();
        //设置title
        getTitleView(1).setTitle(getResources().getString(R.string.me_category)).
                setTitleColor(this, R.color.white).
                setLeftImage(R.drawable.toolbar_navigation_icon_normal, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        mPresenter.initRecyclerRemove(this, mRecyclerSystem);
        mPresenter.initRecyclerAdd(this, mRecyclerRemove);
        mPresenter.getCategoryList("2");
        //只要对RadioGroup进行监听
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (R.id.activity_category_pay == checkedId) {
                    showProgress();
                    type = "2";
                    mPresenter.getCategoryList("2");
                } else if (R.id.activity_category_income_rgbtn == checkedId) {
                    showProgress();
                    type = "1";
                    mPresenter.getCategoryList("1");
                }
                changeColorForRadioGroup(Integer.parseInt(type));
            }
        });
        changeColorForRadioGroup(Integer.parseInt(type));
        radio_layout.setBackgroundColor(DrawableUtil.getSkinColor(CategoryActivity.this));

    }

    private void changeColorForRadioGroup(int statisc_type) {
        int c = DrawableUtil.getSkinColor(CategoryActivity.this);
        pay_btn.setTextColor(Color.WHITE);
        income_btn.setTextColor(Color.WHITE);
        switch (statisc_type) {
            case 1:
                income_btn.setTextColor(c);
                break;
            case 2:
                pay_btn.setTextColor(c);
                break;
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_category;
    }

    @OnClick(R.id.add_btn)
    void addClick() {

        LayoutInflater mInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.dialoglayout, null);
        LinearLayout layout = (LinearLayout) view
                .findViewById(R.id.dialoglayout);

        final EditText mEdit = new EditText(this);

        mEdit.setHint("最多支持4个字");
        mEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)}); //最大输入长度
        mEdit.setTextSize(14);
        TextView mTextView = new TextView(this);
        mTextView.setText("请输入分类名称");
        mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        mTextView.setTextSize(16);
        mTextView.setPadding(0, 16, 0, 0);
        mTextView.setTextColor(ContextCompat.getColor(this, R.color.first_text));
        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
        builder.setCustomTitle(mTextView);
        layout.addView(mEdit);
        builder.setView(view);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mText = mEdit.getText().toString();
                mPresenter.addCustomCategory(mText, type);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void getNullOrNot(boolean flag) {
        if (flag) {
            more.setVisibility(View.VISIBLE);
        } else {
            more.setVisibility(View.GONE);
        }
    }

    /**
     * 退出
     *
     * @param loginOutEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent2(LoginOutEvent loginOutEvent) {
        finish();
    }

    @Override
    public void checkDialog(boolean flag, final int position, final int type) {
        if (flag) {
            new CustormDialog(this)
                    .builder()
                    .setTitle("警告")
                    .setMsg("删除类别会同时删除该类别下的所有历史收支记录")
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mPresenter.deleteCategory(position, type, true);
                        }
                    })
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //填写事件
                        }
                    }).show();
        } else {
            mPresenter.deleteCategory(position, type, false);
        }
    }
}
