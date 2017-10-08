package com.thefunteam.android.model;

public class GameCommand {
    String sessionId;
    String gameId;

    public GameCommand(String sessionId, String gameId) {
        this.sessionId = sessionId;
        this.gameId = gameId;
    }
}
