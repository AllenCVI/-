package com.kuxuan.moneynote.ui.activitys.opinion;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.base.mvpbase.MVPFragmentActivity;
import com.kuxuan.moneynote.utils.DrawableUtil;
import com.kuxuan.moneynote.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class OptionActivity extends MVPFragmentActivity<OptionPresenter, OptionModel> implements OptionContract.OptionView {
    @Bind(R.id.action_edittext)
    EditText action_edittext;

    @Bind(R.id.count_tv)
    TextView count_tv;
    int count  = 100;

    @Bind(R.id.action_button)
    Button action_button;
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

    }

    @Override
    public void hideProgress() {

    }

    public static void show(Context context){
        Intent intent = new Intent(context,OptionActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        //设置标题
        getTitleView(1).setTitle(getResources().getString(R.string.me_opinion)).
                setTitleColor(this,R.color.white).
                setLeftImage(R.drawable.toolbar_navigation_icon_normal, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        buttonListener();

    }

    @Override
    public int getLayout() {
        return R.layout.activity_option;
    }

    public void buttonListener(){
        //监听字数变化的操作
        action_button.setEnabled(false);
        action_button.setBackgroundResource(R.drawable.button_alpha_bu);
        action_edittext.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    action_button.setEnabled(true);
                    action_button.setBackground(DrawableUtil.getShape(OptionActivity.this));
                }else{
                    action_button.setEnabled(false);
                    action_button.setBackgroundResource(R.drawable.button_alpha_bu);
                }
                int number = count - s.length();
                count_tv.setText("还可以输入"+number+"字");
                selectionStart = action_edittext.getSelectionStart();
                selectionEnd = action_edittext.getSelectionEnd();
                //删除多余输入的字（不会显示出来）
                if (temp.length() > count) {
                    s.delete(selectionStart - 1, selectionEnd);
                    action_edittext.setText(s);
                }
                //设置光标在最后
                action_edittext.setSelection(s.length());
            }
        });
    }
    /**
     * button点击事件
     */
    @OnClick(R.id.action_button)
    void onAction(){
        String action = action_edittext.getText().toString();
        mPresenter.sendMessage(action);
        finish();

    }

    @Override
    public void sendSuccess(String message) {
        ToastUtil.show(this,message);
        finish();
    }
}
