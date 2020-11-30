package com.kuxuan.moneynote.utils;

import android.content.Context;
import android.support.design.widget.TabLayout;

import com.kuxuan.moneynote.json.NewChartData;

import java.util.ArrayList;

/**
 * Created by xieshengqi on 2018/3/8.
 */

public class TabLayoutUtil {

    /**
     * 重新计算需要滚动的距离
     *
     * @param index 选择的tab的下标
     */
    public static void recomputeTlOffset1(final TabLayout tabLayout, int index, ArrayList<NewChartData> data, Context context) {
        if (tabLayout.getTabAt(index) != null) tabLayout.getTabAt(index).select();
        final int width = (int) (getTablayoutOffsetWidth(index,data)*  context.getResources().getDisplayMetrics().density);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.smoothScrollTo(width, 0);
            }
        });
    }


//重中之重是这个计算偏移量的方法，各位看官看好了。

    /**
     * 根据字符个数计算偏移量
     *
     * @param index 选中tab的下标
     * @return 需要移动的长度
     */
    public static int getTablayoutOffsetWidth(int index, ArrayList<NewChartData> newChartData) {
        String str = "";
        for (int i = 0; i < index; i++) {
            //channelNameList是一个List<String>的对象，里面转载的是30个词条
            //取出直到index的tab的文字，计算长度
            str += newChartData.get(i).getKey();
        }
        return str.length() * 8 + index * 30;
    }
}
