package com.kuxuan.moneynote.ui.fragments.details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.classic.common.MultipleStatusView;
import com.kuxuan.moneynote.MyApplication;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseFragmentActivity;
import com.kuxuan.moneynote.base.mvpbase.MVPFragment;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.TimeJson;
import com.kuxuan.moneynote.json.TypeDataJson;
import com.kuxuan.moneynote.json.netbody.RES;
import com.kuxuan.moneynote.json.netbody.SkinBean;
import com.kuxuan.moneynote.ui.activitys.SearchActivity;
import com.kuxuan.moneynote.ui.activitys.budget.BudgetActivity;
import com.kuxuan.moneynote.ui.activitys.edit.EditBillActivity;
import com.kuxuan.moneynote.ui.activitys.eventbus.BugetEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.LoginEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.LoginOutEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.NetworkEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.RefreshEvent;
import com.kuxuan.moneynote.ui.activitys.eventbus.SkinEvent;
import com.kuxuan.moneynote.ui.activitys.login.LoginActivity;
import com.kuxuan.moneynote.ui.activitys.login.PhoneLoginActivity;
import com.kuxuan.moneynote.ui.adapter.SkinAdapter;
import com.kuxuan.moneynote.ui.weight.ScoreDialog;
import com.kuxuan.moneynote.utils.DisplayUtil;
import com.kuxuan.moneynote.utils.GlideUtil;
import com.kuxuan.moneynote.utils.ImageUtil;
import com.kuxuan.moneynote.utils.JavaFormatUtils;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.NetWorkUtil;
import com.kuxuan.moneynote.utils.SPUtil;
import com.kuxuan.moneynote.utils.SkinUtil;
import com.kuxuan.moneynote.utils.TextSetUtil;
import com.kuxuan.moneynote.utils.TimeUtlis;
import com.kuxuan.moneynote.utils.UIHelper;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 首页-明细
 * Created by xieshengqi on 2017/10/19.
 */

public class DetialFragment extends MVPFragment<DetialPresent, DetialModel> implements DetialContract.DetialView {

