package com.kuxuan.moneynote.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.kuxuan.moneynote.ui.weight.MyToast;


/**
 * Created by xieshengqi on 17/2/21.
 */

public class ToastUtil {
    public static void show(Context context, String text) {
//        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        MyToast.makeTextGravity(context, text, Toast.LENGTH_SHORT, Gravity.BOTTOM).show();
    }

    public static void showDeBugMessage(String err) {
    }

    public static void err(Context context, int code, String msg) {
        Toast.makeText(context, code + ":" + msg, Toast.LENGTH_SHORT).show();
    }

//    public static Observable<String> showSnackbar(View view, String string, String action) {
//        return Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                Snackbar.make(view, string, Snackbar.LENGTH_INDEFINITE)
//                        .setAction(action, view1 -> {
//                            e.onNext(action);
//                            e.onComplete();
//                        }).show();
//            }
//        });
}