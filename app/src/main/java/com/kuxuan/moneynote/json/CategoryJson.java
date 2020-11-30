package com.kuxuan.moneynote.json;

import java.util.List;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class CategoryJson {
    private List<CategoryList> system;
    private List<CategoryList> custom;
    private List<CategoryList> removed_system;


    public List<CategoryList> getRemoved_system() {
        return removed_system;
    }

    public void setRemoved_system(List<CategoryList> removed_system) {
        this.removed_system = removed_system;
    }

    public List<CategoryList> getSystem() {
        return system;
    }

    public void setSystem(List<CategoryList> system) {
        this.system = system;
    }

    public List<CategoryList> getCustom() {
        return custom;
    }

    public void setCustom(List<CategoryList> custom) {
        this.custom = custom;
    }
}
