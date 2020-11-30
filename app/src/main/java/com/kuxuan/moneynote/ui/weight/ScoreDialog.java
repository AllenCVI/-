package com.kuxuan.moneynote.ui.weight;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.utils.DisplayUtil;
import com.kuxuan.moneynote.utils.TextParser;


/**
 * 签到成功积分弹出框（首页）
 * Created by xieshengqi on 2018/4/23.
 */

public class ScoreDialog extends Dialog {

    private ImageView close_img;
    private TextView textView;

    public ScoreDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_score_success, null);
        close_img = (ImageView) view.findViewById(R.id.dialog_score_delete);
        textView = view.findViewById(R.id.dialog_score_textview);
        setTextData("1");
        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setContentView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }


    public void setTextData(String count) {
        TextParser textParser = new TextParser();
        textParser.append("恭喜您签到成功\n", DisplayUtil.dip2px(14), Color.parseColor("#262626"));
        textParser.append("积分+" + count, DisplayUtil.dip2px(15), Color.parseColor("#FC3507"));
        textParser.parse(textView);
    }
}
