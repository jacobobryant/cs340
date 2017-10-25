package com.thefunteam.android.model.shared;

import java.util.Collections;
import java.util.List;

public class ClientModel {
    public final String sessionId;
    public final List<AvailableGame> availableGames;  // nullable
    public final Game currentGame;  // nullable

    public ClientModel(String sessionId,
            List<AvailableGame> availableGames, Game currentGame) {
        this.sessionId = sessionId;
        this.availableGames = (availableGames == null) ? null :
                Collections.unmodifiableList(availableGames);
        this.currentGame = currentGame;
    }
}
