package com.thefunteam.android;

public class RegistrationPresenter {

    void register(String username, String password) {
        NextLayerFacade.getInstance().register(username, password);
    }
}
