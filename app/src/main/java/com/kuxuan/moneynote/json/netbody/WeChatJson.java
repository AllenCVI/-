package com.kuxuan.moneynote.json.netbody;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class WeChatJson {
    private String openid;
    private String nickname;
    private String sex;
    private String headimgurl;
    private String unionid;


    public WeChatJson(String openid, String nickname, String sex, String headimgurl,String unionid) {
        this.openid = openid;
        this.nickname = nickname;
        this.sex = sex;
        this.headimgurl = headimgurl;
        this.unionid = unionid;
    }
}
