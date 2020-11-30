package com.kuxuan.moneynote.ui.fragments.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kuxuan.moneynote.MyApplication;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseFragmentActivity;
import com.kuxuan.moneynote.base.mvpbase.MVPFragment;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.BillJson;
import com.kuxuan.moneynote.json.HeadImg;
import com.kuxuan.moneynote.json.MineJson;
import com.kuxuan.moneynote.ui.activitys.AboutActivity;
import com.kuxuan.moneynote.ui.activitys.alarm.AlarmActivity;
import com.kuxuan.moneynote.ui.activitys.bill.BillActivity;
import com.kuxuan.moneynote.ui.activitys.bindphone.BindThirdActivity;
import com.kuxuan.moneynote.ui.activitys.budget.BudgetActivity;
import com.kuxuan.moneynote.ui.activitys.category.CategoryActivity;
import com.kuxuan.moneynote.ui.activitys.eventbus.BugetEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.LoginEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.LoginOutEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.NetworkEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.RefreshEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.SkinEvent;
import com.kuxuan.moneynote.ui.activitys.exportbill.Activity_ExportBill;
import com.kuxuan.moneynote.ui.activitys.login.LoginActivity;
import com.kuxuan.moneynote.ui.activitys.opinion.OptionActivity;
import com.kuxuan.moneynote.ui.activitys.person.PersonActivity;
import com.kuxuan.moneynote.ui.activitys.score.UserScoreActivity;
import com.kuxuan.moneynote.ui.weight.MyToast;
import com.kuxuan.moneynote.utils.GlideUtil;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.NetWorkUtil;
import com.kuxuan.moneynote.utils.SPUtil;
import com.kuxuan.moneynote.utils.TextSetUtil;
import com.kuxuan.moneynote.utils.UIHelper;
import com.kuxuan.sqlite.db.CategoryDB;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
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
public class MineFragment extends MVPFragment<MinePresent, MineModel> implements MineContract.MineView {
    @Bind(R.id.category_lin)
    LinearLayout category_lin;
    @Bind(R.id.alarm_lin)
    LinearLayout alarm_lin;
    @Bind(R.id.recommend_lin)
    LinearLayout recommend_lin;

    @Bind(R.id.logo_lin)
    LinearLayout logo_lin;

    @Bind(R.id.bill_lin)
    LinearLayout bill_lin;

    @Bind(R.id.im_portrait)
    CircleImageView mPortraitImage;

    @Bind(R.id.name)
    TextView mNameText;

    @Bind(R.id.month)
    TextView monthTv;

    @Bind(R.id.income_text)
    TextView incomeText;

    @Bind(R.id.pay_text)
    TextView payText;

    @Bind(R.id.balance_text)
    TextView balanceText;


    Calendar cal;
    String month;
    String year;
    private static final String TAG = "MineFragment";
    private BaseFragmentActivity activity;
    private ShareAction mShareAction;
    private UMShareListener mShareListener;
    String aaaa = "http://www.baidu.com";

    UMWeb web;

    /**
     * 类别设置的点击事件
     */
    @OnClick(R.id.category_lin)
    void categoryClick() {
        if (LoginStatusUtil.isLoginin()) {
            if (NetWorkUtil.isNetworkAvailable(getActivity())) {
                CategoryActivity.show(getActivity());
            } else {
                MyToast.makeText(getActivity(), getResources().getString(R.string.nonetwork), Toast.LENGTH_SHORT).show();
            }
        } else {
            UIHelper.openActivity(getActivity(), LoginActivity.class);
        }
    }

    /**
     * 定时提醒的点击事件
     */
    @OnClick(R.id.alarm_lin)
    void alarmClick() {
        AlarmActivity.show(getActivity());
    }

    /**
     * 推荐给好友的点击事件
     */
    @OnClick(R.id.recommend_lin)
    void recommendClick() {
        if (!LoginStatusUtil.isLoginin()) {
            UIHelper.openActivity(getActivity(), LoginActivity.class);
            return;
        }
        ShareBoardConfig config = new ShareBoardConfig();
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
        config.setTitleVisibility(false);
        config.setCancelButtonText("取消");
        config.setIndicatorVisibility(false);

        if (mShareAction != null) {
            mShareAction.open(config);
        }


    }

    /**
     * 意见反馈的点击事件
     */
    @OnClick(R.id.advice_lin)
    void adviceClick() {
        if (LoginStatusUtil.isLoginin()) {
            OptionActivity.show(getActivity());
        } else {
            UIHelper.openActivity(getActivity(), LoginActivity.class);
        }

    }

