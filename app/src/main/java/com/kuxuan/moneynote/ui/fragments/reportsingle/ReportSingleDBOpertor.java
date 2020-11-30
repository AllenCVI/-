package com.kuxuan.moneynote.ui.fragments.reportsingle;

import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.NewCategoryJson;
import com.kuxuan.moneynote.json.NewChartData;
import com.kuxuan.moneynote.json.PopCharData;
import com.kuxuan.moneynote.json.TimeDataJson;
import com.kuxuan.moneynote.json.TypeDataJson;
import com.kuxuan.moneynote.utils.CalanderUtil;
import com.kuxuan.sqlite.db.CategoryDB;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 离线记账操作类
 * Created by xieshengqi on 2018/3/29.
 */

public class ReportSingleDBOpertor {

    private static ReportSingleDBOpertor mInstance;

    private ReportSingleDBOpertor() {

    }

    public static ReportSingleDBOpertor getInstance() {
        if (mInstance == null) {
            synchronized (ReportSingleDBOpertor.class) {
                mInstance = new ReportSingleDBOpertor();
            }
        }
        return mInstance;
    }

    /**
     * 获取周的所有时间段
     *
     * @param maps
     * @return
     */
    public ArrayList<NewChartData> getWeekTabData(HashMap<String, Integer> maps, int type) {
        if (maps == null) {
            return null;
        }
        int startYear = maps.get("startYear");
        int startMonth = maps.get("startMonth");
        int startDay = maps.get("startDay");
        int endYear = maps.get("endYear");
        int endMonth = maps.get("endMonth");
        int endDay = maps.get("endDay");
        ArrayList<NewChartData> data = new ArrayList<>();
        ArrayList<String> weekTab = CalanderUtil.getWeekTab(startYear, startMonth, startDay, endYear, endMonth, endDay);
        for (int i = 0; i < weekTab.size(); i++) {
            String da = weekTab.get(i);
            NewChartData newChartData = new NewChartData();
            newChartData.setKey(da);
            int year = 0;
            int weekCount = 0;
            try {
                String[] split = da.split("-");
                year = Integer.parseInt(split[0]);
                weekCount = Integer.parseInt(split[1].substring(0, split[1].length() - 1));

            } catch (Exception e) {
                year = CalanderUtil.getCurrentYear();
                weekCount = Integer.parseInt(da.substring(0, da.length() - 1));
            }
            ArrayList<String> time_range = CalanderUtil.getWeekAllDay(year, weekCount);
            newChartData.setTime_range(time_range);
            newChartData.setYear(year + "");
            newChartData.setStatistic_type(type);
            data.add(newChartData);
        }
        return data;
    }


    /**
     * 获取月份所需的数据
     *
     * @param maps
     * @param type
     * @return
     */
    public ArrayList<NewChartData> getMonthTabData(HashMap<String, Integer> maps, int type) {
        int startYear = maps.get("startYear");
        int startMonth = maps.get("startMonth");
        int endYear = maps.get("endYear");
        int endMonth = maps.get("endMonth");

        ArrayList<NewChartData> dataArrayList = new ArrayList<>();
        ArrayList<String> monthTab = CalanderUtil.getMonthTab(startYear, startMonth, endYear, endMonth);
        for (int i = 0; i < monthTab.size(); i++) {
            String da = monthTab.get(i);
            NewChartData newChartData = new NewChartData();
            newChartData.setKey(da);
            int year = 0;
            int month = 0;
            try {
                String[] split = da.split("-");
                year = Integer.parseInt(split[0]);
                month = Integer.parseInt(split[1].substring(0, split[1].length() - 1));

            } catch (Exception e) {
                year = CalanderUtil.getCurrentYear();
                month = Integer.parseInt(da.substring(0, da.length() - 1));
            }
            newChartData.setMonth(month + "");
            newChartData.setYear(year + "");
            newChartData.setStatistic_type(type);
            dataArrayList.add(newChartData);
        }
        return dataArrayList;
    }


