package com.kuxuan.moneynote.ui.activitys.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.kuxuan.moneynote.base.mvpbase.BaseModel;
import com.kuxuan.moneynote.base.mvpbase.BasePresent;
import com.kuxuan.moneynote.base.mvpbase.BaseView;
import com.kuxuan.moneynote.json.CategoryJson;
import com.kuxuan.moneynote.listener.MVPListener;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public interface CategoryContract {
    interface  CategoryView extends BaseView {

        /**
         * 判断更多类别是否为空
         * @param flag
         */
        void getNullOrNot(boolean flag);

        /**
         * 判断是否需要弹窗
         * @param flag
         * @param position
         * @param type
         */
        void checkDialog(boolean flag,int position,int type);

    }



    interface CategoryModel extends BaseModel {
        /**
         * 获取类别列表
         */
        void getCategoryList(MVPListener<CategoryJson> listMVPListener,String type);

        /**
         * 移除记账类别接口
         * @param listMVPListener
         * @param category_id id
          * @param category_type 类型
         */
        void removeCategory(MVPListener<Object> listMVPListener,String category_id,int category_type,String name ,String type);

        /**
         * 添加系统类别接口
         * @param listMVPListener   listMVPListener
         * @param category_id       id
         */
        void addCategory(MVPListener<Object> listMVPListener,String category_id);

        /**
         * 添加自定义类别接口
         * @param listMVPListener   listMVPListener
         * @param name       name
         * @param type       type
         */
        void addCustomCategory(MVPListener<Object> listMVPListener,String name,String type);


        /**
         * 检测类别下是否有账单
         * @param listMVPListener   listMVPListener
         */
        void checkBillCategory(MVPListener<Boolean> listMVPListener,int type,String category_id);
    }


    abstract  class  CategoryPresent extends BasePresent<CategoryModel,CategoryView> {
        /**
         * 获取类别列表
         * @param type  支出/收入
         */
        abstract void getCategoryList(String type);

        /**
         * 设置recyclerSystem
         * @param context
         * @param recyclerView
         */
        abstract void initRecyclerRemove(Context context, RecyclerView recyclerView);

        /**
         * 设置recyclerAdd
         * @param context
         * @param recyclerView
         */
        abstract void initRecyclerAdd(Context context, RecyclerView recyclerView);

        /**
         * 删除类别
         * @param position
         * @param customtype
         */
        abstract void deleteCategory(int position,int customtype,boolean needDeleteDb);

        /**
         * 增加商品类别
         * @param position        position
         */
        abstract void addCategory(int position);

        /**
         * 增加自定义类别
         * @param name        name
         * @param type        type
         */
        abstract void addCustomCategory(String name,String type);


        /**
         * 检测dialog
         * @param categoryId
         * @param type
         * @param position
         */
        abstract void checkDialog(String categoryId,int type,int position);



    }
}
