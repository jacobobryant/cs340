package com.thefunteam.android.model;

import com.thefunteam.android.model.InGameModel.InGame;

import java.util.List;

public class Model {

    private String sessionId;
    private List<Game> availableGames;
    private Game currentGame;
    private InGame inGameObject;
    private String errorMessage;

    public Model(String sessionId, List<Game> availableGames, Game currentGame, InGame inGameObject, String errorMessage) {
        this.sessionId = sessionId;
        this.availableGames = availableGames;
        this.currentGame = currentGame;
        this.inGameObject = inGameObject;
        this.errorMessage = errorMessage;
    }

    public String getSessionId() {
        return sessionId;
    }

    public List<Game> getAvailableGames() {
        return availableGames;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public InGame getInGameObject() {
        return inGameObject;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