    @Bind(R.id.fragment_detial_year_text)
    TextView fragmentDetialYear;
    @Bind(R.id.fragment_detial_time_layout)
    LinearLayout time_layout;
    @Bind(R.id.fragment_detial_month)
    TextView fragmentDetialMonth;
    @Bind(R.id.fragment_detial_moneyin)
    TextView fragmentDetialMoneyin;
    @Bind(R.id.fragment_detial_moneyout)
    TextView fragmentDetialMoneyout;
    @Bind(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;
    @Bind(R.id.fragment_detial_recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;
    BaseFragmentActivity activity;
    @Bind(R.id.empty_view)
    LinearLayout noData_layout;

    @Bind(R.id.relativelayout)
    RelativeLayout relativeLayout;

    //无网络和无登录时候的布局
    @Bind(R.id.layout_nologinornet_layout)
    LinearLayout nologin_layout;
    @Bind(R.id.nologinornet_textView)
    TextView nol_text;
    private static final int CHOOSE_PICTURE = 0;
    @Bind(R.id.score_imageview)
    ImageView score_img;
    @Bind(R.id.fragment_detial_allMoney)
    TextView allMoney_text;
    @Bind(R.id.fragment_detial_type_text)
    TextView type_text;
    String fileheader;
    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        handler = new Handler();
        fileheader = MyApplication.getInstance().getApplicationContext().getFilesDir().getPath()+"/"+"skin/";
        int position = (int) SPUtil.get(getContext(), Constant.Skin.CHECKED, -1);

        if (position > -1) {

            if(position==1000){

                String fileURl = (String) SPUtil.get(getContext(),Constant.Skin.CUTPIC,"");
                if(!fileURl.equals("")){
                    GlideUtil.setImageWithNoCache(getContext(),new File(fileURl),iv_banner);
                }

            }else {

                File file = new File(fileheader + Constant.Skin.HOMEPICNAME + position + ".png");
                GlideUtil.setImageWithNoCache(getContext(), file, iv_banner);
            }

        }


//        getTitleView(0).setTitle(getString(R.string.app_name));
//        getTitleView(0).setTitle("钱多随手记账");
        activity = (BaseFragmentActivity) getActivity();
        mPresenter.initRecyclerView(getActivity(), mRecyclerView);
        mPresenter.initRefreshLayout(getActivity(), refreshLayout);
        mPresenter.initListener(fragmentDetialYear);
        mPresenter.initListener(fragmentDetialMonth);
        mPresenter.InitPopWindow();
        mPresenter.initListener(time_layout);
        initMiluView();
        checkNet();
        nologin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nol_text.getText().toString().equals(getResources().getString(R.string.nologin))) {
                    //登录
                    Bundle bundle = new Bundle();
                    bundle.putInt(PhoneLoginActivity.GOTYPE, 3);
                    UIHelper.openActivityWithBundle(activity, LoginActivity.class, bundle);
                } else {
                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                }
            }
        });
        score_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2018/4/23 签到领积分
                showScoreDialog();
            }
        });
        Glide.with(this).load(R.drawable.score).into(score_img);
        initTime();
        type_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isOpen = (boolean) SPUtil.get(getActivity(), Constant.System.SWITCH, false);
                if(isOpen){
                    if(LoginStatusUtil.isLoginin()){
                    Intent intent = new Intent(getActivity(), BudgetActivity.class);
                    startActivity(intent);
                    }else {
                        UIHelper.openActivity(getActivity(), LoginActivity.class);
                    }
                }
            }
        });
    }

    private void checkNet() {
        synchronized (DetialFragment.class) {
            boolean networkAvailable = NetWorkUtil.isNetworkAvailable(getActivity());
            if (networkAvailable)
                mPresenter.loginCheck();
            else {
                showNoNetWork();
                getDataFirst();
            }
        }
    }

    //签到成功的窗口
    private void showScoreDialog() {
        ScoreDialog dialog = new ScoreDialog(getActivity());
        dialog.show();

    }

    /**
     * 初始化多布局
     */
    private void initMiluView() {
        multipleStatusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.error_retry_view:
                        //重新加载
                        multipleStatusView.showLoading();
                        mPresenter.getDataLists(mPresenter.getCurrentYear() + "", mPresenter.getCurrentMonth() + "");
                        break;
                    case R.id.no_network_retry_view:
                        //检测网络
                        checkNet();
                        break;
                }
            }
        });
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_detial;
    }

    @Override
    public void showProgress() {
        activity.showProgressDialog(getResources().getString(R.string.loadding));
    }

    @Override
    public void hideProgress() {
        activity.closeProgressDialog();
    }

    @Override
    public void showNoLogin() {
        //新逻辑（没登录显示离线没有用户的记账信息）
        nologin_layout.setVisibility(View.VISIBLE);
        nol_text.setText(getResources().getString(R.string.nologin));
    }

