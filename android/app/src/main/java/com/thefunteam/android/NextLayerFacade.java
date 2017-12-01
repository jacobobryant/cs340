package com.thefunteam.android;

import com.google.gson.Gson;
import com.thefunteam.android.model.*;
import com.thefunteam.android.model.MessageCommand;
import com.thefunteam.android.model.shared.*;

import java.util.List;

/**
 * This class is used to do everything that the presenters need to do.
 * That way the presenters don't have to worry about the ClientCommunticator and json and junk like that.
 */
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

    public void sendMessage(String message){
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/chat",
                gson.toJson(new MessageCommand(sessionId, message))
        );
    }

    public void returnDestCard(DestinationCard[] returnCard) {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/return-dest",
                gson.toJson(new ReturnDestCommand(sessionId, returnCard))
        );
    }

    public void drawDestCard() {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/draw-dest",
                gson.toJson(new UserCommand(sessionId))
        );
    }

    public void drawTrainCard() {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/draw-train",
                gson.toJson(new UserCommand(sessionId))
        );
    }

    public void drawFaceUpCard(int index) {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/draw-faceup-train",
                gson.toJson(new FaceupTrainCommand(sessionId, index))
        );
    }

    public void claimRoute(Route route, List<TrainType> cards) {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/build",
                gson.toJson(new BuildCommand(sessionId, route, cards))
        );
    }
}