    /**
     * 获取年所需的数据
     *
     * @param maps
     * @param type
     * @return
     */
    public ArrayList<NewChartData> getYearTabData(HashMap<String, Integer> maps, int type) {
        int startYear = maps.get("startYear");
        int endYear = maps.get("endYear");

        ArrayList<NewChartData> dataArrayList = new ArrayList<>();
        ArrayList<String> monthTab = CalanderUtil.getYearTab(startYear, endYear);
        for (int i = 0; i < monthTab.size(); i++) {
            String da = monthTab.get(i);
            NewChartData newChartData = new NewChartData();
            newChartData.setKey(da);
            int year = 0;
            year = Integer.parseInt(da.substring(0, da.length() - 1));
            newChartData.setYear(year + "");
            newChartData.setStatistic_type(type);
            dataArrayList.add(newChartData);
        }
        return dataArrayList;
    }


    /**
     * 获取一个周的数据
     *
     * @param datasLists
     * @param categoryDaoOperator
     * @return
     */
    public NewCategoryJson getWeekNewCategoryJson(ArrayList<String> datasLists, CategoryDaoOperator categoryDaoOperator, int category_id, int type) {
        NewCategoryJson newCategoryJson = new NewCategoryJson();
        ArrayList<TypeDataJson> typeDataJsonLists = new ArrayList<>();
        ArrayList<TimeDataJson> timeDataJsonsLists = new ArrayList<>();
        ArrayList<TypeDataJson> detialDataLists = new ArrayList<>();
        for (int i = 0; i < datasLists.size(); i++) {
            String s = datasLists.get(i);
            String[] split = s.split("-");
            int year = Integer.parseInt(split[0]);
            int month = Integer.parseInt(split[1]);
            int day = Integer.parseInt(split[2]);
            ArrayList<CategoryDB> weekData = categoryDaoOperator.getWeekData(year, month, day, type, category_id);
            if (category_id != -1) {
                detialDataLists.addAll(changJson(weekData, category_id));
            } else {
                typeDataJsonLists.addAll(changJson(weekData));
            }
            TimeDataJson timeDataJson = getWeekTimeDataJson(year, month, day, weekData);
            if (timeDataJson != null) {
                timeDataJsonsLists.add(timeDataJson);
            }
        }
        if (category_id == -1) {
            ArrayList<TypeDataJson> categLists = getCategLists(typeDataJsonLists);
            double account = 0;
            for (TypeDataJson typeDataJson : categLists) {
                account += Double.parseDouble(typeDataJson.getAccount());
            }
            newCategoryJson.setAccount(account + "");
            newCategoryJson.setCategory(categLists);
        } else {
            double account = 0;
            for (TypeDataJson typeDataJson : detialDataLists) {
                account += Double.parseDouble(typeDataJson.getAccount());
            }
            newCategoryJson.setAccount(account + "");
            newCategoryJson.setDetial_data(detialDataLists);
        }


        newCategoryJson.setTime_data(timeDataJsonsLists);
        return newCategoryJson;
    }

    /**
     * 根据月获得报表数据
     *
     * @param year
     * @param month
     * @param categoryDaoOperator
     * @return
     */
    public NewCategoryJson getMonthNewCategoryJson(int year, int month, CategoryDaoOperator categoryDaoOperator, int type, int category_id) {

        NewCategoryJson newCategoryJson = new NewCategoryJson();
        ArrayList<TypeDataJson> typeDataJsonLists = new ArrayList<>();
        ArrayList<TimeDataJson> timeDataJsonsLists = new ArrayList<>();
        ArrayList<TypeDataJson> detialDataLists = new ArrayList<>();
        ArrayList<CategoryDB> weekData = categoryDaoOperator.getMonthData(year, month, type, category_id);
        if (category_id == -1)
            typeDataJsonLists.addAll(changJson(weekData));
        else
            detialDataLists.addAll(changJson(weekData, category_id));
        int monthDay = CalanderUtil.getMonthDay(year, month);
        for (int i = 1; i <= monthDay; i++) {
            TimeDataJson timeDataJson = getMonthTimeDataJson(year, month, i, categoryDaoOperator, category_id, type);
            if (timeDataJson != null) {
                timeDataJsonsLists.add(timeDataJson);
            }
        }
        if (category_id == -1) {
            ArrayList<TypeDataJson> categLists = getCategLists(typeDataJsonLists);
            double account = 0;
            for (TypeDataJson typeDataJson : categLists) {
                account += Double.parseDouble(typeDataJson.getAccount());
            }
            newCategoryJson.setAccount(account + "");
            newCategoryJson.setCategory(categLists);
        } else {
            double account = 0;
            for (TypeDataJson typeDataJson : detialDataLists) {
                account += Double.parseDouble(typeDataJson.getAccount());
            }
            newCategoryJson.setAccount(account + "");
            newCategoryJson.setDetial_data(detialDataLists);
        }

        newCategoryJson.setTime_data(timeDataJsonsLists);
        return newCategoryJson;
    }


