package com.kuxuan.moneynote.ui.weight;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.utils.DisplayUtil;

/**
 * Created by Allence on 2018/3/6 0006.
 */

public class MyLoadingView {

    static AlertDialog alertDialog;
    static AlertDialog alertDialog1;
    static AlertDialog alertDialog2;

    public static void showLoadingView(Activity context){

        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        WindowManager.LayoutParams  lp= alertDialog.getWindow().getAttributes();

        int width = DisplayUtil.dip2px(224);
        int height = DisplayUtil.dip2px(106);

        lp.width=width;//定义宽度
        lp.height=height;//定义高度
        alertDialog.getWindow().setAttributes(lp);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = alertDialog.getWindow();
        View view = LayoutInflater.from(context).inflate(R.layout.loadingview,null);
        window.setContentView(view);
        darkenBackground(context,0.95f);


    }


    /**
     * 改变背景颜色
     */
    private static void darkenBackground(final Activity context, final Float bgcolor) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    WindowManager.LayoutParams lp = context.getWindow().getAttributes();
                    lp.alpha = bgcolor;
                    context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    context.getWindow().setAttributes(lp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void dialogDismiss(){

        if(alertDialog!=null&&alertDialog.isShowing()){
            alertDialog.dismiss();
        }

        if(alertDialog1!=null&&alertDialog1.isShowing()){
            alertDialog1.dismiss();
        }


        if(alertDialog2!=null&&alertDialog2.isShowing()){
            alertDialog2.dismiss();
        }


    }


    public static void showSuccessView(Activity context){

        alertDialog1 = new AlertDialog.Builder(context).create();
        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.show();
        WindowManager.LayoutParams  lp= alertDialog1.getWindow().getAttributes();

        int width = DisplayUtil.dip2px(224);
        int height = DisplayUtil.dip2px(106);

        lp.width=width;//定义宽度
        lp.height=height;//定义高度
        alertDialog1.getWindow().setAttributes(lp);
        alertDialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = alertDialog1.getWindow();
        View view = LayoutInflater.from(context).inflate(R.layout.success,null);
        window.setContentView(view);
        darkenBackground(context,0.95f);
        cancelDelay(alertDialog1);

    }


    public static void showEmailErr(Activity context){

        alertDialog2 = new AlertDialog.Builder(context).create();
        alertDialog2.setCanceledOnTouchOutside(false);
        alertDialog2.show();
        WindowManager.LayoutParams  lp= alertDialog2.getWindow().getAttributes();

        int width = DisplayUtil.dip2px(188);
        int height = DisplayUtil.dip2px(60);

        lp.width=width;//定义宽度
        lp.height=height;//定义高度
        alertDialog2.getWindow().setAttributes(lp);
        alertDialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = alertDialog2.getWindow();
        View view = LayoutInflater.from(context).inflate(R.layout.success,null);
        TextView tv_sub = view.findViewById(R.id.tv_sub);
        tv_sub.setText("您输入的邮箱不合法");
        window.setContentView(view);
        darkenBackground(context,0.95f);
        cancelDelay(alertDialog2);
    }

   private static Handler handler = new Handler(){

       @Override
       public void handleMessage(Message msg) {
           dialogDismiss();
       }
   };

    private static void cancelDelay(AlertDialog alertDialog){
        handler.sendEmptyMessageDelayed(0,1500);
    }



}
