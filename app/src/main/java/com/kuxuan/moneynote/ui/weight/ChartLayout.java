package com.kuxuan.moneynote.ui.weight;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.json.CategoryDataJson;
import com.kuxuan.moneynote.json.LineJson;
import com.kuxuan.moneynote.utils.JavaFormatUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by xieshengqi on 2017/10/20.
 */

public class ChartLayout extends RelativeLayout {

    /**
     * 判断是收入还是支出
     * 1 收入 2 支出
     */


    private int type = 2;
    private LinearLayout linelayout, yuanhuanLayout;
    private TextView zhichu_text, average_text;
    private LineChartView lineChartView;

    private PieChart piechart;

    private LinearLayout piechart_layout;

    private Context mContext;

    /**
     * 时间类型
     * 0代表周，1代表月，2代表年
     */
    private int timeType = 0;


    public static final int LINE = 0;
    public static final int YUANHUAN = 1;

    public ChartLayout(Context context) {
        super(context);
        initView(context);
    }

    public ChartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ChartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    OnChartPopListener listener;

    public void setOnChartPopListener(OnChartPopListener listener) {
        this.listener = listener;
    }

    public interface OnChartPopListener {
        void showPop(float x, float y, LineJson li);

        void dismiss();
    }


    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.chart_layout, this);
        linelayout = view.findViewById(R.id.chart_linelayout);
        yuanhuanLayout = view.findViewById(R.id.chart_yuanhuanlayout);
        zhichu_text = view.findViewById(R.id.chart_line_allmoney);
        average_text = view.findViewById(R.id.chart_line_averagemoney);
        lineChartView = view.findViewById(R.id.linechartView);
        piechart = view.findViewById(R.id.chart_piechart);
        piechart_layout = view.findViewById(R.id.chart_yuanhualist_layout);
        initPiechart();
        setListenerChartView();
    }


    private void setListenerChartView() {
        lineChartView.setOnPopListener(new LineChartView.OnPopShowListener() {
            @Override
            public void showPop(int position, float x, float y, LineJson lineJson) {
                Log.e("showPop", "x:  " + x + ",Y:  " + y);
                if (listener != null) {
                    listener.showPop(x, y, lineJson);
                }
            }

            @Override
            public void dismiss() {

                if (listener != null) {
                    listener.dismiss();
                }
                Log.e("showPop", "dismiss");

            }
        });
    }


    public void showLineLayout() {
        linelayout.setVisibility(View.VISIBLE);
        yuanhuanLayout.setVisibility(View.GONE);
    }


    public void showYuanhuanLayout() {
        linelayout.setVisibility(View.GONE);
        yuanhuanLayout.setVisibility(View.VISIBLE);
    }


    // 获得数组中最大值
    private float findMax(float[] array) {
        float max = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }


    public void setType(int type) {
        this.type = type;
    }

    /**
     * 填充上面总收入平均值
     *
     * @param data
     */
    private void setTopContent(ArrayList<LineJson> data) {
        switch (type) {
            case 2:
                //支出
                float zhichucount = 0;
                for (int i = 0; i < data.size(); i++) {
                    zhichucount += data.get(i).getyAixs();
                }
                zhichu_text.setText("总支出：" + JavaFormatUtils.getDataForTwo(zhichucount));
                average_text.setText("平均值：" + JavaFormatUtils.getDataForTwo(zhichucount / data.size()));
                break;
            case 1:
                //收入
                float shourucount = 0;
                for (int i = 0; i < data.size(); i++) {
                    shourucount += data.get(i).getyAixs();
                }
                zhichu_text.setText("总收入：" + JavaFormatUtils.getDataForTwo(shourucount));
                average_text.setText("平均值：" + JavaFormatUtils.getDataForTwo(shourucount / data.size()));
                break;


        }
    }

    ArrayList<LineJson> lineJsons;

    /**
     * 设置折线图的值
     *
     * @param data
     */
    public void setLineData(ArrayList<LineJson> data) {
        lineJsons = data;
        ArrayList xItemArray = new ArrayList();
        ArrayList<Float> yItemArray = new ArrayList<>();
        float[] yItem = new float[data.size()];
        for (int i = 0; i < data.size(); i++) {
            xItemArray.add(data.get(i).getxAixs());
            yItemArray.add(data.get(i).getyAixs());
            yItem[i] = data.get(i).getyAixs();
        }
        lineChartView.setData(xItemArray, yItemArray, findMax(yItem), data);
        setTopContent(data);
    }

    private void initPiechart() {
        piechart.setDrawEntryLabels(false);
        Description description = new Description();
        description.setText("");
        piechart.setDescription(description);
        piechart.setCenterText("测试\n总支出");
        piechart.setRotationEnabled(false);
        /**
         * 是否显示圆环中间的洞
         */
        piechart.setDrawHoleEnabled(true);
        /**
         * 设置中间洞的颜色
         */
        piechart.setHoleColor(Color.WHITE);

        /**
         * 设置圆环透明度及半径
         */
//        piechart.setTransparentCircleColor(Color.YELLOW);
//        piechart.setTransparentCircleAlpha(110);
        piechart.setTransparentCircleRadius(76);
        piechart.setTransparentCircleAlpha(0);

        /**
         * 设置圆环中间洞的半径
         */
        piechart.setHoleRadius(60);
    }


    private int[] huanData = {70, 12, 8, 7, 5};


    /**
     * 获取其他数据总和
     *
     * @param lists
     * @return
     */
    private float getQitaCount(ArrayList<CategoryDataJson> lists) {
        double count = 0;
        double allCount = 0;
        for (int i = 5; i < lists.size(); i++) {
            count += lists.get(i).getCategory_account();
            allCount = lists.get(i).getAllAccount();
        }
        return (float) (count / allCount);
    }

    /**
     * 最多5种
     */
    public void addDataSet(ArrayList<CategoryDataJson> lists) {
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        Collections.sort(lists);
        //正常颜色
        //异常颜色
        double allCount = 0;
        if (lists != null) {
            if (lists.size() != 0) {
                for (int i = 0; i < lists.size(); i++) {
                    //只能在5个之内
                    if (i < 6) {
                        allCount = lists.get(i).getAllAccount();
                        double da = lists.get(i).getCategory_account() / lists.get(i).getAllAccount();
                        if (i < 5) {

                            yEntry.add(new PieEntry((float) (da * 100)));
                        } else {
                            float qitaCount = getQitaCount(lists);

                            yEntry.add(new PieEntry(getQitaCount(lists) * 100));
                        }
                        colors.add(mContext.getResources().getColor(colos[i]));
                    } else {
                        break;
                    }
                }
            } else {
                for (int i = 0; i < 6; i++) {
                    lists.add(new CategoryDataJson());
                }
                yEntry.add(new PieEntry(100));
                colors.add(Color.rgb(239, 239, 244));
            }

            PieDataSet pieDataSet = new PieDataSet(yEntry, "employee");
            //设置间隔
            pieDataSet.setSliceSpace(0);
            //设置字号
            pieDataSet.setValueTextSize(0);
            pieDataSet.setColors(colors);
            Legend legend = piechart.getLegend();
            legend.setForm(Legend.LegendForm.CIRCLE);
            legend.setEnabled(false);
            legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
            PieData pieData = new PieData(pieDataSet);
            String value = null;
            NumberFormat nf = NumberFormat.getInstance();
            // 设置此格式中不使用分组
            nf.setGroupingUsed(false);
            // 设置数的小数部分所允许的最大位数。
            nf.setMaximumFractionDigits(2);
            value = nf.format(allCount);
            if (type == 2) {
                piechart.setCenterText("总支出\n" + value + "");
            } else {
                piechart.setCenterText("总收入\n" + value + "");
            }
            piechart.setData(pieData);
            piechart.animateX(1000);
            piechart.invalidate();
            setLinearlayoutData(lists);
        }

    }

    /**
     * 填充圆环右面的数据
     */
    private void setLinearlayoutData(ArrayList<CategoryDataJson> lists) {
        piechart_layout.removeAllViews();
        for (int i = 0; i < lists.size(); i++) {
            if (i < 6)
                piechart_layout.addView(getView(i, lists.get(i), lists), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }


    private int[] colos = {R.color.bg_chart_color_5, R.color.bg_chart_color_4, R.color.bg_chart_color_3, R.color.bg_chart_color_2, R.color.bg_chart_color_1, R.color.bg_chart_color_0};
    private int[] drawables = {R.drawable.bg_chart_color_5, R.drawable.bg_chart_color_4, R.drawable.bg_chart_color_3, R.drawable.bg_chart_color_2, R.drawable.bg_chart_color_1, R.drawable.bg_chart_color_0};

    private View getView(int position, CategoryDataJson categoryDataJson, ArrayList<CategoryDataJson> lists) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chartdata_layout, null);
        TextView imgtext = view.findViewById(R.id.item_chartdata_colorimg);
        imgtext.setBackground(mContext.getResources().getDrawable(drawables[position]));
        TextView name_ext = view.findViewById(R.id.item_chartdata_name);
        TextView baifenbi_text = view.findViewById(R.id.item_chartdata_baifenbi);
        if (categoryDataJson.getName() != null) {
            name_ext.setText(categoryDataJson.getName());
            baifenbi_text.setText(categoryDataJson.getBaifenbi() + "%");
            if (position == 5) {
                name_ext.setText("其他");
                DecimalFormat df = new DecimalFormat("0.0");
                ;
                baifenbi_text.setText(df.format(getQitaCount(lists) * 100) + "%");
            }
        } else {
            name_ext.setText("-");
            baifenbi_text.setText("0.0 %");
        }


        return view;
    }

    public void setData(int type, ArrayList<LineJson> data) {
        switch (type) {
            case LINE:
                setLineData(data);
                setTopContent(data);
                break;
            case YUANHUAN:

                break;
        }
    }

}