    /**
     * 根据年获得报表数据
     *
     * @param year
     * @param categoryDaoOperator
     * @return
     */
    public NewCategoryJson getYearNewCategoryJson(int year, CategoryDaoOperator categoryDaoOperator, int type, int category_id) {
        NewCategoryJson newCategoryJson = new NewCategoryJson();
        ArrayList<TypeDataJson> typeDataJsonLists = new ArrayList<>();
        ArrayList<TypeDataJson> detialDataLists = new ArrayList<>();
        ArrayList<TimeDataJson> timeDataJsonsLists = new ArrayList<>();
        ArrayList<CategoryDB> weekData = categoryDaoOperator.getYearData(year, type, category_id);
        if (category_id == -1)
            typeDataJsonLists.addAll(changJson(weekData));
        else
            detialDataLists.addAll(changJson(weekData, category_id));
        for (int i = 1; i <= 12; i++) {
            TimeDataJson timeDataJson = getYearTimeDataJson(year, i, categoryDaoOperator, type, category_id);
            if (timeDataJson != null) {
                timeDataJsonsLists.add(timeDataJson);
            }
        }
        if (category_id == -1) {
            ArrayList<TypeDataJson> categLists = getCategLists(typeDataJsonLists);
            double account = 0;
            for (TypeDataJson typeDataJson : categLists) {
                account += Double.parseDouble(typeDataJson.getAccount());
            }
            newCategoryJson.setAccount(account + "");
            newCategoryJson.setCategory(categLists);
        } else {
            double account = 0;
            for (TypeDataJson typeDataJson : detialDataLists) {
                account += Double.parseDouble(typeDataJson.getAccount());
            }
            newCategoryJson.setAccount(account + "");
            newCategoryJson.setDetial_data(detialDataLists);
        }

        newCategoryJson.setTime_data(timeDataJsonsLists);
        return newCategoryJson;
    }