    /**
     * 关于的点击事件
     */
    @OnClick(R.id.logo_lin)
    void logoClick() {
        AboutActivity.show(getActivity());
    }

    @Override
    public void showProgress() {
        activity.showProgressDialog(getResources().getString(R.string.loadding));
    }


    @OnClick(R.id.lin_bill_export)
    void bill_exportOnclick() {
        if (LoginStatusUtil.isLoginin()) {
            Activity_ExportBill.show(getActivity());
        } else {
            UIHelper.openActivity(getActivity(), LoginActivity.class);
        }

    }


    @Override
    public void hideProgress() {
        activity.closeProgressDialog();
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);

        int position = (int) SPUtil.get(getContext(), Constant.Skin.CHECKED, -1);
        if (position > -1) {
            String fileheader = MyApplication.getInstance().getApplicationContext().getFilesDir().getPath() + "/" + "skin/";

            if (position == 1000) {
                String fileURl = (String) SPUtil.get(getContext(), Constant.Skin.CUTPIC, "");
                if (!fileURl.equals("")) {
                    GlideUtil.setImageWithNoCache(getContext(), new File(fileURl), iv_mineskin);
                }
            } else {
                File file = new File(fileheader + Constant.Skin.HOMEPICNAME + position + ".png");
                GlideUtil.setImageWithNoCache(getContext(), file, iv_mineskin);
            }

        }


