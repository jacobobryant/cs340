package com.thefunteam.android.model;

/**
 * Created by msi on 2017-10-04.
 */

public class JoinGame {
    String sessionId;
    String gameId;

    public JoinGame(String sessionId, String gameId) {
        this.sessionId = sessionId;
        this.gameId = gameId;
    }
}