    /**
     * 获取周下面的pop
     *
     * @param year
     * @param month
     * @param day
     * @param list
     * @return
     */
    private TimeDataJson getWeekTimeDataJson(int year, int month, int day, ArrayList<CategoryDB> list) {

        TimeDataJson timeDataJson = new TimeDataJson();
        timeDataJson.setDay(month + "-" + day);
        ArrayList<PopCharData> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            PopCharData typeDataJson = new PopCharData();
            typeDataJson.setCategory_id(list.get(i).getCategory_id());
            typeDataJson.setAccount(list.get(i).getAccount() + "");
            typeDataJson.setName(list.get(i).getName());
            typeDataJson.setSmall_icon(list.get(i).getImage_path());
            typeDataJson.setTime(year + "/" + String.format("%02d", month) + "/" + String.format("%02d", day));
            data.add(typeDataJson);
        }
        double account = 0;
        for (PopCharData typeDataJson : data) {
            account += Double.parseDouble(typeDataJson.getAccount());
        }
        timeDataJson.setData(data);
        if (account > 0) {
            timeDataJson.setAccount( account);
            return timeDataJson;
        } else {
            return null;
        }
    }

    /**
     * 获取月下面弹窗的
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    private TimeDataJson getMonthTimeDataJson(int year, int month, int day, CategoryDaoOperator categoryDaoOperator, int category_id, int type) {
        TimeDataJson timeDataJson = new TimeDataJson();
        timeDataJson.setDay(String.format("%02d", day) + "");
        ArrayList<PopCharData> data = new ArrayList<>();
        ArrayList<CategoryDB> weekData = categoryDaoOperator.getWeekData(year, month, day, type, category_id);
        for (int i = 0; i < weekData.size(); i++) {
            PopCharData typeDataJson = new PopCharData();
            typeDataJson.setCategory_id(weekData.get(i).getCategory_id());
            typeDataJson.setAccount(weekData.get(i).getAccount() + "");
            typeDataJson.setName(weekData.get(i).getName());
            typeDataJson.setSmall_icon(weekData.get(i).getImage_path());
            typeDataJson.setTime(year + "/" + String.format("%02d", month) + "/" + String.format("%02d", day));
            data.add(typeDataJson);
        }
        double account = 0;
        for (PopCharData typeDataJson : data) {
            account += Double.parseDouble(typeDataJson.getAccount());
        }
        timeDataJson.setData(data);
        if (account > 0) {
            timeDataJson.setAccount(account);
            return timeDataJson;
        } else {
            return null;
        }
    }


    /**
     * 根据年获取需要数据
     *
     * @param year
     * @param month
     * @param categoryDaoOperator
     * @return
     */
    private TimeDataJson getYearTimeDataJson(int year, int month, CategoryDaoOperator categoryDaoOperator, int type, int category_id) {
        TimeDataJson timeDataJson = new TimeDataJson();
        timeDataJson.setDay(String.format("%02d", month) + "");
        ArrayList<PopCharData> data = new ArrayList<>();
        ArrayList<CategoryDB> monthData = categoryDaoOperator.getMonthData(year, month, type, category_id);
        for (int i = 0; i < monthData.size(); i++) {
            PopCharData typeDataJson = new PopCharData();
            typeDataJson.setCategory_id(monthData.get(i).getCategory_id());
            typeDataJson.setAccount(monthData.get(i).getAccount() + "");
            typeDataJson.setName(monthData.get(i).getName());
            typeDataJson.setSmall_icon(monthData.get(i).getImage_path());
            typeDataJson.setTime(year + "/" + String.format("%02d", month) + "/" + String.format("%02d", monthData.get(i).getDay()));
            data.add(typeDataJson);
        }
        double account = 0;
        for (PopCharData typeDataJson : data) {
            account += Double.parseDouble(typeDataJson.getAccount());
        }
        timeDataJson.setData(data);
        if (account > 0) {
            timeDataJson.setAccount( account);
            return timeDataJson;
        } else {
            return null;
        }
    }

    /**
     * 把重复类别的数据转化为单一数据
     *
     * @param dataJsons
     * @return
     */
    private ArrayList<TypeDataJson> getCategLists(ArrayList<TypeDataJson> dataJsons) {
        ArrayList<TypeDataJson> newDataLists = new ArrayList<>();
        ArrayList<Integer> cateGoryIdlists = new ArrayList<>();
        //找出所有的类别
        for (TypeDataJson typeDataJson : dataJsons) {
            if (!cateGoryIdlists.contains(typeDataJson.getCategory_id())) {
                cateGoryIdlists.add(typeDataJson.getCategory_id());
            }
        }
        for (int i = 0; i < cateGoryIdlists.size(); i++) {
            TypeDataJson t = null;
            for (TypeDataJson typeDataJson : dataJsons) {
                if (typeDataJson.getCategory_id() == cateGoryIdlists.get(i)) {
                    if (t == null) {
                        t = new TypeDataJson();
                        t.setName(typeDataJson.getName());
                        t.setCategory_id(typeDataJson.getCategory_id());
                        t.setDetail_icon(typeDataJson.getDetail_icon());
                        t.setSmall_icon(typeDataJson.getSmall_icon());
                        t.setAccount(typeDataJson.getAccount());
                        t.setType(typeDataJson.getType());
                    } else {
                        //累加总值
                        t.setAccount(Double.parseDouble(t.getAccount()) + Double.parseDouble(typeDataJson.getAccount()) + "");
                    }
                }
            }
            if (t != null)
                newDataLists.add(t);
        }
        return newDataLists;
    }

    /**
     * 把数据库中的数据转化为需要的数据
     *
     * @param list
     * @return
     */
    private ArrayList<TypeDataJson> changJson(ArrayList<CategoryDB> list) {
        ArrayList<TypeDataJson> datas = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TypeDataJson typeDataJson = new TypeDataJson();
            typeDataJson.setBill_id(list.get(i).getBill_id());
            typeDataJson.setCategory_id(list.get(i).getCategory_id());
            typeDataJson.setType(list.get(i).getType());
            typeDataJson.setDetail_icon(list.get(i).getImage_path());
            typeDataJson.setSmall_icon(list.get(i).getImage_path());
            typeDataJson.setAccount(list.get(i).getAccount() + "");
            typeDataJson.setName(list.get(i).getName());
            datas.add(typeDataJson);
        }
        return datas;
    }

    /**
     * 数据归类(最外层的数据)
     *
     * @param list
     * @return
     */
    private ArrayList<TypeDataJson> changJsonNoDetial(ArrayList<CategoryDB> list) {
        ArrayList<TypeDataJson> datas = new ArrayList<>();
        ArrayList<String> idLists = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (!isInLists(list.get(i).getCategory_id() + "", idLists)) {
                idLists.add(list.get(i).getCategory_id() + "");
            }
        }
