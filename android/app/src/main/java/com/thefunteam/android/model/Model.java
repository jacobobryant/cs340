package com.thefunteam.android.model;

import com.thefunteam.android.model.InGameModel.InGame;

import java.util.List;
import com.thefunteam.android.model.shared.Game;
import com.thefunteam.android.model.shared.AvailableGame;
import com.thefunteam.android.model.shared.Player;

public class Model {

    private String sessionId;
    private List<AvailableGame> availableGames;
    private Game currentGame;
    private String errorMessage;

    public Model() {}

    public Model(String sessionId, List<AvailableGame> availableGames, Game currentGame, String errorMessage) {
        this.sessionId = sessionId;
        this.availableGames = availableGames;
        this.currentGame = currentGame;
        this.errorMessage = errorMessage;
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

    public Player getCurrentPlayer() {
        if( currentGame == null || currentGame.getPlayers() == null) { return null; }
        for(int i = 0; i < currentGame.getPlayers().size(); i++) {
            Player player = currentGame.getPlayers().get(i);
            if(player.isCurrentPlayer()) {
                return player;
            }
        }
        return null;
    }
}
