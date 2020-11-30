package com.kuxuan.moneynote.ui.activitys.home;

import android.support.v4.app.Fragment;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.ui.fragments.details.DetialFragment;

/**
 * @author xieshengqi
 */
public class DataGenerator {

    public static final int[] M_TAB_RES =
            new int[]{R.drawable.bottom_detail_normal, R.drawable.bottom_chart_normal, R.drawable.bottom_find_normal, R.drawable.bottom_me_normal};

    public static final int[] M_TAB_RES_PRESSED = new int[]{R.drawable.bottom_detail_pressed, R.drawable.bottom_chart_pressed, R.drawable.bottom_find_pressed, R.drawable.bottom_me_pressed};

    public static Fragment[] getFragments() {
        Fragment[] fragments = new Fragment[4];
        fragments[0] = new DetialFragment();
//        fragments[1] = new ReportSingleFragment();
//        fragments[1] = new TestFragment();
//        fragments[2] = new FindFragment();
//        fragments[3] = new MineFragment();
        return fragments;
    }

//    /**
//     * 获取Tab 显示的内容
//     *
//     * @param context
//     * @param position
//     * @return
//     */
//    public static View getTabView(Context context, int position) {
//        View view = LayoutInflater.from(context).inflate(R.layout.home_tab_content, null);
//        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
//        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
//        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
//        tabText.setText(mTabTitle[position]);
//        return view;
//    }
}