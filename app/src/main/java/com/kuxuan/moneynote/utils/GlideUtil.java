package com.kuxuan.moneynote.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kuxuan.moneynote.R;
import com.kuxuan.moneynote.ui.weight.GlideCircleTransform;

import java.io.File;

/**
 * Created by xieshengqi on 2017/10/30.
 */

public class GlideUtil {

    public static void setImageWithNoCache(Context context,String url,ImageView imageView){
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.category_custom_selected).error(R.mipmap.category_custom_selected).into(imageView);
    }
    public static void setImageWithCirlce(Context context,String url,ImageView imageView){
        Glide.with(context).load(url).transform(new GlideCircleTransform(context)).placeholder(R.mipmap.category_custom_selected).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.mipmap.category_custom_selected).into(imageView);
    }

    public static void setImageWithNoCache(Context context, File file, ImageView imageView){
        Glide.with(context).load(file).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.mipmap.category_custom_selected).into(imageView);
    }

    public static void setImageWithNoCache(Context context,String url,ImageView imageView,boolean placeholder){
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.mipmap.category_custom_selected).into(imageView);
    }


    public static void setImageWithCache(Context context,String url,ImageView imageView){
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }
}