//        for (int i = 0; i < list.size(); i++) {
//            TypeDataJson typeDataJson = new TypeDataJson();
//            typeDataJson.setBill_id(list.get(i).getBill_id());
//            typeDataJson.setCategory_id(list.get(i).getCategory_id());
//            typeDataJson.setType(list.get(i).getType());
//            typeDataJson.setDetail_icon(list.get(i).getImage_path());
//            typeDataJson.setSmall_icon(list.get(i).getImage_path());
//            typeDataJson.setName(list.get(i).getName());
//            typeDataJson.setAccount(list.get(i).getAccount() + "");
//            datas.add(typeDataJson);
//        }

        for (int i = 0; i < idLists.size(); i++) {
            TypeDataJson typeDataJson = new TypeDataJson();
            typeDataJson.setBill_id(list.get(i).getBill_id());
            typeDataJson.setCategory_id(list.get(i).getCategory_id());
            typeDataJson.setType(list.get(i).getType());
            typeDataJson.setDetail_icon(list.get(i).getImage_path());
            typeDataJson.setSmall_icon(list.get(i).getImage_path());
            typeDataJson.setName(list.get(i).getName());
            double account = 0;
            for (CategoryDB categoryDB : list) {
                if (String.valueOf(categoryDB.getCategory_id()).equals(idLists.get(i))) {
                    account += categoryDB.getAccount();
                }
            }
            typeDataJson.setAccount(account + "");
        }
        return datas;
    }


    private boolean isInLists(String id, ArrayList<String> lists) {
        boolean isIn = false;
        for (String i : lists) {
            if (i.equals(id)) {
                isIn = true;
                break;
            }
        }
        return isIn;
    }


    /**
     * 详情页本地数据转化
     *
     * @param list
     * @param category_id
     * @return
     */
    private ArrayList<TypeDataJson> changJson(ArrayList<CategoryDB> list, int category_id) {
        ArrayList<TypeDataJson> datas = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TypeDataJson typeDataJson = new TypeDataJson();
            typeDataJson.setBill_id(list.get(i).getBill_id());
            typeDataJson.setDetail_icon(list.get(i).getImage_path());
            typeDataJson.setSmall_icon(list.get(i).getImage_path());
            typeDataJson.setDay(list.get(i).getYear() + "-" + String.format("%02d", list.get(i).getMonth()) + "-" + String.format("%02d", list.get(i).getDay()));
            typeDataJson.setCategory_id(list.get(i).getCategory_id());
            typeDataJson.setType(list.get(i).getType());
            typeDataJson.setDemo(list.get(i).getDemo());
            typeDataJson.setAccount(list.get(i).getAccount() + "");
            typeDataJson.setName(list.get(i).getName());
            datas.add(typeDataJson);
        }
        return datas;
    }
}
