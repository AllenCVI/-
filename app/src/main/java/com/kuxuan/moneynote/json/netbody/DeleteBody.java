package com.kuxuan.moneynote.json.netbody;

/**
 * Created by xieshengqi on 2017/10/25.
 */

public class DeleteBody {

    private String bill_id;


    public DeleteBody(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }
}
