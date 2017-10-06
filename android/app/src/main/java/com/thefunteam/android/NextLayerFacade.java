package com.thefunteam.android;

import com.google.gson.Gson;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.CreateGame;
import com.thefunteam.android.model.Game;
import com.thefunteam.android.model.JoinGame;
import com.thefunteam.android.model.Login;

import java.util.concurrent.atomic.AtomicIntegerArray;

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



    public void joinGame(String gameId) {
        String sessionId =  Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/join",
                gson.toJson(new JoinGame(sessionId, gameId))
        );
    }

    public void createGame() {
        String sessionId = new String(); // what should be session ID?
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/create",
                gson.toJson(new CreateGame(sessionId));
        );
    }

    public void startGame() {

    }

    public void leaveGame(String sessionId) {
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/leave",
                gson.toJson(new LeaveGame(sessionId));
        );
    }

}
