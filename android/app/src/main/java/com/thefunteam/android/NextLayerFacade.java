package com.thefunteam.android;

import com.google.gson.Gson;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.GameCommand;
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

    public void joinGame(String gameId) {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/joinGame",
                gson.toJson(new GameCommand(sessionId, gameId))
        );
    }

    public void createGame(String gameId) {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/createGame",
                gson.toJson(new GameCommand(sessionId, gameId))
        );
    }

    public void startGame(String gameId) {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/startGame",
                gson.toJson(new GameCommand(sessionId, gameId))
        );
    }

    public void leaveGame(String gameId) {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/leaveGame",
                gson.toJson(new GameCommand(sessionId, gameId))
        );
    }
}
