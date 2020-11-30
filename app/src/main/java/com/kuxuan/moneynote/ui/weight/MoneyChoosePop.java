package com.kuxuan.moneynote.ui.weight;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.kuxuan.moneynote.R;

/**
 * Created by xieshengqi on 2017/10/20.
 */

public class MoneyChoosePop extends PopupWindow {


    public static final int ZHICHU = 10001;
    public static final int SHOURU = 10002;
    private Context mContext;
    private ImageView image_zhichu, image_shouru;
    private RelativeLayout zhichu_layout, shouru_layout;

    private View bgView;


    private int type = ZHICHU;


    public MoneyChoosePop(Context context) {
        super(context);
        initView(context);
    }

    public MoneyChoosePop(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MoneyChoosePop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.pop_money_layout, null);
        zhichu_layout = conentView.findViewById(R.id.pop_money_zhichu_layout);
        shouru_layout = conentView.findViewById(R.id.pop_money_shouru_layout);
        image_zhichu = conentView.findViewById(R.id.pop_money_zhichu_img);
        image_shouru = conentView.findViewById(R.id.pop_money_shouru_img);
        bgView = conentView.findViewById(R.id.pop_money_bgView);
        //获取popupwindow的高度与宽度
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        final ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);

        zhichu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initImage();
                image_zhichu.setVisibility(View.VISIBLE);

                if (listener != null && type != ZHICHU) {
                    listener.onClick(ZHICHU);
                    type = ZHICHU;

                }
                dismiss();
            }
        });
        shouru_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initImage();
                image_shouru.setVisibility(View.VISIBLE);


                if (listener != null && type != SHOURU) {
                    listener.onClick(SHOURU);
                    type = SHOURU;

                }
                dismiss();
            }
        });

            bgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }


    public void setType(int type) {
        initImage();
        this.type = type;
        if (type == 1) {
            this.type = SHOURU;
            image_shouru.setVisibility(View.VISIBLE);
        } else {
            this.type = ZHICHU;
            image_zhichu.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        bgView.setBackgroundColor(Color.parseColor("#80000000"));
    }

    OnPopClickListener listener;


    public void setOnPopClickListener(OnPopClickListener listener) {
        this.listener = listener;
    }

    public interface OnPopClickListener {
        void onClick(int type);
    }


    private void initImage() {
        image_zhichu.setVisibility(View.INVISIBLE);
        image_shouru.setVisibility(View.INVISIBLE);
    }


}
