package com.thefunteam.android;

public class RegistrationPresenter {
//lol
    void register(String username, String password) {
        NextLayerFacade.getInstance().register(username, password);
    }
}
