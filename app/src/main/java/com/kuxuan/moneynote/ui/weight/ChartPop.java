package com.kuxuan.moneynote.ui.weight;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.json.PopCharData;
import com.kuxuan.moneynote.utils.DisplayUtil;
import com.kuxuan.moneynote.utils.GlideUtil;
import com.kuxuan.moneynote.utils.JavaFormatUtils;

import java.util.ArrayList;

/**
 * Created by xieshengqi on 2017/10/23.
 */

public class ChartPop extends PopupWindow {


    private LinearLayout linearLayout;
    private LinearLayout viewLayout;
    private ImageView image;
    private Context mContext;

    /**
     * 代表周1，月2，年3
     */
    private int type = 1;

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ChartPop(Context context) {
        super(context);
        initView(context);
    }

    public ChartPop(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ChartPop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.pop_chart_layout, null);
        linearLayout = conentView.findViewById(R.id.pop_chart_layout);
        viewLayout = conentView.findViewById(R.id.pop_chartview_layout);
        image = conentView.findViewById(R.id.pop_chart_image);
        this.setContentView(conentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);
    }


    /**
     * 填充数据
     *
     * @param list
     */
    public boolean setData(ArrayList<PopCharData> list) {
        if (viewLayout == null)
            return false;
        viewLayout.removeAllViews();
        if (list == null || list.size() == 0) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (i < 3) {
                View view = getView(list.get(i));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(params);
                viewLayout.addView(view);
            }
        }
        return true;

    }

    private View getView(PopCharData charData) {
        View childView = LayoutInflater.from(mContext).inflate(R.layout.item_chartpop_layout, null);
        TextView time = childView.findViewById(R.id.item_chartpop_time_text);
        TextView name = childView.findViewById(R.id.item_chartpop_name_text);
        TextView count = childView.findViewById(R.id.item_chartpop_count_text);
        ImageView image = childView.findViewById(R.id.item_chartpop_image);
        if (type == 1) {
        }
        //格式为18/03/01之前格式为2018/03/01
        String substring = charData.getTime().substring(2, charData.getTime().length());
        time.setText(substring);
        name.setText(charData.getName());
        count.setText(JavaFormatUtils.formatFloatNumber(Double.parseDouble(charData.getAccount())));
        GlideUtil.setImageWithNoCache(mContext, charData.getSmall_icon(), image);
        return childView;
    }



    /**
     * 设置位置
     *
     * @param fixY 上面布局的总高
     * @param x
     * @param y
     */
    public void setParamsForLayout(int fixY, float x, float y) {
        int allY = (int) (fixY + y);
        int leftMargin = -DisplayUtil.dip2px(4);
        int left;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) image.getLayoutParams();
        layoutParams1.setMargins((int) x, 0, 0, 0);
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        if (x <= layoutParams.width / 2) {
            //不用左边距，直接靠左
            left = 0;
        } else {
            left = (int) (x - layoutParams.width / 2);
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            if (DisplayUtil.getScreenWidth() - x <= layoutParams.width / 2) {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                left = 0;
            }
        }
        layoutParams.setMargins(left + leftMargin, allY - layoutParams.height, 0, 0);
        layoutParams1.setMargins((int) x + leftMargin, allY - layoutParams1.height, 0, 0);
        linearLayout.setLayoutParams(layoutParams);
        image.setLayoutParams(layoutParams1);
    }
}
