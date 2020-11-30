package com.kuxuan.moneynote.json.netbody;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class BillBody {
    private String category_id;
    private int type;
    private String account;
    private String time;
    private String demo;


    private String bill_id;

    public BillBody(String category_id, int type, String account, String time, String demo,String bill_id) {
        this.category_id = category_id;
        this.type = type;
        this.account = account;
        this.time = time;
        this.demo = demo;
        this.bill_id = bill_id;
    }


    public BillBody(String category_id, int type, String account, String time, String demo) {
        this.category_id = category_id;
        this.type = type;
        this.account = account;
        this.time = time;
        this.demo = demo;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }




}
