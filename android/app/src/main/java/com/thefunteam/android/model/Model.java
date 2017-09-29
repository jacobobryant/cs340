package com.thefunteam.android.model;

import java.util.List;

public class Model {

    private String sessionId;
    private List<Game> availableGames;
    private Game currentGame;

    public Model(String sessionId, List<Game> availableGames, Game currentGame) {
        this.sessionId = sessionId;
        this.availableGames = availableGames;
        this.currentGame = currentGame;
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
}
