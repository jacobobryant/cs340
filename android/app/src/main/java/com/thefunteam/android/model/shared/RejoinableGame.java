package com.thefunteam.android.model.shared;

import java.util.Collections;
import java.util.List;

//single rejoinable game
public class RejoinableGame {
    public final String gameId;
    public final List<String> players;
    public final String sessionId;

    public RejoinableGame(String gameId, List<String> players, String sessionId) {
        this.gameId = gameId;
        this.players = (players == null) ? null :
                Collections.unmodifiableList(players);
        this.sessionId = sessionId;
    }

    public String getGameId() {
        return gameId;
    }

    public List<String> getPlayers() {
        return players;
    }

    public String getSessionId() {
        return sessionId;
    }
}
