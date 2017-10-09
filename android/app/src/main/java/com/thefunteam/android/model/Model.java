package com.thefunteam.android.model;

import java.util.List;

public class Model {

    private String sessionId;
    private List<Game> availableGames;
    private Game currentGame;
    private String errorMessage;

    public Model(String sessionId, List<Game> availableGames, Game currentGame, String errorMessage) {
        this.sessionId = sessionId;
        this.availableGames = availableGames;
        this.currentGame = currentGame;
        this.errorMessage = errorMessage;
    }

    public Model() {
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

    public String getErrorMessage() {
        return errorMessage;
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
