package com.thefunteam.android.model.shared;

import java.util.Collections;
import java.util.List;

public class AvailableGame {
    public final String gameId;
    public final List<String> players;

    public AvailableGame(String gameId, List<String> players) {
        this.gameId = gameId;
        this.players = (players == null) ? null :
                Collections.unmodifiableList(players);
    }

    public String getGameId() {
        return gameId;
    }

    public List<String> getPlayers() {
        return players;
    }
}
