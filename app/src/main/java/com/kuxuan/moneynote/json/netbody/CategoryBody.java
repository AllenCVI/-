package com.kuxuan.moneynote.json.netbody;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class CategoryBody {
    private String category_id;
    private int category_type;

    public CategoryBody(String category_id, int category_type) {
        this.category_id = category_id;
        this.category_type = category_type;
    }


    public CategoryBody(String category_id) {
        this.category_id = category_id;
    }
}
