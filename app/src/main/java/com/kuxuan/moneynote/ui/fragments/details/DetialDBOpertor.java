package com.kuxuan.moneynote.ui.fragments.details;

import com.kuxuan.moneynote.db.CategoryDaoOperator;
import com.kuxuan.moneynote.json.BillData;
import com.kuxuan.moneynote.json.TypeDataJson;
import com.kuxuan.moneynote.json.UserAllBillJson;
import com.kuxuan.moneynote.json.YearData;
import com.kuxuan.moneynote.utils.CalanderUtil;
import com.kuxuan.sqlite.db.CategoryDB;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xieshengqi on 2018/3/30.
 */

public class DetialDBOpertor {


    private static DetialDBOpertor mInstance;

    private CategoryDaoOperator categoryDaoOperator;

    private DetialDBOpertor() {
        categoryDaoOperator = CategoryDaoOperator.newInstance();
    }

    public static DetialDBOpertor getInstance() {
        if (mInstance == null) {
            synchronized (DetialDBOpertor.class) {
                mInstance = new DetialDBOpertor();
            }
        }
        return mInstance;
    }

    /**
     * 获取明细页数据库数据
     *
     * @param year
     * @param month
     * @param maps
     * @return
     */
    public UserAllBillJson getUserBillJson(int year, int month, HashMap<String, Integer> maps) {

        UserAllBillJson userAllBillJson = new UserAllBillJson();
        if(maps==null){
            return  userAllBillJson;
        }
        int monthDay = CalanderUtil.getMonthDay(year, month);
        ArrayList<BillData> bill_datas = new ArrayList<>();
        for (int i = monthDay; i > 0; i--) {
            BillData billData = getBillData(year, month, i);
            if (billData != null) {
                bill_datas.add(billData);
            }
        }
        double pay_account = 0;
        double inCome_account = 0;
        for (BillData typeDataJson : bill_datas) {
            inCome_account += typeDataJson.getDay_income_account();
            pay_account += typeDataJson.getDay_pay_account();
        }
        userAllBillJson.setIncome_account(inCome_account + "");
        userAllBillJson.setPay_account(pay_account + "");
        userAllBillJson.setBill_data(bill_datas);
        YearData startData = new YearData(maps.get("startYear"), maps.get("startMonth"));
        YearData endData = new YearData(maps.get("endYear"), maps.get("endMonth"));
        userAllBillJson.setStart_data(startData);
        userAllBillJson.setEnd_data(endData);


        return userAllBillJson;
    }


    private BillData getBillData(int year, int month, int day) {
        BillData billData = null;
        ArrayList<CategoryDB> categoryForDay = categoryDaoOperator.getCategoryForDay(year, month, day);
        ArrayList<TypeDataJson> day_datas = changJson(categoryForDay);

        if (day_datas == null || day_datas.size() == 0) {
            return null;
        } else {
            billData = new BillData();
        }
        double pay_account = 0;
        double inCome_account = 0;
        for (TypeDataJson typeDataJson : day_datas) {
            if (typeDataJson.getType() == 1) {
                //收入
                inCome_account += Double.parseDouble(typeDataJson.getAccount());
            } else {
                //支出
                pay_account += Double.parseDouble(typeDataJson.getAccount());
            }
        }
        billData.setDay_income_account(inCome_account);
        billData.setDay_pay_account( pay_account);
        billData.setTime(month + "月" + day + "日 " + getDay(year + "-" + month + "-" + day));
        billData.setDay_data(day_datas);
        return billData;
    }

    private String getDay(String data) {
        String string = null;
        switch (CalanderUtil.getDayForWeek(data)) {
            case 1:
                string = "星期一";
                break;
            case 2:
                string = "星期二";
                break;
            case 3:
                string = "星期三";
                break;
            case 4:
                string = "星期四";
                break;
            case 5:
                string = "星期五";
                break;
            case 6:
                string = "星期六";
                break;
            case 7:
                string = "星期日";
                break;
            default:
                string = "星期一";
        }
        return string;
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
//            java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
            typeDataJson.setAccount(list.get(i).getAccount() + "");
            typeDataJson.setName(list.get(i).getName());
            typeDataJson.setDemo(list.get(i).getDemo());
            datas.add(typeDataJson);
        }
        return datas;
    }

}
