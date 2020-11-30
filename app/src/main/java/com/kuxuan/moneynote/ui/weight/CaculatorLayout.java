package com.kuxuan.moneynote.ui.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuxuan.moneynote.R;

import java.math.BigDecimal;
import java.util.Stack;

import static com.kuxuan.moneynote.R.id.num;

/**
 * Created by seed on 2015/10/20.
 */
public class CaculatorLayout extends LinearLayout implements View.OnClickListener {

    private static final String INPUT_1 = "1";
    private static final String INPUT_2 = "2";
    private static final String INPUT_3 = "3";
    private static final String INPUT_4 = "4";
    private static final String INPUT_5 = "5";
    private static final String INPUT_6 = "6";
    private static final String INPUT_7 = "7";
    private static final String INPUT_8 = "8";
    private static final String INPUT_9 = "9";
    private static final String INPUT_0 = "0";
    private static final int NUMBER_2 = 2;
    private static final String INPUT_POINT = ".";
    private static final String INPUT_ADD = "+";
    private static final String INPUT_SUB = "-";
    private static final String TAG = "CaculatorLayout";
    private Context mContext;
    private View mLayout;
    //7、8、9、删除按钮
    private Button mNumberSevenBtn, mNumberEightBtn, mNumberNineBtn;
    //4、5、6、加按钮
    private Button mNumberFourBtn, mNumberFiveBtn, mNumberSixBtn, mAddBtn;
    //1、2、3、减按妞
    private Button mNumberOneBtn, mNumberTwoBtn, mNumberThreeBtn, mReduceBtn;
    //0、小数点、等于按钮
    private Button mNumberZeroBtn, mPointBtn, mResultBtn;
    private ImageButton mDeleteBtn;
    private TextView mNumText;

    private String shownumtext = "0";
    private String completenumtext = "0";

    private EditText beizhu;
    Sum sum;
    private static final String EQUAL = "=";
    private static final String COMPLETES = "完成";
    /**
     * 总共的文字
     */
    public static String numText = "";


    public CaculatorLayout(Context context) {
        super(context);
        this.mContext = context;
        iniView();
        addView(mLayout);
    }

