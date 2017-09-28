package com.thefunteam.android;

import com.google.gson.Gson;

public class NextLayerFacade {
    private static NextLayerFacade ourInstance = new NextLayerFacade();

    public static NextLayerFacade getInstance() { return ourInstance; }

    private NextLayerFacade() { }

    void login(String username, String password) {
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/login",
                gson.toJson(new Login(username, password))
        );
    }

    void register(String username, String password) {
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/register",
                gson.toJson(new Login(username, password))
        );
    }

    void joinGame(String gameId) {

    }

    void createGame() {

    }

    void startGame() {

    }

    void leaveGame() {

    }

}
