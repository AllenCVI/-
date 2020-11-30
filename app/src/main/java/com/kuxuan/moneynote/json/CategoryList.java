package com.kuxuan.moneynote.json;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class CategoryList  {
    private String id;
    private String name;
    private int type;
    private String icon;
    private int types;
    private boolean isClick;

    public CategoryList(String id, String name, int type, String icon, int types) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.icon = icon;
        this.types = types;
    }

    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