    public CaculatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        iniView();
        addView(mLayout);
    }

    public CaculatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        iniView();
        addView(mLayout);
    }

    Stack<String> textStack;
    Stack<String> signStack;

    private void iniView() {

        textStack = new Stack<String>();
        signStack = new Stack<String>();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        mLayout = inflater.inflate(R.layout.popupwindow_caculator, null, false);
        mNumberSevenBtn = (Button) mLayout.findViewById(R.id.calculator_btn7);
        mNumberEightBtn = (Button) mLayout.findViewById(R.id.calculator_btn8);
        mNumberNineBtn = (Button) mLayout.findViewById(R.id.calculator_btn9);
        mDeleteBtn = (ImageButton) mLayout.findViewById(R.id.calculator_delete);
        beizhu = mLayout.findViewById(R.id.num_edit);


        mNumberFourBtn = (Button) mLayout.findViewById(R.id.calculator_btn4);
        mNumberFiveBtn = (Button) mLayout.findViewById(R.id.calculator_btn5);
        mNumberSixBtn = (Button) mLayout.findViewById(R.id.calculator_btn6);
        mAddBtn = (Button) mLayout.findViewById(R.id.calculator_add_btn);

        mNumberOneBtn = (Button) mLayout.findViewById(R.id.calculator_btn1);
        mNumberTwoBtn = (Button) mLayout.findViewById(R.id.calculator_btn2);
        mNumberThreeBtn = (Button) mLayout.findViewById(R.id.calculator_btn3);
        mReduceBtn = (Button) mLayout.findViewById(R.id.calculator_reduce_btn);

        mNumberZeroBtn = (Button) mLayout.findViewById(R.id.calculator_btn0);
        mPointBtn = (Button) mLayout.findViewById(R.id.calculator_point_btn);
        mResultBtn = (Button) mLayout.findViewById(R.id.calculator_result_btn);
        mNumText = (TextView) mLayout.findViewById(num);
        mNumberSevenBtn.setOnClickListener(this);
        mNumberEightBtn.setOnClickListener(this);
        mNumberNineBtn.setOnClickListener(this);
        mDeleteBtn.setOnClickListener(this);

        mNumberFourBtn.setOnClickListener(this);
        mNumberFiveBtn.setOnClickListener(this);
        mNumberSixBtn.setOnClickListener(this);
        mAddBtn.setOnClickListener(this);

        mNumberOneBtn.setOnClickListener(this);
        mNumberTwoBtn.setOnClickListener(this);
        mNumberThreeBtn.setOnClickListener(this);
        mReduceBtn.setOnClickListener(this);

        mNumberZeroBtn.setOnClickListener(this);
        mPointBtn.setOnClickListener(this);
        mResultBtn.setOnClickListener(this);
    }


    public static void setNumText(String numText) {
        CaculatorLayout.numText = numText;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu.setText(beizhu);
    }

    public void setShowNumText(String shownumtext) {
        this.shownumtext = shownumtext;
    }

    public void setCompleteNumtext(String completenumtext) {
        this.completenumtext = completenumtext;
    }


    @Override
    public void onClick(View view) {
        String numtemp = "";
        String signtemp = "";
        switch (view.getId()) {
            case R.id.calculator_btn7:

                if (completenumtext.equals(INPUT_0)) {
                    completenumtext = INPUT_7;
                } else {
                    completenumtext = completenumtext + INPUT_7;
                }
                if (showNumber(completenumtext)) {
                    shownumtext = completenumtext;
                }
                break;
            case R.id.calculator_btn8:
                if (completenumtext.equals(INPUT_0)) {
                    completenumtext = INPUT_8;
                } else {
                    completenumtext = completenumtext + INPUT_8;
                }
                if (showNumber(completenumtext)) {
                    shownumtext = completenumtext;
                }
                break;
            case R.id.calculator_btn9:
                if (completenumtext.equals(INPUT_0)) {
                    completenumtext = INPUT_9;
                } else {
                    completenumtext = completenumtext + INPUT_9;
                }
                if (showNumber(completenumtext)) {
                    shownumtext = completenumtext;
                }
                break;
            case R.id.calculator_btn6:
                if (completenumtext.equals(INPUT_0)) {
                    completenumtext = INPUT_6;
                } else {
                    completenumtext = completenumtext + INPUT_6;
                }
                if (showNumber(completenumtext)) {
                    shownumtext = completenumtext;
                }
                break;
            case R.id.calculator_btn5:
                if (completenumtext.equals(INPUT_0)) {
                    completenumtext = INPUT_5;
                } else {
                    completenumtext = completenumtext + INPUT_5;
                }
                if (showNumber(completenumtext)) {
                    shownumtext = completenumtext;
                }
                break;
            case R.id.calculator_btn4:
                if (completenumtext.equals(INPUT_0)) {
                    completenumtext = INPUT_4;
                } else {
                    completenumtext = completenumtext + INPUT_4;
                }
                if (showNumber(completenumtext)) {
                    shownumtext = completenumtext;
                }
                break;
            case R.id.calculator_btn3:
                if (completenumtext.equals(INPUT_0)) {
                    completenumtext = INPUT_3;
                } else {
                    completenumtext = completenumtext + INPUT_3;
                }
                if (showNumber(completenumtext)) {
                    shownumtext = completenumtext;
                }
                break;
            case R.id.calculator_btn2:
                if (completenumtext.equals(INPUT_0)) {
                    completenumtext = INPUT_2;
                } else {
                    completenumtext = completenumtext + INPUT_2;
                }
                if (showNumber(completenumtext)) {
                    shownumtext = completenumtext;
                }
                break;
            case R.id.calculator_btn1:
                if (completenumtext.equals(INPUT_0)) {
                    completenumtext = INPUT_1;
                } else {
                    completenumtext = completenumtext + INPUT_1;
                }
                if (showNumber(completenumtext)) {
                    shownumtext = completenumtext;
                }
                break;
            case R.id.calculator_btn0:
                if (completenumtext.equals(INPUT_0)) {
                    completenumtext = INPUT_0;
                } else {
                    completenumtext = completenumtext + INPUT_0;
                }
                if (showNumber(completenumtext)) {
                    shownumtext = completenumtext;
                }
                break;

            case R.id.calculator_point_btn:
                completenumtext = completenumtext + INPUT_POINT;
                if (showNumber(completenumtext)) {
                    shownumtext = completenumtext;
                }

                break;
            case R.id.calculator_result_btn:
                if (!signStack.empty() && !shownumtext.equals(INPUT_0)) {
                    textStack.push(shownumtext);
                    if (textStack.size() == 2) {
                        String firstNum = textStack.pop();
                        String signtext = signStack.pop();
                        String secondNum = textStack.pop();
                        String result = calc(firstNum, signtext, secondNum);
                        mNumText.setText(result);
                        completenumtext = result;
                        shownumtext = result;
                        mResultBtn.setText(COMPLETES);
                    }

                } else {

                    String sumStr = shownumtext;
                    double sumDouble = Double.parseDouble(sumStr);
//                    if (sumDouble <= 0) {
//                        return;
//                    }

                    sum.complete();

                }
                return;
            case R.id.calculator_add_btn:

                showAndCalc(INPUT_ADD);

                return;
            case R.id.calculator_reduce_btn:

                showAndCalc(INPUT_SUB);

                return;

            case R.id.calculator_delete:

                String all = mNumText.getText().toString();
                cut(all);

                return;


        }


        if (!signStack.empty() && !shownumtext.equals(INPUT_0)) {
            mResultBtn.setText(EQUAL);
        } else {
            mResultBtn.setText(COMPLETES);
        }


        if (signStack.empty()) {
            mNumText.setText(shownumtext);
        } else {
            numtemp = textStack.peek();
            signtemp = signStack.peek();
            mNumText.setText(numtemp + signtemp + shownumtext);
        }

    }

    private void cut(String all) {


        if (signStack.empty() || shownumtext.equals(INPUT_0)) {
            mResultBtn.setText(COMPLETES);
        }


        if (all.equals(INPUT_0) || all.equals("") || all.length() == 1 || all == null) {
            shownumtext = INPUT_0;
            completenumtext = INPUT_0;
            mNumText.setText(INPUT_0);
            return;
        }


        System.out.println(all);

        if (!all.endsWith(INPUT_ADD) && !all.endsWith(INPUT_SUB)) {
            all = all.substring(0, all.length() - 1);


            shownumtext = shownumtext.substring(0, shownumtext.length() - 1);
            completenumtext = shownumtext;

            if (all.endsWith(INPUT_ADD) || all.endsWith(INPUT_SUB)) {

                shownumtext = INPUT_0;
                completenumtext = INPUT_0;

            }
            mNumText.setText(all);
        } else {
            signStack.pop();
            all = all.substring(0, all.length() - 1);
            shownumtext = textStack.pop();
            completenumtext = shownumtext;
            mNumText.setText(all);
        }

    }


    private void showAndCalc(String sign) {

        if (signStack.empty()) {
            String numberText = shownumtext;
            textStack.push(numberText);
            signStack.push(sign);
            mNumText.setText(numberText + sign);
            completenumtext = INPUT_0;
            shownumtext = INPUT_0;
        } else {
            textStack.push(shownumtext);
            if (textStack.size() == 2) {
                String firstNum = textStack.pop();
                String signtext = signStack.pop();
                signStack.push(sign);
                String secondNum = textStack.pop();
                String result = calc(firstNum, signtext, secondNum);
                textStack.push(result);
                mNumText.setText(result + sign);
                completenumtext = INPUT_0;
                shownumtext = INPUT_0;
            } else {
                if (!signStack.peek().equals(sign)) {
                    signStack.pop();
                    signStack.push(sign);
                    String num = textStack.peek();
                    mNumText.setText(num + sign);
                    completenumtext = INPUT_0;
                    shownumtext = INPUT_0;
                }
            }
        }
    }

    private String calc(String firstNum, String sign, String secondNum) {
        String result = "";
        BigDecimal first = new BigDecimal(firstNum);
        BigDecimal second = new BigDecimal(secondNum);
        double sum = 0;
        switch (sign) {
            case INPUT_ADD:

//               sum  = first.add(second).doubleValue();
//                result = String.valueOf(sum);

                result = formatFloatNumber(first.add(second).doubleValue());


                break;
            case INPUT_SUB:
//                sum = second.subtract(first).doubleValue();
//                result = String.valueOf(sum);
                result = formatFloatNumber(second.subtract(first).doubleValue());
                break;
        }


        if (result.endsWith(".0") || result.endsWith(".00")) {

            int length = result.split("\\.")[1].length() + 1;

            result = result.substring(0, result.length() - length);
        }

        if (result.contains(".")) {

            if (result.endsWith("0")) {
                result = result.substring(0, result.length() - 1);
            }
        }


        return result;
    }

    /**
     * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
     *
     * @param value
     * @return Sting
     */
    private static String formatFloatNumber(double value) {
        if (value != 0.00) {
            java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
            return df.format(value);
        } else {
            return "0.00";
        }

    }


    private boolean showNumber(String num) {


        //如果第一个字符是符号返回false不让显示
        if (num.equals("0-")) {
            completenumtext = shownumtext;
            return false;
        }


        //如果输入这个字符后含有两个小数点不显示
        if (hasTwopoint(num)) {
            completenumtext = shownumtext;
            return false;
        }

        //如果小数点后位数大于两位不显示
        if (num.contains(INPUT_POINT)) {
            if (!liangweixiaoshu(num)) {
                completenumtext = shownumtext;
                return false;
            }
        }


        //最多输入8位整数
        if (!num.contains(".")) {
            if (num.length() > 8) {
                completenumtext = shownumtext;
            }
        }


        return true;
    }


    private boolean liangweixiaoshu(String number) {

        String b[] = number.split("\\.");
        if (b.length < 2) {
            return true;
        }

        if (b[1].length() < 3) {
            return true;
        }

        return false;
    }


    private boolean hasTwopoint(String number) {

        char c = '.';
        int num = 0;
        char[] chars = number.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (c == chars[i]) {
                num++;
                if (num > 1) {
                    return true;
                }
            }
        }
        return false;
    }


    public interface Sum {
        void complete();
    }

    public void setSum(Sum sum) {
        this.sum = sum;
    }
}