//    /**
//     * 获取没有登录的用户账单
//     */
//    @Override
//    public void getNoLoginData() {
//        TimeJson currentTime = TimeUtlis.getCurrentTime();
//        multipleStatusView.showLoading();
//        mPresenter.getDataLists(currentTime.getYear() + "", currentTime.getMonth() + "");
//    }

    @Override
    public void showLogin() {
        nologin_layout.setVisibility(View.GONE);
    }

    @Override
    public void showNoNetWork() {
        nologin_layout.setVisibility(View.VISIBLE);
        nol_text.setText(getResources().getString(R.string.nonetwork));
    }

    @Override
    public void showNoData() {
        noData_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showHaveData() {
        noData_layout.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        multipleStatusView.showError();
    }

    @Override
    public void getDataFirst() {
        TimeJson currentTime = TimeUtlis.getCurrentTime();
        multipleStatusView.showLoading();
        mPresenter.getDataLists(currentTime.getYear() + "", currentTime.getMonth() + "");
    }

    @Override
    public void showYearAndMonth(String year, String month) {
        fragmentDetialYear.setText(year + "");
        TextSetUtil.setTextForMonth(month, fragmentDetialMonth);
    }

    @Override
    public void setScrollForNodata() {
//        mRecyclerView.requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public void setScrollData() {
//        mRecyclerView.requestDisallowInterceptTouchEvent(false);
    }

    @Override
    public void showContentLayout() {
        multipleStatusView.showContent();
    }

    @Override
    public void finishRefresh() {
        refreshLayout.finishLoadmore();
        refreshLayout.finishRefreshing();
    }

    double currentInmoney, currentOutMoney;

    @Override
    public void setData(String year, String month, String inmoney, String outMoey) {
        fragmentDetialYear.setText(year + "");
        int mon = Integer.parseInt(month);
        if (mon < 10) {
            TextSetUtil.setTextForMonth("0" + mon, fragmentDetialMonth);
        } else {
            TextSetUtil.setTextForMonth(mon + "", fragmentDetialMonth);
        }
        currentInmoney = Double.parseDouble(inmoney);
        currentOutMoney = Double.parseDouble(outMoey);
        TextSetUtil.setTextForMoey(JavaFormatUtils.formatFloatNumber(currentInmoney), fragmentDetialMoneyin);
        TextSetUtil.setTextForMoey(JavaFormatUtils.formatFloatNumber(currentOutMoney), fragmentDetialMoneyout);
            changeTypeData(currentInmoney, currentOutMoney);
    }

    private void changeTypeData(Double inMoney, Double outMoney) {
        boolean isOpen = (boolean) SPUtil.get(getActivity(), Constant.System.SWITCH, false);
        long date = (Long) SPUtil.get(getActivity(), Constant.System.BUGET_NUM, 0l);

        if (isOpen) {
            Drawable drawableRight = getResources().getDrawable(
                    R.mipmap.icon_edit);
            type_text.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, drawableRight, null);
            type_text.setCompoundDrawablePadding(4);
            type_text.setText("预算结余");
            TextSetUtil.setTextForBigSize(JavaFormatUtils.formatFloatNumber(date - outMoney), allMoney_text);
        } else {
            type_text.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, null, null);
            type_text.setCompoundDrawablePadding(4);
            type_text.setText("本月结余");
            TextSetUtil.setTextForBigSize(JavaFormatUtils.formatFloatNumber(inMoney - outMoney), allMoney_text);

        }
    }

    private void initTime() {
        TimeJson currentTime = TimeUtlis.getCurrentTime();
        fragmentDetialYear.setText(currentTime.getYear() + "");
        TextSetUtil.setTextForMonth(currentTime.getMonth() + "", fragmentDetialMonth);
        currentInmoney = 0.00;
        currentOutMoney = 0.00;
        TextSetUtil.setTextForMoey("0.00", fragmentDetialMoneyin);
        TextSetUtil.setTextForMoey("0.00", fragmentDetialMoneyout);
        changeTypeData(currentInmoney, currentOutMoney);
    }


    @Override
    public void goToEdit(TypeDataJson dayDataEntity) {
        Intent intent = new Intent(getActivity(), EditBillActivity.class);
        intent.putExtra(EditBillActivity.DATA, dayDataEntity);
        startActivity(intent);
    }


    SkinBean skinBean;

    @Override
    public void showSkinData(SkinBean skinBean) {


        this.skinBean = skinBean;

    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshEvent event) {
        mPresenter.getDataLists(mPresenter.getCurrentYear() + "", mPresenter.getCurrentMonth() + "");
//      mPresenter.loginCheck();
    }

    /**
     * 登录之后刷新数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginEvent event) {

        checkNet();
    }

    BaseQuickAdapter adapter;



    @OnClick(R.id.iv_skinicon)
    public void click() {

        showPopWindow();

    }


    int j = 0;

    @OnClick(R.id.iv_search)
    public void searchClick() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }

    PopupWindow popupWindow;
    Handler handler;




    @Override
    public void showDownLoad(RES res, int size, int position, final View background, final ProgressBar progressBar, final int current) {

        if (size < 2) {
            size++;
            if (size == 1) {
                mPresenter.downLoadPic(res, background, progressBar, res.getMine_banner(), size, position);
            } else if (size == 2) {
                mPresenter.downLoadPic(res, background, progressBar, res.getBanner(), size, position);
            }
        }
        if (size == 2) {
            progressBar.setProgress(current);
            if (current == 100) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        background.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }
                });

                SkinUtil.markDownload(position);
            }
        }

    }

    /**
     * 裁剪图片方法实现
     */
    protected void startPhotoZoom(String dpath) {

        UCrop.Options options = new UCrop.Options();
        // 设置图片处理的格式JPEG
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        // 设置压缩后的图片精度
        options.setCompressionQuality(96);

        // 得到头像的缓存地址
        File dPath = MyApplication.getPortraitTmpFile();

        // 发起剪切
        UCrop.of(Uri.fromFile(new File(dpath)), Uri.fromFile(dPath))
                .withAspectRatio(8, 5)
                // 返回最大的尺寸
                .withMaxResultSize(1000, 625)
                // 相关参数
                .withOptions(options)
                .start(getActivity());
    }


    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Uri uri) {
        String mPortraitPath;
        mPortraitPath = uri.getPath();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap img = BitmapFactory.decodeFile(mPortraitPath, options);
        uploadPic(img);
    }

    private void uploadPic(Bitmap bitmap) {
        String imageFile = ImageUtil.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));

        GlideUtil.setImageWithNoCache(getContext(),new File(imageFile),iv_banner);
        SPUtil.putAndApply(getContext(),Constant.Skin.CHECKED,1000);
