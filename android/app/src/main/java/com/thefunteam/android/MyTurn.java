package com.thefunteam.android;

import com.google.gson.Gson;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.UserCommand;

public class MyTurn implements TurnState {

    @Override
    public void drawDestCard() {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/draw-dest",
                gson.toJson(new UserCommand(sessionId))
        );
    }

    @Override
    public void returnDestCard(int returnCard) {
        // TODO: send a ReturnDestCommand with a list of DestCards to return
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/return-dest",
                gson.toJson(new UserCommand(sessionId))
        );
    }

    @Override
    public void drawTrainCard() {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/draw-train",
                gson.toJson(new UserCommand(sessionId))
        );
    }

    @Override
    public void drawFaceUpCard(int index) {
        // TODO: send the DrawFaceUpCardCommand with the index of the card you want to draw
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/choose-train",
                gson.toJson(new UserCommand(sessionId))
        );
    }

    @Override
    public void claimRoute() {
        // TODO: send the route that you want to claim
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/claim-route",
                gson.toJson(new UserCommand(sessionId))
        );
    }
}
