package com.kuxuan.moneynote.ui.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.json.LineJson;
import com.kuxuan.moneynote.json.PointJson;
import com.kuxuan.moneynote.utils.DisplayUtil;

import java.util.ArrayList;

/**
 * 折线图
 *
 * @author xieshengqi
 */
public class LineChartView extends View {
    private static final String TAG = "LineChartView";
    //Y轴  每个刻度的间距间距
    private int myInterval;
    //X轴  每个刻度的间距间距
    private int mxInterval;
    //Y轴距离view长度
    private int mLeftInterval;
    //X轴距离view长度
    private int mBottomInterval;
    //X轴距离view顶部长度
    private int mTopInterval;
    //Y轴字体的大小
    private float mYAxisFontSize;
    //虚线间隔
    private int mXuxianInterval = 20;
    //View 的宽和高
    private int mWidth, mHeight;
    //线的颜色
    private int mLineColor;
    //线条的宽度
    private float mStrokeWidth = 4.0f;
    //X轴的文字
    private ArrayList<String> mXAxis = new ArrayList<>();
    //点 (温度)
    private ArrayList<Float> mYAxis = new ArrayList<>();
    //纵轴最大值
    private float maxYValue;
    //纵轴分割数量
    private int dividerCount;
    //画坐标线的轴
    private Paint axisPaint;
    //画X轴文字
    private Paint axisTextPaint;
    //连接线条
    private Paint linePaint;
    //小圆点内环
    private Paint innerCirclePaint;
    //小圆点中间环
    private Paint middleCiclePaint;
    //小圆点外环
    private Paint outterCiclePaint;
    //折线路径
    private Path mpolylinePath;
    //小圆点内环半径
    private int innerCircleRadius;
    //小圆点中间环半径
    private int middleRadius;
    //小圆点外环半径
    private int outerRadius;

    //x坐标有多少
    private int number_xAixs = 0;


    private ArrayList<PointJson> points;


    public LineChartView(Context context) {
        this(context, null);
        init(context);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LineChartView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int index = a.getIndex(i);
            switch (index) {
                // 折线颜色
                case R.styleable.LineChartView_lineColor:
                    mLineColor = a.getColor(index, Color.BLACK);
                    break;
                // X轴每个刻度的间距间距
                case R.styleable.LineChartView_dividerCount:
                    dividerCount = a.getInt(index, 5);
                    break;
                // X轴每个刻度的间距间距
                case R.styleable.LineChartView_xInterval:
                    mxInterval = a.getDimensionPixelSize(index, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()));
                    break;
                // Y轴距离view长度
                case R.styleable.LineChartView_leftInterval:
                    mLeftInterval = a.getDimensionPixelSize(index, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
                    break;
                // X轴距离view底部的高度
                case R.styleable.LineChartView_bottomInterval:
                    mBottomInterval = a.getDimensionPixelSize(index, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
                    break;
                // X轴距离view顶部长度
                case R.styleable.LineChartView_topInterval:
                    mTopInterval = a.getDimensionPixelSize(index, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
                    break;
                // Y轴字体的大小
                case R.styleable.LineChartView_yAxisFontSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    mYAxisFontSize = a.getDimensionPixelSize(index, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 13, getResources().getDisplayMetrics()));
                    break;
            }

        }
        a.recycle();


    }