        cal = Calendar.getInstance();
        activity = (BaseFragmentActivity) getActivity();
        mShareListener = new CustomShareListener(getActivity());
        month = cal.get(Calendar.MONTH) + 1 + "";
        year = cal.get(Calendar.YEAR) + "";
        TextSetUtil.setTextForMonth(month + "月", monthTv, Color.BLACK);
        if (LoginStatusUtil.isLoginin()) {
            MineJson userInfo = LoginStatusUtil.getUserInfo();
            if (userInfo == null) {
                mPresenter.getMineData();
            } else {
                setMineData(userInfo);
            }
//            mPresenter.getMineBill(year);
        }
        mShareAction = new ShareAction(getActivity()).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE).setShareboardclickCallback(new ShareBoardlistener() {
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                UMWeb web = new UMWeb(aaaa);
                web.setTitle(getResources().getString(R.string.app_name));
                web.setDescription(getResources().getString(R.string.app_name) + "，最简洁的随手记账软件，官方推荐，百万财富用户的记账首选App");
                web.setThumb(new UMImage(getActivity(), R.mipmap.app_icon));
                new ShareAction(getActivity()).withMedia(web)
                        .setPlatform(share_media)
                        .setCallback(mShareListener)
                        .share();
            }
        });

        TextSetUtil.setTextForMoey("0.00", incomeText, 16, 12);
        TextSetUtil.setTextForMoey("0.00", payText, 16, 12);
        TextSetUtil.setTextForMoey("0.00", balanceText, 16, 12);

        setBillData();


    }

    /**
     * 获取某月账单
     */
    private void setBillData() {


        Observable.create(new ObservableOnSubscribe<HashMap<String, String>>() {
            @Override
            public void subscribe(ObservableEmitter<HashMap<String, String>> e) throws Exception {
                CategoryDaoOperator categoryDaoOperator = CategoryDaoOperator.newInstance();
                //收入 type ==1
                ArrayList<CategoryDB> categoryDBArrayList_Income = categoryDaoOperator.getMonthData(Integer.parseInt(year), Integer.parseInt(month), 1);

                double income = 0;

                for (int i = 0; i < categoryDBArrayList_Income.size(); i++) {

                    income = income + categoryDBArrayList_Income.get(i).getAccount();

                }


                ArrayList<CategoryDB> categoryDBArrayList_expenditure = categoryDaoOperator.getMonthData(Integer.parseInt(year), Integer.parseInt(month), 2);

                double expenditure = 0;

                for (int i = 0; i < categoryDBArrayList_expenditure.size(); i++) {

                    expenditure = expenditure + categoryDBArrayList_expenditure.get(i).getAccount();

                }

                double surplus = 0;

                surplus = income - expenditure;

                String incomeStr = TextSetUtil.formatFloatNumber(income);
                String expenditureStr = TextSetUtil.formatFloatNumber(expenditure);
                String surplusStr = TextSetUtil.formatFloatNumber(surplus);
                HashMap<String, String> map = new HashMap<>();
                map.put("incomeStr", incomeStr);
                map.put("expenditureStr", expenditureStr);
                map.put("surplusStr", surplusStr);
                e.onNext(map);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HashMap<String, String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HashMap<String, String> stringDoubleHashMap) {
                TextSetUtil.setTextForMoey(stringDoubleHashMap.get("incomeStr"), incomeText, 16, 12);
                TextSetUtil.setTextForMoey(stringDoubleHashMap.get("expenditureStr"), payText, 16, 12);
                TextSetUtil.setTextForMoey(stringDoubleHashMap.get("surplusStr"), balanceText, 16, 12);

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    //账单信息
//    @OnClick(R.id.fragment_mine_accountbill)
    @OnClick(R.id.bill_lin)
    void billClick() {
        BillActivity.show(getActivity());
    }

    /**
     * 预算设置
     */
    @OnClick(R.id.yusuan_lin)
    void yusuanClick() {
        if (LoginStatusUtil.isLoginin()) {
            Intent intent = new Intent(getActivity(), BudgetActivity.class);
            startActivity(intent);
        } else {
            UIHelper.openActivity(getActivity(), LoginActivity.class);
        }

    }

    //积分信息
    @OnClick(R.id.fragment_mine_scroetext)
    void scoreClick() {
        Intent intent = new Intent(getActivity(), UserScoreActivity.class);
        startActivity(intent);
    }

    //个人信息
    @OnClick(R.id.head_lin)
    void headClick() {
        if (!LoginStatusUtil.isLoginin()) {
            UIHelper.openActivity(getActivity(), LoginActivity.class);
        } else {
            PersonActivity.show(getActivity());
        }
    }


    boolean newUser;

    /**
     * 登陆之后刷新数据
     *
     * @param loginEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginEvent loginEvent) {

        if (LoginStatusUtil.isLoginin()) {
            newUser = loginEvent.isNewUser();
            MineJson userInfo = LoginStatusUtil.getUserInfo();
            if (userInfo == null)
                mPresenter.getMineData();
            else {
                setMineData(userInfo);
            }
//            mPresenter.getMineBill(year);
        }
            setBillData();

    }


    /**
     * 选头像后刷新
     *
     * @param headImg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HeadImg headImg) {

        Glide.with(this).load(headImg.getUrl())
                .into(mPortraitImage);

    }


    /**
     * add账单之后刷新数据
     *
     * @param loginEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshEvent loginEvent) {
        if (LoginStatusUtil.isLoginin()) {
            MineJson userInfo = LoginStatusUtil.getUserInfo();
            if (userInfo == null)
                mPresenter.getMineData();
            else {
                setMineData(userInfo);
            }
//            mPresenter.getMineBill(year);
        }
//        mPresenter.getMineBill(year);
        setBillData();

    }

    /**
     * 网络变化
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NetworkEvent event) {
        if (NetWorkUtil.isNetworkAvailable(getActivity()) && LoginStatusUtil.isLoginin()) {
            mPresenter.getMineData();
        }

    }


    @Bind(R.id.iv_mineskin)
    ImageView iv_mineskin;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent5(SkinEvent skinBean) {
        String fileheader = MyApplication.getInstance().getApplicationContext().getFilesDir().getPath() + "/" + "skin/";
        if (skinBean.getCode() == 1000) {
            String fileURl = (String) SPUtil.get(getContext(), Constant.Skin.CUTPIC, "");
            if (!fileURl.equals("")) {
                GlideUtil.setImageWithNoCache(getContext(), new File(fileURl), iv_mineskin);
            }
        } else {
            File file = new File(fileheader + Constant.Skin.MINE + skinBean.getCode() + ".png");
            GlideUtil.setImageWithNoCache(getContext(), file, iv_mineskin);
        }

    }


    /**
     * 退出
     *
     * @param loginOutEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent2(LoginOutEvent loginOutEvent) {
//        mPresenter.getMineData();
//        mPresenter.getMineBill("2017");
        if (LoginStatusUtil.getToken() == null) {
            if (mNameText != null) {
                mNameText.setText("未登录");
                mPortraitImage.setImageResource(R.drawable.im_portrait);
//                TextSetUtil.setTextForMoey("0.00", incomeText, 16, 12);
//                TextSetUtil.setTextForMoey("0.00 ", payText, 16, 12);
//                TextSetUtil.setTextForMoey("0.00", balanceText, 16, 12);
            }
//刷新账单
            setBillData();

        }
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void setMineData(final MineJson mineModel) {
        if (mineModel.getUsername() == null && mineModel.getNickname() == null) {
            mNameText.setText("");
        } else {
            if (mineModel.getUsername() == "") {
                mNameText.setText(mineModel.getNickname());
            } else {
                mNameText.setText(mineModel.getUsername());
            }
        }

        if (mineModel.getAvatar() == null && mineModel.getHeadimgurl() == null) {

        } else {
            if (mineModel.getAvatar() == null) {
                Glide.with(this).load(mineModel.getHeadimgurl())
                        .into(mPortraitImage);
            } else {
                Glide.with(this).load(mineModel.getAvatar())
                        .into(mPortraitImage);
            }
        }

        //预算设置
        String month_budget = mineModel.getMonth_budget();
        long budget = 0;
        try {
            budget = (long) Double.parseDouble(month_budget);
        } catch (Exception e) {

        }
        if (budget == 0) {
            SPUtil.putAndApply(MyApplication.getInstance(), Constant.System.SWITCH, false);
            SPUtil.putAndApply(MyApplication.getInstance(), Constant.System.BUGET_NUM, Constant.System.NORMAL_NUM);
        } else {
            SPUtil.putAndApply(MyApplication.getInstance(), Constant.System.SWITCH, true);
            SPUtil.putAndApply(MyApplication.getInstance(), Constant.System.BUGET_NUM, budget);
        }
        EventBus.getDefault().post(new BugetEvent());
        // 强制绑定手机号入口
//        if((mineModel.getMobile()==null||mineModel.getMobile().equals(""))){

        boolean isWeichatLogin = (Boolean) SPUtil.get(getActivity(), Constant.IsFirstWEiChatLogin.ISWEICHATLOGIN, false);

        if (isWeichatLogin) {
            boolean flag = (boolean) SPUtil.get(getActivity(), Constant.IsFirstWEiChatLogin.ISFIRSTWEICHATLOGIN, false);

            if (newUser && (mineModel.getMobile().equals("") || mineModel.getMobile() == null)) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IsFirstWEiChatLogin.NEWUSER, newUser);
                bundle.putBoolean(Constant.IsFirstWEiChatLogin.ISFIRSTWEICHATLOGIN, true);
                UIHelper.openActivityWithBundle(getActivity(), BindThirdActivity.class, bundle);
                return;
            }

            if (!flag && (mineModel.getMobile().equals("") || mineModel.getMobile() == null)) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IsFirstWEiChatLogin.NEWUSER, newUser);
                bundle.putBoolean(Constant.IsFirstWEiChatLogin.ISFIRSTWEICHATLOGIN, true);
                UIHelper.openActivityWithBundle(getActivity(), BindThirdActivity.class, bundle);
            }

        }
        aaaa = mineModel.getAndroid_share_url();


    }

    @Override
    public void setMineBillData(BillJson billJson) {
        if (billJson == null) {
            return;
        }
        if (billJson.getmBillJsonList() != null) {
            for (int i = 0; i < billJson.getmBillJsonList().size(); i++) {
                if (billJson.getmBillJsonList().get(i).getMonth().equals(month + "月")) {
                    TextSetUtil.setTextForMoey(billJson.getmBillJsonList().get(i).getIncome(), incomeText, 16, 12);
                    TextSetUtil.setTextForMoey(billJson.getmBillJsonList().get(i).getPay(), payText, 16, 12);
                    TextSetUtil.setTextForMoey(billJson.getmBillJsonList().get(i).getBalance(), balanceText, 16, 12);
                    return;
                }
            }
        }

//        TextSetUtil.setTextForMoey("0.00", incomeText, 16, 12);
//        TextSetUtil.setTextForMoey("0.00 ", payText, 16, 12);
//        TextSetUtil.setTextForMoey("0.00", balanceText, 16, 12);


    }

    @Override
    public void showErrorMsg(String msg) {
        if (LoginStatusUtil.getToken() == null) {
            if (mNameText != null) {
                mNameText.setText("未登录");
                mPortraitImage.setImageResource(R.drawable.im_portrait);
//                TextSetUtil.setTextForMoey("0.00", incomeText, 16, 12);
//                TextSetUtil.setTextForMoey("0.00 ", payText, 16, 12);
//                TextSetUtil.setTextForMoey("0.00", balanceText, 16, 12);
            }

        }

    }


    private static class CustomShareListener implements UMShareListener {
        private WeakReference<FragmentActivity> mActivity;

        private CustomShareListener(FragmentActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                    Toast.makeText(mActivity.get(), " 分享成功啦", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mActivity.get(), " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity.get(), " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
