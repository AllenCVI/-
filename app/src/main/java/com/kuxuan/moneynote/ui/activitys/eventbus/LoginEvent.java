package com.kuxuan.moneynote.ui.activitys.eventbus;

/**
 * Created by xieshengqi on 2017/10/30.
 */

public class LoginEvent {

    boolean newUser;


    public LoginEvent(){}

    public LoginEvent(boolean newUser) {
        this.newUser = newUser;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }
}
