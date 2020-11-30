package com.kuxuan.moneynote.db;

import com.kuxuan.sqlite.db.BillCategoreDB;

import java.util.List;

/**
 * Created by Allence on 2018/4/2 0002.
 */

public class CategoryDBJson {

    private List<BillCategoreDB> system;

    public List<BillCategoreDB> getSystem() {
        return system;
    }

    public void setSystem(List<BillCategoreDB> system) {
        this.system = system;
    }
}
