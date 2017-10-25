package com.thefunteam.android;

import com.google.gson.Gson;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.GameCommand;
import com.thefunteam.android.model.InGameModel.MessageCommand;
import com.thefunteam.android.model.LoginCommand;
import com.thefunteam.android.model.UserCommand;

public class NextLayerFacade {
    private static NextLayerFacade ourInstance = new NextLayerFacade();

    public static NextLayerFacade getInstance() { return ourInstance; }

    private NextLayerFacade() { }

    public void login(String username, String password) {
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/login",
                gson.toJson(new LoginCommand(username, password))
        );
    }

    public void register(String username, String password) {
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/register",
                gson.toJson(new LoginCommand(username, password))
        );
    }

    public void joinGame(String gameId) {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/join",
                gson.toJson(new GameCommand(sessionId, gameId))
        );
    }

    public void createGame() {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/create",
                gson.toJson(new UserCommand(sessionId))
        );
    }

    public void startGame() {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/start",
                gson.toJson(new UserCommand(sessionId))
        );
    }

    public void leaveGame() {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/leave",
                gson.toJson(new UserCommand(sessionId))
        );
    }

    //added
    public void sendMessage(String message){
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/chat",
                gson.toJson(new MessageCommand(sessionId, message));
        );
    }
}
