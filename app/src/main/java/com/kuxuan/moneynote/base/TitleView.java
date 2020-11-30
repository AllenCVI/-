package com.kuxuan.moneynote.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.utils.DrawableUtil;


/**
 * Created by xieshengqi on 17/2/23.
 */

public class TitleView {

    private View headerView;


    private ImageView left_image, right_image,edit_image;
    private TextView left_text, right_text, title_text,edit_text,head_text;
    private RelativeLayout head, titleLayout,editLayout;
    private EditText searchText;
    private TextView edit_back;

    private Context context;

    public TitleView(Context context, View view,int what) {
        this.headerView = view;
        switch (what){
            //观赏型小titleView仅展示一行文字的titleView
            case 0:
                initSimpleTitleView();
                break;
            //功能型小titleView
            case 1:
                initComplexTitleView();
                break;
            //功能型大TitleView
            case 2:
                initBigTitleView();
                break;
        }
        head = (RelativeLayout) headerView.findViewById(R.id.header);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&Build.VERSION.SDK_INT<Build.VERSION_CODES.N) {
//            int barHeight = StatusBarUtil.getStatusBarHeight(context);
//            head.setPadding(0, barHeight, 0, 0);
//        }
        //设置皮肤颜色
        head.setBackgroundColor(DrawableUtil.getSkinColor(context));

    }

    private void initBigTitleView() {
        edit_image = headerView.findViewById(R.id.header_edit_image);
        edit_text = headerView.findViewById(R.id.header_edit_name);
        editLayout = headerView.findViewById(R.id.header_edit_layout);
        edit_back = headerView.findViewById(R.id.header_edit_back);
    }

    private void initComplexTitleView() {
        right_image = (ImageView) headerView.findViewById(R.id.header_img_tight);
        left_image = (ImageView) headerView.findViewById(R.id.header_img_left);
        left_text = (TextView) headerView.findViewById(R.id.header_back);
        right_text = (TextView) headerView.findViewById(R.id.header_btn_right_txt);
        title_text = (TextView) headerView.findViewById(R.id.header_title_txt);
        head_text = headerView.findViewById(R.id.header_text);
        titleLayout = (RelativeLayout) headerView.findViewById(R.id.header_title_layout);
    }

    private void initSimpleTitleView() {
        editLayout = headerView.findViewById(R.id.header_edit_layout);
        title_text = (TextView) headerView.findViewById(R.id.header_title_txt);
    }






    public TitleView setLeftImage(int drawable, View.OnClickListener listener) {
        left_image.setVisibility(View.VISIBLE);
        left_text.setVisibility(View.GONE);
        left_image.setImageResource(drawable);
        if (listener != null) {
            left_image.setOnClickListener(listener);
        }
        return this;
    }

    public TitleView setRightText(String text) {
        head_text.setVisibility(View.VISIBLE);
        head_text.setText(text);
        return this;
    }

    public TitleView setRightText(String content,View.OnClickListener listener) {
        right_image.setVisibility(View.VISIBLE);
        head_text.setVisibility(View.VISIBLE);
        head_text.setText(content);
        if (listener != null) {
            head_text.setOnClickListener(listener);
        }
        return this;
    }



    public TitleView setLeftImage(int drawable) {
        left_image.setVisibility(View.VISIBLE);
        left_text.setVisibility(View.GONE);
        left_image.setImageResource(drawable);
        return this;
    }

    public TitleView setLeft_text(String content, View.OnClickListener listener) {
        left_image.setVisibility(View.GONE);
        left_text.setVisibility(View.VISIBLE);
        left_text.setText(content);
        if (listener != null) {
            left_text.setOnClickListener(listener);
        }
        return this;
    }

    /*
      后加的
     */
//    public TitleView setLeft_text(int what,String content, View.OnClickListener listener) {
//        left_text.setVisibility(View.VISIBLE);
//        left_text.setText(content);
//        if (listener != null) {
//            left_text.setOnClickListener(listener);
//        }
//        return this;
//    }