    private void init(Context context) {
        // 画坐标线的轴
        points = new ArrayList<>();
        axisPaint = new Paint();
        axisPaint.setTextSize(mYAxisFontSize);
        axisPaint.setAntiAlias(true);
        axisPaint.setStrokeWidth(DisplayUtil.dip2px(2));
        axisPaint.setColor(Color.parseColor("#f7f7f7"));

        // 画X轴文字
        axisTextPaint = new Paint();
        axisTextPaint.setTextSize(mYAxisFontSize);
        axisTextPaint.setAntiAlias(true);
        axisTextPaint.setColor(Color.parseColor("#6d6d6d"));

        // 连接线条
        linePaint = new Paint();
        linePaint.setColor(mLineColor);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(mStrokeWidth);

        // 小圆点内环
        innerCirclePaint = new Paint();
        innerCirclePaint.setStyle(Paint.Style.FILL);
        innerCirclePaint.setAntiAlias(true);
        innerCirclePaint.setColor(Color.rgb(255, 255, 255));
        innerCirclePaint.setStrokeWidth(DisplayUtil.dip2px(0.5f));

        // 小圆点中间环
        middleCiclePaint = new Paint();
        middleCiclePaint.setStyle(Paint.Style.STROKE);
        middleCiclePaint.setAntiAlias(true);
        middleCiclePaint.setColor(mLineColor);
        middleCiclePaint.setStrokeWidth(DisplayUtil.dip2px(0.5f));

        // 小圆点外环
        outterCiclePaint = new Paint();
        outterCiclePaint.setStyle(Paint.Style.STROKE);
        outterCiclePaint.setAntiAlias(true);
        outterCiclePaint.setColor(Color.parseColor("#6d6d6d"));
        outterCiclePaint.setStrokeWidth(DisplayUtil.dip2px(0.5f));

        // 折线路径
        mpolylinePath = new Path();

        //小圆点内环半径
        innerCircleRadius = DisplayUtil.dip2px(2);
        //小圆点中间环半径
        middleRadius = DisplayUtil.dip2px(2.5f);
        //小圆点外环半径
        outerRadius = DisplayUtil.dip2px(2.5f);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        myInterval = (getHeight() - mBottomInterval - mTopInterval) / dividerCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        mHeight = heightSize;
        mWidth = DisplayUtil.getScreenWidth();

        Log.d(TAG, "widthSize:" + mWidth + ",heightSize:" + mHeight);
        setMeasuredDimension(mWidth, mHeight);
//        if (mXAxis == null) {
//            Log.d(TAG, "mWidth:" + mWidth + ",mHeight:" + mHeight + "mXAxis:" + mXAxis);
//            return;
        //宽度通过数组长度计算
//        mWidth = mxInterval*(mXAxis.size()-1) + mLeftInterval*2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mXAxis.size() == 0 || mYAxis.size() == 0) {
            Log.e(TAG, "数据异常");
            return;
        }
//        mxInterval = (mWidth-(outerRadius*2)*mXAxis.size())/mXAxis.size();

