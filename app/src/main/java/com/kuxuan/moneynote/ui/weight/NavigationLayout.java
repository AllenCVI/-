package com.kuxuan.moneynote.ui.weight;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.ui.activitys.home.DataGenerator;
import com.kuxuan.moneynote.utils.DrawableUtil;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xieshengqi on 2017/10/18.
 */

public class NavigationLayout extends FrameLayout implements View.OnClickListener {


    private FrameLayout mingxi_frame, baobiao_frame, faxian_frame, user_frame, center_frame;
    private ImageView mingxi_img, baobiao_img, faxian_img, user_img;
    private TextView mingxi_text, baobiao_text, faxian_text, user_text;

    private CircleImageView CI_detail, CI_chart, CI_find, CI_me;

    private ImageView[] img = {mingxi_img, baobiao_img, faxian_img, user_img};
    private CircleImageView[] CI_imgarr = {CI_detail, CI_chart, CI_find, CI_me};
    private TextView[] text = {mingxi_text, baobiao_text, faxian_text, user_text};
    private int currentPosition = 0;


    private Context mContext;

    public NavigationLayout(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public NavigationLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NavigationLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    CircleImageView CI_enter;

    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_navigation, this);
        String[] stringArray = context.getResources().getStringArray(R.array.navigation_title);

        CI_enter = view.findViewById(R.id.CI_enter);


        mingxi_frame = view.findViewById(R.id.layout_mingxi);
        img[0] = view.findViewById(R.id.tab_mingxi_img);
        mingxi_text = view.findViewById(R.id.tab_mingxi_text);
        CI_imgarr[0] = view.findViewById(R.id.CI_detail);


        baobiao_frame = view.findViewById(R.id.layout_baobiao);
        img[1] = view.findViewById(R.id.tab_baobiao_img);
        baobiao_text = view.findViewById(R.id.tab_baobiao_text);
        CI_imgarr[1] = view.findViewById(R.id.CI_chart);

        faxian_frame = view.findViewById(R.id.layout_faxian);
        img[2] = view.findViewById(R.id.tab_faxian_img);
        faxian_text = view.findViewById(R.id.tab_faxian_text);
        CI_imgarr[2] = view.findViewById(R.id.CI_find);

        user_frame = view.findViewById(R.id.layout_user);
        img[3] = view.findViewById(R.id.tab_user_img);
        user_text = view.findViewById(R.id.tab_user_text);
        CI_imgarr[3] = view.findViewById(R.id.CI_me);

        center_frame = view.findViewById(R.id.layout_center);

        mingxi_text.setText(stringArray[0]);
        baobiao_text.setText(stringArray[1]);
        faxian_text.setText(stringArray[2]);
        user_text.setText(stringArray[3]);
        mingxi_frame.setOnClickListener(this);
        baobiao_frame.setOnClickListener(this);
        faxian_frame.setOnClickListener(this);
        user_frame.setOnClickListener(this);
        center_frame.setOnClickListener(this);
        selectPosition(0);
        setCenter_CIColor();
//        replace(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_mingxi:
                initialization(0);
                break;
            case R.id.layout_baobiao:
                initialization(1);
                break;
            case R.id.layout_faxian:
                initialization(2);
                break;
            case R.id.layout_user:
                initialization(3);

                break;
            case R.id.layout_center:
//点击中间的记账
                listener.onClickCenter();
                break;
        }
    }


    public OnItemClickListener listener;


    public void setListener(OnItemClickListener lis) {
        this.listener = lis;
    }

    private void initialization(int position) {
        if (currentPosition == position) {
            return;
        }
        img[0].setSelected(false);
        mingxi_text.setSelected(false);
        baobiao_text.setSelected(false);
        img[1].setSelected(false);
        faxian_text.setSelected(false);
        img[2].setSelected(false);
        user_text.setSelected(false);
        img[3].setSelected(false);

        CI_imgarr[0].setImageResource(0);
        CI_imgarr[1].setImageResource(0);
        CI_imgarr[2].setImageResource(0);
        CI_imgarr[3].setImageResource(0);

        selectPosition(position);
//        replace(position);
    }

    private void selectPosition(int position) {
        currentPosition = position;
//        switch (position) {
//            case 0:
//                img[0].setSelected(true);
//                mingxi_text.setSelected(true);
//                break;
//            case 1:
//                baobiao_text.setSelected(true);
//                img[1].setSelected(true);
//                CI_imgarr[1].setImageResource(R.color.hometab_skin_2);
//                break;
//            case 2:
//                faxian_text.setSelected(true);
//                img[2].setSelected(true);
//                CI_imgarr[2].setImageResource(R.color.hometab_skin_2);
//                break;
//            case 3:
//                user_text.setSelected(true);
//                img[3].setSelected(true);
//                CI_imgarr[3].setImageResource(R.color.hometab_skin_2);
//                break;
//        }
        setColor(position);
        if (listener != null) {
            listener.onClick(position);
        }
    }


    private void setColor(int position) {

//        int checked = (int) SPUtil.get(mContext, Constant.Skin.CHECKED,2);
//
//        switch (checked) {
//            case 0:
//                CI_imgarr[position].setImageResource(R.color.hometab_skin_1);
//                break;
//            case 1:
//                CI_imgarr[position].setImageResource(R.color.hometab_skin_2);
//                break;
//            case 2:
//                CI_imgarr[position].setImageResource(R.color.hometab_skin_3);
//                break;
//            case 3:
//                CI_imgarr[position].setImageResource(R.color.hometab_skin_4);
//                break;
//            case 4:
//                CI_imgarr[position].setImageResource(R.color.hometab_skin_5);
//                break;
//            case 5:
//                CI_imgarr[position].setImageResource(R.color.hometab_skin_6);
//                break;
//        }
        Drawable drawableColor = new ColorDrawable(DrawableUtil.getSkinColor(mContext));
        CI_imgarr[position].setImageDrawable(drawableColor);

    }


    public void setCenter_CIColor() {
        Drawable drawableColor = new ColorDrawable(DrawableUtil.getSkinColor(mContext));
        CI_enter.setImageDrawable(drawableColor);
//        switch (Position) {
//            case 0:
//                CI_enter.setImageResource(R.color.hometab_skin_1);
//                break;
//            case 1:
//                CI_enter.setImageResource(R.color.hometab_skin_2);
//                break;
//            case 2:
//                CI_enter.setImageResource(R.color.hometab_skin_3);
//                break;
//            case 3:
//                CI_enter.setImageResource(R.color.hometab_skin_4);
//                break;
//            case 4:
//                CI_enter.setImageResource(R.color.hometab_skin_5);
//                break;
//            case 5:
//                CI_enter.setImageResource(R.color.hometab_skin_6);
//                break;
//        }

    }


    public void refresh(int position) {
        setColor(position);
    }

    public void setPosition(int position) {
        initialization(position);
//        selectPosition(position);
    }


    public interface OnItemClickListener {
        void onClick(int position);

        void onClickCenter();
    }

    //foucus换图片
    public void replace(int position) {
        for (int i = 0; i < DataGenerator.getFragments().length; i++) {
            if (i != position) {
                img[i].setImageResource(DataGenerator.M_TAB_RES[i]);
            } else {
                img[i].setImageResource(DataGenerator.M_TAB_RES_PRESSED[i]);
            }
        }
    }


}
