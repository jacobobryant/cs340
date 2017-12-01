package com.thefunteam.android.model;

import java.util.List;
import com.thefunteam.android.model.shared.Game;
import com.thefunteam.android.model.shared.AvailableGame;
import com.thefunteam.android.model.shared.MapHelper;
import com.thefunteam.android.model.shared.Player;
import com.thefunteam.android.view.Map;
import com.thefunteam.android.model.shared.RejoinableGame;

public class Model {

    private String sessionId;
    private List<AvailableGame> availableGames;
    private Game currentGame;
    private String errorMessage;
    private List<RejoinableGame> rejoinableGames;

    public Model() {}

    public Model(String sessionId, List<AvailableGame> availableGames, Game currentGame,
                 String errorMessage, List<RejoinableGame> rejoinableGames) {
        this.sessionId = sessionId;
        this.availableGames = availableGames;
        this.currentGame = currentGame;
        this.errorMessage = errorMessage;
        this.rejoinableGames = rejoinableGames;
    }

    public String getSessionId() {
        return sessionId;
    }

    public List<AvailableGame> getAvailableGames() {
        return availableGames;
    }

    public List<RejoinableGame> getRejoinableGames() {
        return rejoinableGames;
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

    public String getWinningPlayer() {
        List<Player> players = getCurrentGame().getPlayers();

        int topIndex = -1;
        int topScore = 0;
        int topCompleted = 0;
        boolean longestRoute = false;

        for(int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if(     (player.totalScore() > topScore) ||
                    (player.totalScore() == topScore && player.completedDest > topCompleted) ||
                    (player.totalScore() == topScore && player.completedDest == topCompleted && longestRoute) ||
                    topIndex == -1) {
                topIndex = i;
                topScore = player.totalScore();
                topCompleted = player.completedDest;
                longestRoute = player.longestRoutePoints == 10;
            }
        }

        return players.get(topIndex).username;
    }
}
