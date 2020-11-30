package com.kuxuan.moneynote.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseActivity;
import com.kuxuan.moneynote.utils.GlideRoundTransform;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;


public class AboutActivity extends BaseActivity {

    @Bind(R.id.icon)
    ImageView imageView;



    @Override
    public int getLayout() {
        return R.layout.activity_about;
    }

    public static void show(Context context){
        Intent intent = new Intent(context,AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    private void initView(){
        //设置标题
        getTitleView(1).setTitle(getResources().getString(R.string.me_logo)).
                setTitleColor(this,R.color.white).
                setLeftImage(R.drawable.toolbar_navigation_icon_normal, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        TextView textView = (TextView) findViewById(R.id.version);
        String versionName = getVersionName(this);
        textView.setText(versionName);
        Glide.with(this).load(R.mipmap.icon_logo).transform(new GlideRoundTransform(this, 10)).into(imageView);
    }

    /**
     * get App versionName
     * @param context
     * @return
     */
    public String getVersionName(Context context){
        PackageManager packageManager=context.getPackageManager();
        PackageInfo packageInfo;
        String versionName="";
        try {
            packageInfo=packageManager.getPackageInfo(context.getPackageName(),0);
            versionName=packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }



}
