package com.kuxuan.moneynote.ui.activitys.person;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kuxuan.moneynote.MyApplication;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.base.mvpbase.MVPFragmentActivity;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.HeadImg;
import com.kuxuan.moneynote.json.MineJson;
import com.kuxuan.moneynote.json.netbody.PersonBody;
import com.kuxuan.moneynote.json.netbody.WeChatJson;
import com.kuxuan.moneynote.listener.UpDBListener;
import com.kuxuan.moneynote.servier.ServiceUtil;
import com.kuxuan.moneynote.ui.activitys.UpdatePassword.UpdateActivity;
import com.kuxuan.moneynote.ui.activitys.bindphone.BindThirdActivity;
import com.kuxuan.moneynote.ui.activitys.eventbus.LoginOutEvent;
import com.kuxuan.moneynote.ui.weight.DialogGetHeadPicture;
import com.kuxuan.moneynote.utils.ImageUtil;
import com.kuxuan.moneynote.utils.LoginStatusUtil;
import com.kuxuan.moneynote.utils.NetWorkUtil;
import com.kuxuan.moneynote.utils.ToastUtil;
import com.kuxuan.moneynote.utils.UIHelper;
import com.kuxuan.moneynote.utils.UpDBDataOpertor;
import com.kuxuan.sqlite.db.CategoryDB;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class PersonActivity extends MVPFragmentActivity<PersonPresenter, PersonModel> implements PersonContract.PersonView {
    @Bind(R.id.name_text)
    TextView mNameText;

    @Bind(R.id.im_portrait)
    CircleImageView mPortrait;

    @Bind(R.id.id_number_text)
    TextView mIdNumber;

    @Bind(R.id.gender_text)
    TextView mGender;

    @Bind(R.id.phonenumber_text)
    TextView mPhoneNumber;

    @Bind(R.id.wechat_text)
    TextView mWeChat;

    @Bind(R.id.phone_lin)
    LinearLayout mPhoneLin;

    @Bind(R.id.rightphoneimg)
    ImageView mPhoneImg;

    @Bind(R.id.rightwechatimg)
    ImageView mWeChaImg;

    @Bind(R.id.update_lin)
    LinearLayout mUpdateLin;

    @Bind(R.id.view_line)
    View view;

    private static final String GENDER_MAN = "man";
    private static final String GENDER_WOMEN = "woman";
    private static final String MAN = "男";
    private static final int CHOOSE_PICTURE = 0;
    private static final String NULL = "";
    private static final int TWO = 2;
    private String mobile;


    @Override
    public void showProgress() {
        showProgressDialog(getResources().getString(R.string.deleteing));
    }

    @Override
    public void hideProgress() {
        closeProgressDialog();
    }


    @Override
    public void initView() {
        //设置标题
        getTitleView(1).setTitle(getResources().getString(R.string.person)).
                setTitleColor(this, R.color.white).
                setLeftImage(R.drawable.toolbar_navigation_icon_normal, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        MineJson userInfo = LoginStatusUtil.getUserInfo();
        if (userInfo == null) {
            mPresenter.getMineData();
        } else {
            setMineData(userInfo);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_person;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }


    //头像点击事件
    @OnClick(R.id.icon_lin)
    void iconClick() {
        Intent openAlbumIntent = new Intent(
                Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
    }

    @OnClick(R.id.name_lin)
    void nameClick() {
        createDialog();
    }

    @OnClick(R.id.phone_lin)
    void phoneClick() {
        UIHelper.openActivity(this, BindThirdActivity.class);
    }

    @OnClick(R.id.sex_lin)
    void sexClick() {
        DialogGetHeadPicture dialogGetHeadPicture = new DialogGetHeadPicture(this) {
            @Override
            public void firstText() {
                if (NetWorkUtil.isNetworkAvailable(PersonActivity.this)) {
                    PersonBody personBody = new PersonBody(mNameText.getText().toString(), GENDER_MAN);
                    mPresenter.updatePersonData(personBody);
                    mGender.setText("男");
                } else {
                    ToastUtil.show(PersonActivity.this, getResources().getString(R.string.nonetwork));
                }
            }

            @Override
            public void secondText() {
                if (NetWorkUtil.isNetworkAvailable(PersonActivity.this)) {
                    PersonBody personBody = new PersonBody(mNameText.getText().toString(), GENDER_WOMEN);
                    mPresenter.updatePersonData(personBody);
                    mGender.setText("女");
                } else {
                    ToastUtil.show(PersonActivity.this, getResources().getString(R.string.nonetwork));
                }
            }

            @Override
            public void setFirstText(TextView textView) {
                textView.setText("男");
            }

            @Override
            public void setSecondText(TextView textView) {
                textView.setText("女");
            }
        };

        dialogGetHeadPicture.show();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }


    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public static void show(Context context) {
        Intent intent = new Intent(context, PersonActivity.class);
        context.startActivity(intent);
    }

    private void createDialog() {
        TextView title = new TextView(this);
        title.setText("昵称");
        title.setTextSize(20);
        title.setPadding(0, 20, 0, 20);
        title.setTextColor(ContextCompat.getColor(this, R.color.first_text));
        title.setGravity(Gravity.CENTER);
        final EditText editText = new EditText(this);
        editText.setHint("请输入2~8位昵称");
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        editText.setTextSize(14);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCustomTitle(title)
                .setView(editText)
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editText.getText().toString();
                if (s.length() < TWO) {
                    Toast.makeText(PersonActivity.this, "昵称长度过短", Toast.LENGTH_SHORT).show();
                    return;
                }
                PersonBody personBody;
                if (MAN.equals(mGender.getText().toString())) {
                    personBody = new PersonBody(s, "man");
                } else {
                    personBody = new PersonBody(s, "woman");
                }
                if (NetWorkUtil.isNetworkAvailable(PersonActivity.this)) {
                    mPresenter.updatePersonData(personBody);
                    mNameText.setText(s);
                } else {
                    ToastUtil.show(PersonActivity.this, getResources().getString(R.string.nonetwork));
                }
                dialog.dismiss();
            }
        });


    }


    /**
     * 数据同步
     */
    private void updata() {
        showProgressDialog("退出中...");
        final CategoryDaoOperator categoryDaoOperator = CategoryDaoOperator.newInstance();
        if (LoginStatusUtil.getLoginUserId() != -1) {
            UpDBDataOpertor.getInstance().onUpData(new UpDBListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess() {
                    closeProgressDialog();
                    LoginStatusUtil.loginOut();
                    EventBus.getDefault().post(new LoginOutEvent());
                    finish();
                }

                @Override
                public void onFail() {

                }

                @Override
                public void onexitLoginFail(ArrayList<CategoryDB> needUpdataJson) {
                    for (CategoryDB categoryDB : needUpdataJson) {
                        if (categoryDB.getStatus() == 1) {
                            categoryDaoOperator.updataIsDelete(categoryDB.getBill_id(), true);
                        } else {
                            categoryDaoOperator.updataNeedSynce(categoryDB.getBill_id());
                        }
                    }
                    closeProgressDialog();
                    LoginStatusUtil.loginOut();
                    EventBus.getDefault().post(new LoginOutEvent());
                    finish();
                }

                @Override
                public void onNoNeedUpdata() {
                    closeProgressDialog();
                    LoginStatusUtil.loginOut();
                    EventBus.getDefault().post(new LoginOutEvent());
                    finish();
                }
            });
//            final ArrayList<CategoryDB> needUpdataJson = categoryDaoOperator.getNeedUpdataJson();
//            if (needUpdataJson != null && needUpdataJson.size() != 0) {
//                String json = UpDataOperator.getInstance().getJson(needUpdataJson);
//                RetrofitClient.getApiService().upData(new UpDataBody(0, json)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
//                    @Override
//                    public void onError(ExceptionHandle.ResponeThrowable e) {
//                        for (CategoryDB categoryDB : needUpdataJson) {
//                            if (categoryDB.getStatus() == 1) {
//                                categoryDaoOperator.updataIsDelete(categoryDB.getBill_id(), true);
//                            } else {
//                                categoryDaoOperator.updataNeedSynce(categoryDB.getBill_id());
//                            }
//                        }
//                        closeProgressDialog();
//                        LoginStatusUtil.loginOut();
//                        EventBus.getDefault().post(new LoginOutEvent());
//                        finish();
//                    }
//
//                    @Override
//                    public void onSuccess(BaseJson<Object> objectBeanNewJson) {
//                        closeProgressDialog();
//                        //更新数据库
//                        if (objectBeanNewJson != null && objectBeanNewJson.getCode() == 0) {
//                            for (CategoryDB categoryDB : needUpdataJson) {
//                                if (categoryDB.getStatus() == 1) {
//                                    categoryDaoOperator.deleteData(categoryDB.getBill_id());
//                                } else {
//                                    categoryDaoOperator.updataNoNeedSynce(categoryDB.getBill_id());
//                                }
//                            }
//                        } else {
//                            for (CategoryDB categoryDB : needUpdataJson) {
//                                categoryDaoOperator.updataNeedSynce(categoryDB.getBill_id());
//                            }
//                        }
//                        LoginStatusUtil.loginOut();
//                        EventBus.getDefault().post(new LoginOutEvent());
//                        finish();
//                    }
//                });
//            } else {
//                LoginStatusUtil.loginOut();
//                EventBus.getDefault().post(new LoginOutEvent());
//                finish();
//            }
        } else {
            closeProgressDialog();
            LoginStatusUtil.loginOut();
            EventBus.getDefault().post(new LoginOutEvent());
            finish();
        }

    }

    @OnClick(R.id.exit_btn)
    void exitBtn() {

        //最后上传一遍数据
        updata();
        ServiceUtil.stopUpData(this);
        //TODO 刷新数据（重写）
//        EventBus.getDefault().post(new RefreshEvent());

//        showProgress();
//        RetrofitClient.getApiService().logOut().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
//            @Override
//            public void onError(ExceptionHandle.ResponeThrowable e) {
//                ToastUtil.show(PersonActivity.this,e.message);
//                hideProgress();
//            }
//
//            @Override
//            public void onSuccess(BaseJson<Object> objectBaseJson) {
//                if (objectBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
//                    hideProgress();
//                    LoginStatusUtil.loginOut();
//                    EventBus.getDefault().post(new LoginEvent());
//                    finish();
//                } else {
//                    hideProgress();
//                }
//
//            }
//        });
    }

    @Override
    public void setMineData(MineJson mineModel) {

        mIdNumber.setText(mineModel.getId());
        if (NULL.equals(mineModel.getUsername())) {
            mNameText.setText(mineModel.getNickname());
        } else {
            mNameText.setText(mineModel.getUsername());
        }

        if (GENDER_WOMEN.equals(mineModel.getGender())) {
            mGender.setText("女");
        } else {
            mGender.setText("男");
        }
        if (NULL.equals(mineModel.getMobile())) {
            mPhoneNumber.setText("未绑定");
            mPhoneImg.setVisibility(View.VISIBLE);
            mPhoneNumber.setTextColor(ContextCompat.getColor(this, R.color.white));
            mUpdateLin.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        } else {
            mPhoneNumber.setText(mineModel.getMobile());
            mPhoneLin.setClickable(false);
            mPhoneNumber.setTextColor(ContextCompat.getColor(this, R.color.textSecond));
            mPhoneImg.setVisibility(View.GONE);
            mUpdateLin.setVisibility(View.VISIBLE);

            mobile = mineModel.getMobile();
        }

        if (mineModel.getNickname() == null) {
            mWeChat.setText("未绑定");
            mWeChat.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            mWeChat.setText(mineModel.getNickname());
            mWeChat.setClickable(false);
            mWeChaImg.setVisibility(View.GONE);
        }

        if (mineModel.getAvatar() == null && mineModel.getHeadimgurl() == null) {
            Glide.with(this).load(R.drawable.im_portrait)
                    .into(mPortrait);
        } else {
            if (mineModel.getAvatar() == null) {
                Glide.with(this).load(mineModel.getHeadimgurl())
                        .into(mPortrait);
            } else {
                Glide.with(this).load(mineModel.getAvatar())
                        .into(mPortrait);
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent2(LoginOutEvent loginOutEvent) {
        finish();
    }


    String headImgUrl;

    @Override
    public void setMineHeadImg() {
        HeadImg headImg = new HeadImg(headImgUrl);
        EventBus.getDefault().post(headImg);
    }

    @OnClick(R.id.wechat_lin)
    void weChatClick() {
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

    @OnClick(R.id.update_lin)
    void updateClick() {
        UpdateActivity.show(this, mobile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 如果返回码是可以用的
            switch (requestCode) {
                case CHOOSE_PICTURE:
                    // 开始对图片进行裁剪处理
                    String url = ImageUtil.getPath(PersonActivity.this, data.getData());
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


    /**
     * 裁剪图片方法实现
     */
    protected void startPhotoZoom(String dpath) {

        UCrop.Options options = new UCrop.Options();
        // 设置图片处理的格式JPEG
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        // 设置压缩后的图片精度
        options.setCompressionQuality(96);

        // 得到头像的缓存地址
        File dPath = MyApplication.getPortraitTmpFile();

        // 发起剪切
        UCrop.of(Uri.fromFile(new File(dpath)), Uri.fromFile(dPath))
                // 1比1比例
                .withAspectRatio(1, 1)
                // 返回最大的尺寸
                .withMaxResultSize(520, 520)
                // 相关参数
                .withOptions(options)
                .start(this);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Uri uri) {
        String mPortraitPath;
        // 得到头像地址
        mPortraitPath = uri.getPath();

        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 2;

        Bitmap img = BitmapFactory.decodeFile(mPortraitPath, options);
        uploadPic(img);
    }

    private void uploadPic(Bitmap bitmap) {
        String imageFile = ImageUtil.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        if (imageFile != null) {
            if (NetWorkUtil.isNetworkAvailable(PersonActivity.this)) {

                PersonBody personBody = new PersonBody(imageFile);
                mPresenter.updateAvatar(personBody);
                headImgUrl = personBody.getAvatar();
            } else {
                ToastUtil.show(PersonActivity.this, getResources().getString(R.string.nonetwork));
            }
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            String openid = data.get("openid");
            String unionid = data.get("uid");
            String gender = data.get("gender");
            String iconurl = data.get("iconurl");
            final String name = data.get("name");

            String sex;
            if (MAN.equals(gender)) {
                sex = "1";
            } else {
                sex = "2";
            }
            RetrofitClient.getApiService().bindWeChat(new WeChatJson(openid, name, sex, iconurl, unionid)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<Object>>() {
                @Override
                public void onError(ExceptionHandle.ResponeThrowable e) {
                    ToastUtil.show(PersonActivity.this, e.message);
                }

                @Override
                public void onSuccess(BaseJson<Object> objectBaseJson) {
                    if (objectBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {
                        ToastUtil.show(PersonActivity.this, getResources().getString(R.string.bindwechat));
                        mWeChat.setText(name);
                        mWeChat.setTextColor(ContextCompat.getColor(PersonActivity.this, R.color.textSecond));
                    } else {
                        ToastUtil.show(PersonActivity.this, objectBaseJson.getError().get(0));
                    }

                }
            });
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), R.string.we_chat_fail, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), R.string.we_chat_cancel, Toast.LENGTH_SHORT).show();
        }
    };

}
