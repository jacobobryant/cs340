package com.thefunteam.android;

import com.google.gson.Gson;
import com.thefunteam.android.model.Login;

public class NextLayerFacade {
    private static NextLayerFacade ourInstance = new NextLayerFacade();

    public static NextLayerFacade getInstance() { return ourInstance; }

    private NextLayerFacade() { }

    public void login(String username, String password) {
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/login",
                gson.toJson(new Login(username, password))
        );
    }

    public void register(String username, String password) {
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/register",
                gson.toJson(new Login(username, password))
        );
    }


    void joinGame(String gameId) {
        Gson gson = new Gson();
//        ClientCommunicator.getInstance().get(
//                "/join",
//                gson.toJson()
//        )
    }

    public void createGame() {

    }

    public void startGame() {

    }

    public void leaveGame() {

    }

}
