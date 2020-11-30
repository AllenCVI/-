package com.kuxuan.moneynote.ui.activitys;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.BaseActivity;
import com.kuxuan.moneynote.ui.weight.ProgressWebView;
import com.kuxuan.moneynote.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;

/**
 * Created by xieshengqi on 2017/4/8.
 */

public class WebviewActivity extends BaseActivity {
    @Bind(R.id.webview)
    ProgressWebView mWebview;

    public static final String URL = "url";
    public static final String TITLE = "title";

    @Override
    public int getLayout() {
        return R.layout.layout_webview;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getExtras().getString(URL);
        initWebview();
        getTitleView(1).setTitle(getIntent().getExtras().getString(TITLE));
        getTitleView(1).setLeft_text("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWebview.canGoBack()) {
                    mWebview.goBack();
                } else {
                    finish();
                }
            }
        });
        if (url == null) {
            ToastUtil.show(this, "无效链接");
            return;
        } else {
            mWebview.loadUrl(url);
        }
    }


    private void initWebview() {
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setUseWideViewPort(true);
        mWebview.setInitialScale(57);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //   支持弹窗式 显示 div
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebview.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebview.getSettings().setSupportZoom(false);
        mWebview.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebview.getSettings().setSupportMultipleWindows(true);
        mWebview.setWebViewClient(new WebViewClient());
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mWebview.canGoBack()) {
                mWebview.goBack();
                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
