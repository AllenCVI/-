package com.kuxuan.moneynote.json.netbody;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class MessageBody {
    private String message;
    private String user_contact;

    public MessageBody(String message) {
        this.message = message;
        this.user_contact = "s";
    }
}
