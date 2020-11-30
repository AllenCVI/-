package com.kuxuan.moneynote.json;

/**
 * Created by xieshengqi on 2018/4/16.
 */

public class BillTypeJson {
    private long  id;
    private int category_type;
    private String icon;
    private String selected_icon;
    private String detail_icon;
    private String name;
    private int type;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCategory_type() {
        return category_type;
    }

    public void setCategory_type(int category_type) {
        this.category_type = category_type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSelected_icon() {
        return selected_icon;
    }

    public void setSelected_icon(String selected_icon) {
        this.selected_icon = selected_icon;
    }

    public String getDetail_icon() {
        return detail_icon;
    }

    public void setDetail_icon(String detail_icon) {
        this.detail_icon = detail_icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
