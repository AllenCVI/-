package com.kuxuan.moneynote.ui.weight;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kuxuan.moneynote.R;

import java.util.ArrayList;


/**
 * tablayout辅助类
 * Created by xieshengqi on 2017/7/14.
 */

public class TabLayoutOpertor {

    private Context mContext;
    private TabLayout tabLayout;

    private OnTabLayoutSelectListener mListener;

    private int currentPosition = 0;

    public TabLayoutOpertor(Context mContext, TabLayout tabLayout, OnTabLayoutSelectListener mListener) {
        this.mContext = mContext;
        this.tabLayout = tabLayout;
        this.mListener = mListener;

    }


    private ArrayList<String> dataLists;


    public void setDataLists(ArrayList<String> lists) {
        this.dataLists = lists;
        initTabLayout();
    }

    public void initTabLayout() {
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        if (dataLists != null) {
            for (int i = 0; i < dataLists.size(); i++) {
                View custormView = getCustormView(mContext, dataLists.get(i));
                tabLayout.addTab(tabLayout.newTab().setCustomView(custormView));
                if (i == currentPosition) {
                    View icon = custormView.findViewById(R.id.item_schedule_view);
                    TextView text = (TextView) custormView.findViewById(R.id.item_schedule_text);
                    icon.setVisibility(View.VISIBLE);
                    text.setTextColor(Color.parseColor("#6C7B8A"));
                }
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedTabPosition = tab.getPosition();
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    View view = tabLayout.getTabAt(i).getCustomView();
                    View icon = view.findViewById(R.id.item_schedule_view);
                    TextView text = (TextView) view.findViewById(R.id.item_schedule_text);
                    if (i == tab.getPosition()) { // 选中状态
                        icon.setVisibility(View.VISIBLE);
                        text.setTextColor(Color.parseColor("#6C7B8A"));
                    } else {// 未选中状态
                        icon.setVisibility(View.INVISIBLE);
                        text.setTextColor(Color.parseColor("#B26C7B8A"));
                    }
                }
                mListener.onTabSelect(tab, selectedTabPosition);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private View getCustormView(Context context, String content) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_schedule_tablayout, null);
        TextView tabText = (TextView) view.findViewById(R.id.item_schedule_text);
        tabText.setText(content);
        return view;
    }


    public interface OnTabLayoutSelectListener {
        void onTabSelect(TabLayout.Tab tab, int position);
    }


}
