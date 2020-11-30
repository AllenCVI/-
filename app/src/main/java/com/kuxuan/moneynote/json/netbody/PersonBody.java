package com.kuxuan.moneynote.json.netbody;

/**
 * @author HFRX hfrx1314@qq.com
 * @version 1.0.0
 */
public class PersonBody {
    private String username;
    private String gender;
    private String avatar;


    public PersonBody(String username, String gender) {
        this.username = username;
        this.gender = gender;
        this.avatar = avatar;

    }

    public PersonBody(String avatar) {
        this.avatar = avatar;

    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