        // 画横线
        axisPaint.setColor(Color.parseColor("#f8f8f8"));
//        axisPaint.setColor(Color.parseColor("#000000"));
        axisPaint.setStrokeWidth(DisplayUtil.dip2px(2));
        for (int i = 0; i <= dividerCount; i++) {
            if (i == 0 || i == dividerCount) {
                Log.d(TAG, mLeftInterval + ":" + (mXAxis.size() - 1) + "：" + mxInterval + "：" + mLeftInterval);
                canvas.drawLine(mLeftInterval, mHeight - mBottomInterval - i * myInterval, (mXAxis.size() - 1) * mxInterval + (outerRadius * 2) * (mXAxis.size() - 1)+mLeftInterval ,
                        mHeight - mBottomInterval - myInterval * i, axisPaint);
            }
        }
        //画平均线
//        axisPaint.setColor(Color.parseColor("#b4b4b4"));
//        axisPaint.setStrokeWidth(DisplayUtil.dip2px(1));
//        int count = (mXAxis.size() - 1) * mxInterval / mXuxianInterval;
//        for (int i = 0; i < count; i++) {
//            int lineWidth = mXuxianInterval;
//            int start = mLeftInterval + (lineWidth * 2) * i;
//            int end = start + lineWidth;
//            boolean isBreak = false;
//            if (end > (mXAxis.size() - 1) * mxInterval + mLeftInterval) {
//                end = (mXAxis.size() - 1) * mxInterval + mLeftInterval;
//                isBreak = true;
//            }
//            float h = (mHeight-mBottomInterval)-((mHeight - mBottomInterval) * average/maxYValue);
//            canvas.drawLine(start, h, end,
//                    h, axisPaint);
//            if (isBreak) {
//                break;
//            }
//        }
        // x轴的刻度集合
        int[] xPoints = new int[mXAxis.size()];
        for (int i = 0; i < mXAxis.size(); i++) {
            float xTextWidth = axisPaint.measureText(mXAxis.get(i)) / 2; //文字宽度一半
            float xfloat = (i) * mxInterval + mLeftInterval - xTextWidth + (outerRadius * 2) * (i - 1);
            ;
            // 画X轴的文字
            if (mXAxis.size() > 7) {
                if (i != 1 && (i + 1) % 5 == 0) {
                    //31天不画30
                    if (mXAxis.size() == 31) {
                        if (i != 29) {
                            canvas.drawText(mXAxis.get(i), xfloat, mHeight - mBottomInterval + 10 + mYAxisFontSize, axisTextPaint);
                        }
                    } else {
                        canvas.drawText(mXAxis.get(i), xfloat, mHeight - mBottomInterval + 10 + mYAxisFontSize, axisTextPaint);
                    }
                } else if (i == mXAxis.size() - 1) {
                    canvas.drawText(mXAxis.get(i), xfloat, mHeight - mBottomInterval + 10 + mYAxisFontSize, axisTextPaint);
                } else if (i == 0) {
                    canvas.drawText(mXAxis.get(i), xfloat, mHeight - mBottomInterval + 10 + mYAxisFontSize, axisTextPaint);
                }
            } else {
                canvas.drawText(mXAxis.get(i), xfloat, mHeight - mBottomInterval + 10 + mYAxisFontSize, axisTextPaint);
            }
//            canvas.drawText(mXAxis.get(i), xfloat, mHeight - mBottomInterval + 10 + mYAxisFontSize, axisTextPaint);
            xPoints[i] = (int) (xfloat + xTextWidth);
            // 画竖线
//            float xvfloat =  i * mxInterval + mLeftInterval;
//            canvas.drawLine(xvfloat,mHeight - mBottomInterval, xvfloat,
//                    mHeight - mBottomInterval - myInterval*dividerCount, axisPaint);
        }


        //画平均值虚线


        /**
         * 画轨迹
         */
        int y = myInterval * (dividerCount); // 只拿纵轴的dividerCount-1/dividerCount画图
        axisPaint.setColor(mLineColor); // 设置坐标值的颜色
        mpolylinePath.reset();
        for (int i = 0; i < mYAxis.size(); i++) {
            int h = (int) (mHeight - (mBottomInterval + y * mYAxis.get(i) / maxYValue));
            if (i == 0) {
                mpolylinePath.moveTo(mLeftInterval+(outerRadius*2)*(i-1), h);
            } else {
                mpolylinePath.lineTo(mLeftInterval + i * mxInterval+(outerRadius*2)*(i-1), h);
            }
        }
        canvas.drawPath(mpolylinePath, linePaint);

