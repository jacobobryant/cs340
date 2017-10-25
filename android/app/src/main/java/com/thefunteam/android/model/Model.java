package com.thefunteam.android.model;

import java.util.List;
import com.thefunteam.android.model.shared.Game;
import com.thefunteam.android.model.shared.AvailableGame;

public class Model {

    private String sessionId;
    private List<AvailableGame> availableGames;
    private Game currentGame;
    private String errorMessage;

    public Model(String sessionId, List<AvailableGame> availableGames, Game currentGame, String errorMessage) {
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

    public List<AvailableGame> getAvailableGames() {

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
