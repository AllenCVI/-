package com.kuxuan.moneynote.json.netbody;

import java.util.List;

/**
 * Created by Allence on 2018/3/27 0027.
 */

public class ExportBill {

    List<String> success;
    int code;

    public List<String> getSuccess() {
        return success;
    }

    public void setSuccess(List<String> success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