    public TitleView setTitle(String content) {
        title_text.setVisibility(View.VISIBLE);
        title_text.setText(content);
        return this;
    }
    public TitleView setTitleColor(Activity activity, int color) {
        title_text.setVisibility(View.VISIBLE);

        title_text.setTextColor(activity.getResources().getColor(color));
        return this;
    }
    public TitleView setRightImage(int drawable, View.OnClickListener listener) {
        right_image.setVisibility(View.VISIBLE);
        right_text.setVisibility(View.GONE);
        right_image.setImageResource(drawable);
        if (listener != null) {
            right_image.setOnClickListener(listener);
        }
        return this;
    }



    public RelativeLayout getHead(){
        return head;
    }

    public TitleView setRight_text(String content, View.OnClickListener listener) {
        right_image.setVisibility(View.GONE);
        right_text.setVisibility(View.VISIBLE);
        right_text.setText(content);
        if (listener != null) {
            right_text.setOnClickListener(listener);
        }
        return this;
    }



    //后加的
//    public TitleView setRight_text(int what,String content, View.OnClickListener listener) {
//        right_text.setVisibility(View.VISIBLE);
//        right_text.setText(content);
//        if (listener != null) {
//            right_text.setOnClickListener(listener);
//        }
//        return this;
//    }



    public TitleView setRight_text(String content) {
        right_image.setVisibility(View.GONE);
        right_text.setVisibility(View.VISIBLE);
        right_text.setText(content);
        return this;
    }


    //后加的
//    public TitleView setRight_text(int what,String content) {
//        right_text.setVisibility(View.VISIBLE);
//        right_text.setText(content);
//        return this;
//    }


    /**
     * 编辑页面用到
     */
    public void changeEditLayout(){
        editLayout.setVisibility(View.VISIBLE);
        titleLayout.setVisibility(View.GONE);
    }

    public ImageView getEdit_image() {
        return edit_image;
    }

    public RelativeLayout getEditLayout() {
        return editLayout;
    }

    public TextView getEdit_back() {
        return edit_back;
    }

    public TextView getEdit_text() {
        return edit_text;
    }

    public TitleView setLeftDrawableText(Drawable drawable, String text) {
        left_image.setVisibility(View.GONE);
        left_text.setVisibility(View.VISIBLE);
        left_text.setText(text);
/// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        left_text.setCompoundDrawablePadding(5);
        left_text.setCompoundDrawables(drawable, null, null, null);
        return this;
    }

    public TitleView setRightDrawableText(Drawable drawable, String text) {
        right_image.setVisibility(View.GONE);
        right_text.setVisibility(View.VISIBLE);
        right_text.setText(text);
/// 这一步必须要做,否则不会显示.
        right_text.setCompoundDrawablePadding(5);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        right_text.setCompoundDrawables(null, null, drawable, null);
        return this;
    }

    public TitleView setRightDrawableText(Drawable drawable, String text, View.OnClickListener clickListener) {
        right_image.setVisibility(View.GONE);
        right_text.setVisibility(View.VISIBLE);
        right_text.setText(text);
        right_text.setOnClickListener(clickListener);
/// 这一步必须要做,否则不会显示.
        right_text.setCompoundDrawablePadding(5);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        right_text.setCompoundDrawables(null, null, drawable, null);
        return this;
    }



    public EditText getSearchText() {
        return searchText;
    }

    public void setSearchText(EditText searchText) {
        this.searchText = searchText;
    }

    public View getHeaderView() {
        return headerView;
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
    }

    public ImageView getLeft_image() {
        return left_image;
    }

    public void setLeft_image(ImageView left_image) {
        this.left_image = left_image;
    }

    public ImageView getRight_image() {
        return right_image;
    }

    public void setRight_image(ImageView right_image) {
        this.right_image = right_image;
    }

    public TextView getLeft_text() {
        return left_text;
    }

    public void setLeft_text(TextView left_text) {
        this.left_text = left_text;
    }

    public TextView getRight_text() {
        return right_text;
    }

    public void setRight_text(TextView right_text) {
        this.right_text = right_text;
    }

    public TextView getTitle_text() {
        return title_text;
    }

    public void setTitle_text(TextView title_text) {
        this.title_text = title_text;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
