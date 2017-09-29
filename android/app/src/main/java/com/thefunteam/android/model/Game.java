package com.thefunteam.android.model;

import java.util.List;

public class Game {

    private String gameId;
    private boolean started;
    private List<String> players;

    public Game(String gameId, boolean started, List<String> players) {
        this.gameId = gameId;
        this.started = started;
        this.players = players;
    }

    public Game() {
    }

    public String getGameId() {
        return gameId;
    }

    public boolean isStarted() {
        return started;
    }

    public List<String> getPlayers() {
        return players;
    }
}
