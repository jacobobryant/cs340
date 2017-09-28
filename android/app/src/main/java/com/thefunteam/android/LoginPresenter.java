package com.thefunteam.android;

public class LoginPresenter {


    void login(String username, String password) {
        NextLayerFacade.getInstance().login(username, password);
    }
}
