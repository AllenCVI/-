package com.kuxuan.moneynote.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * UI工具类
 */
public class UIHelper {
    public static void openActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls); //
        ctx.startActivity(intent);
    }

    public static void openActivityWithIntentForResult(Activity ctx, Class<?> cls,Intent intent,int request_code) {
        intent.setClass(ctx,cls);
        ctx.startActivityForResult(intent,request_code);
    }
    public static void openActivityWithBundle(Context ctx, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(ctx, cls);
        intent.putExtras(bundle);
        ctx.startActivity(intent);
    }

    public static void openActivityForResult(Activity ctx, Class<?> cls, int requestCode) {
        Intent intent = new Intent(ctx, cls); //
        ctx.startActivityForResult(intent, requestCode);
    }

    public static void openActivityWithBundleForResult(Activity ctx, Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(ctx, cls);
        intent.putExtras(bundle);
        ctx.startActivityForResult(intent, requestCode);
    }



}
