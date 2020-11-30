package com.kuxuan.sqlite.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Allence on 2018/3/30 0030.
 */
@Entity
public class BillCategoreDB {
    @Unique
    private long  id;
    private int category_type;
    private String icon;
    private String selected_icon;
    private String detail_icon;
    private String name;
    private int type;
    @Transient
    private boolean click;




    @Generated(hash = 1251638153)
    public BillCategoreDB(long id, int category_type, String icon,
            String selected_icon, String detail_icon, String name, int type) {
        this.id = id;
        this.category_type = category_type;
        this.icon = icon;
        this.selected_icon = selected_icon;
        this.detail_icon = detail_icon;
        this.name = name;
        this.type = type;
    }
    @Generated(hash = 10525810)
    public BillCategoreDB() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getCategory_type() {
        return this.category_type;
    }
    public void setCategory_type(int category_type) {
        this.category_type = category_type;
    }
    public String getIcon() {
        return this.icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getSelected_icon() {
        return this.selected_icon;
    }
    public void setSelected_icon(String selected_icon) {
        this.selected_icon = selected_icon;
    }
    public String getDetail_icon() {
        return this.detail_icon;
    }
    public void setDetail_icon(String detail_icon) {
        this.detail_icon = detail_icon;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }
}
