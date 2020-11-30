package com.kuxuan.moneynote.utils;

import com.kuxuan.moneynote.MyApplication;
import com.kuxuan.moneynote.common.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Allence on 2018/4/26 0026.
 */

public class SkinUtil {


    /**
     * 记录哪个图片已经下载了
     * @param position
     */

    public static void markDownload(int position){

        String loaded = (String) SPUtil.get(MyApplication.getInstance().getApplicationContext(), Constant.Skin.LOADED,"");

        String downLoaded = loaded+position+"*";

        SPUtil.putAndApply(MyApplication.getInstance().getApplicationContext(),Constant.Skin.LOADED,downLoaded);

    }


    /**
     * 给出已经下载的图片列表
     */

    public static List<Integer> getDownloadPicList(){

        String loaded = (String) SPUtil.get(MyApplication.getInstance().getApplicationContext(), Constant.Skin.LOADED,"");

        List<Integer> integerList = new ArrayList();

        String[] loadedArr = loaded.split("\\*");

        if(!loaded.equals("")){
        for (int i=0;i<loadedArr.length;i++){
            integerList.add(Integer.parseInt(loadedArr[i]));
        }


            Collections.sort(integerList);
        }

        return integerList;
    }







}