        /**
         * 画小圆圈
         */
        for (int i = 0; i < mYAxis.size(); i++) {
            int h = (int) (mHeight - (mBottomInterval + y * mYAxis.get(i) / maxYValue));
            //判断是不是有值
            if (mYAxis.get(i) != 0) {
                innerCirclePaint.setColor(Color.YELLOW);
            } else {
                innerCirclePaint.setColor(Color.WHITE);
            }
            Log.e("小圆点X坐标:", mLeftInterval + i * mxInterval + "");
            if (i == 0) {
                canvas.drawCircle(mLeftInterval+(outerRadius*2)*(i-1), h, innerCircleRadius, innerCirclePaint);
                canvas.drawCircle(mLeftInterval+(outerRadius*2)*(i-1), h, middleRadius, middleCiclePaint);
//                canvas.drawCircle(mLeftInterval,h,outerRadius,outterCiclePaint);
            } else {
                canvas.drawCircle(mLeftInterval + i * mxInterval+(outerRadius*2)*(i-1), h, innerCircleRadius, innerCirclePaint);
                canvas.drawCircle(mLeftInterval + i * mxInterval+(outerRadius*2)*(i-1), h, middleRadius, middleCiclePaint);
//                canvas.drawCircle(mLeftInterval + i*mxInterval,h,outerRadius,outterCiclePaint);
            }
            addPointData(mLeftInterval + i * mxInterval+(outerRadius*2)*(i-1), h, i);
        }

    }

    /**
     * 设置点的集合
     *
     * @param x
     * @param y
     * @param position
     */
    private void addPointData(float x, float y, int position) {
        PointJson pointJson = null;
        try {
            pointJson = points.get(position);
        } catch (Exception e) {

        }
        if (pointJson != null) {
            pointJson.setxPoint(x);
            pointJson.setyPoint(y);
        } else {
            pointJson = new PointJson();
            pointJson.setxPoint(x);
            pointJson.setyPoint(y);
            points.add(pointJson);
        }

    }

    //判断
    private boolean isShowPop;

    /**
     * 显示弹出层
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isShowPop = false;
                int i = jugePoint(event.getX(), event.getY());
                if (i != -1) {
                    isShowPop = true;
                    if (mListener != null) {
                        mListener.showPop(i, points.get(i).getxPoint(), points.get(i).getyPoint(), mDatas.get(i));
                    }
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (isShowPop) {
                    if (mListener != null) {
                        mListener.dismiss();
                    }
                }
                break;


        }
        return super.onTouchEvent(event);
    }


    OnPopShowListener mListener;


    public void setOnPopListener(OnPopShowListener listener) {
        this.mListener = listener;
    }

    public interface OnPopShowListener {
        void showPop(int position, float x, float y, LineJson lineJson);

        void dismiss();

    }

    /**
     * 手势按下临界值
     */
    private final float CTITICAL = 50;

    private int jugePoint(float x, float y) {
        int position = -1;
        for (int i = 0; i < points.size(); i++) {
            PointJson pointJson = points.get(i);
            float xP = pointJson.getxPoint();
            float yP = pointJson.getyPoint();
            if (Math.abs(x - xP) <= CTITICAL && Math.abs(y - yP) <= CTITICAL) {
                //符合条件
                position = i;
                break;
            }
        }
        return position;
    }

    /**
     * 初始化参数
     */
    private void initData() {
        //(view的宽度-mXai*小圆圈的直径)/(mXai-1)=每个之间的距离
        int w = DisplayUtil.getScreenWidth() - DisplayUtil.dip2px(30);
        number_xAixs = mXAxis.size();
        int cha = w - (number_xAixs * innerCircleRadius * 2);
        mxInterval = cha / number_xAixs;
        if(number_xAixs>12){
            mxInterval = mxInterval-DisplayUtil.dip2px(1);
        }
//        if (number_xAixs > 12)
//            mxInterval = 35;
//        if (number_xAixs == 12) {
//            mxInterval = 80;
//        }
//        Log.e("x间的距离", "w: " + w + ",cha: " + cha + ",mxInterval: " + mxInterval + ",number_xAixs * outerRadius * 2:  " + (number_xAixs * outerRadius * 2));
    }

    /**
     * 设置Y轴文字
     *
     * @param yItem
     */
    public void setYItem(ArrayList<Float> yItem) {
        mYAxis = yItem;
        int count = 0;
        for (int i = 0; i < yItem.size(); i++) {
            count += yItem.get(i);
        }
        try {
            //出现没有数据的情况
            average = count / yItem.size();
        } catch (Exception e) {
            average = 0;
        }
    }


    private ArrayList<LineJson> mDatas;

    /**
     * 设置X轴文字
     *
     * @param xItem
     */
    public void setXItem(ArrayList xItem) {
        mXAxis = xItem;
        initData();
    }


    public void setData(ArrayList xItem, ArrayList<Float> yItem, float maxYValue, ArrayList<LineJson> lineJson) {
        if (mDatas == null)
            mDatas = new ArrayList<>();
        mDatas.clear();
        mDatas.addAll(lineJson);
        setXItem(xItem);
        setYItem(yItem);
        setMaxYValue(maxYValue);
        invalidate();
        invalidate();
    }

    public float getAverage() {
        return average;
    }

    //平均值
    private float average = 0;

    public void setMaxYValue(float maxYValue) {
        this.maxYValue = maxYValue;
        if (maxYValue == 0f) {
            this.maxYValue = 1;
        }
    }
}