//        String mVideoPath = fileheader + Constant.Skin.CUTPIC+1000+".png";

        SPUtil.putAndApply(getContext(),Constant.Skin.CUTPIC,imageFile);
        SkinEvent skinEvent = new SkinEvent(1000);
        EventBus.getDefault().post(skinEvent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // 如果返回码是可以用的
            switch (requestCode) {
                case CHOOSE_PICTURE:
                    // 开始对图片进行裁剪处理
                    String url = ImageUtil.getPath(getContext(), data.getData());
                    startPhotoZoom(url);

                    break;
                case UCrop.REQUEST_CROP:
                    if (data != null) {
                        final Uri resultUri = UCrop.getOutput(data);

                        System.out.println("图片url" + resultUri);

                        if (resultUri != null) {
                            setImageToView(resultUri);
                        }
                        // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
                case UCrop.RESULT_ERROR:
                    break;
                default:
                    break;
            }
        }


    }
    ImageView iv_add;
    private void showPopWindow() {


        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.homeskin_popwindow, null, false);

        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(138), true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);

        setBackgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(1.0f);
            }
        });
        popupWindow.showAtLocation(relativeLayout, Gravity.BOTTOM, 0, 0);

        final RecyclerView recyclerView = contentView.findViewById(R.id.recyclerview);

        iv_add = contentView.findViewById(R.id.iv_add);

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAlbumIntent = new Intent(
                        Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setType("image/*");
                startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SkinAdapter(R.layout.skinadapter_item);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProgressBar progressBar = view.findViewById(R.id.progressbar);
                if(progressBar.getVisibility() == View.VISIBLE){
                     return;
                }

                ImageView iv_check = view.findViewById(R.id.iv_check);
                if (iv_check.getVisibility() == View.VISIBLE) {
                    return;
                }

                View background = view.findViewById(R.id.view);

                if (background.getVisibility() == View.VISIBLE && progressBar.getVisibility() == View.GONE) {
                    progressBar.setMax(100);
                    progressBar.setProgress(0);
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    RES res = (RES) adapter.getData().get(position);

                    mPresenter.downLoadPic(res, background, progressBar, res.getPreview_img(), 0, position);



                } else {
                    SPUtil.putAndApply(getContext(), Constant.Skin.CHECKED, position);
                    adapter.notifyDataSetChanged();
                    SkinBean mskinBean = new SkinBean();
                    mskinBean.setCode(position);
                    RES item = (RES) adapter.getItem(position);
                    String color = item.getColor();
                    //选中的时候存储颜色
//                    SPUtil.putAndApply(getContext(), Constant.Skin.COLOR_SELECT, "#FF4081" );
                    String color1 = item.getColor();
                    color1 = color1.substring(2, 8);
                    Log.e("字符数", color1.length() + "");
                    SPUtil.putAndApply(MyApplication.getInstance(), Constant.Skin.COLOR_SELECT, color1);

                    EventBus.getDefault().post(new SkinEvent(mskinBean.getCode()));
                    setBanner(position);
                }
            }
        });

        setSkinData();

        ImageView ivpop_close = contentView.findViewById(R.id.ivpop_close);

        ivpop_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        ivpop_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        mPresenter.InitPopWindow();


    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }


    private void setSkinData() {

        if (skinBean != null)
            adapter.setNewData(skinBean.getRes());
    }


    @Bind(R.id.iv_banner)
    ImageView iv_banner;

    private void setBanner(int position) {

        File file = new File(fileheader + Constant.Skin.HOMEPICNAME + position + ".png");
        GlideUtil.setImageWithNoCache(getContext(), file, iv_banner);

    }

    /**
     * 登出刷新数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginOutEvent loginOutEvent) {

        checkNet();

    }

    /**
     * 网络监听
     *
     * @param networkEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NetworkEvent networkEvent) {
        checkNet();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent2(BugetEvent bugetEvent) {
        changeTypeData(currentInmoney, currentOutMoney);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
