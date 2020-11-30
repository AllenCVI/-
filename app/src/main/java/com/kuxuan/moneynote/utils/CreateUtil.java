package com.kuxuan.moneynote.utils;

import java.lang.reflect.ParameterizedType;

/**
 * Created by xieshengqi on 2017/7/6.
 */
public class CreateUtil {

    public static <T> T getT(Object o, int i) {
        try {

            //o.getClass()获取当前类getGenericSuperclass()获取带泛型的父类getActualTypeArguments（）获取类的泛型
            return ((Class<T>) ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments()[i]).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}