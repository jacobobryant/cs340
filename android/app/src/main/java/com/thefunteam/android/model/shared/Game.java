package com.thefunteam.android.model.shared;

import java.util.List;

public class Game {
    public final List<Player> players;
    public int trainDeck;
    public final List<TrainType> faceUpDeck;
    public int destDeck;
    public final List<Route> openRoutes;
    public final List<String> messages;
    public final List<String> history;
    public final String longestRouteHolder;
    public final boolean started;
    public final int turnsLeft;

    public Game(List<Player> players, int trainDeck, List<TrainType> faceUpDeck,
                int destDeck, List<Route> openRoutes, List<String> messages,
                List<String> history, String longestRouteHolder, boolean started, int turnsLeft) {
        this.players = players;
        this.trainDeck = trainDeck;
        this.faceUpDeck = faceUpDeck;
        this.destDeck = destDeck;
        this.openRoutes = openRoutes;
        this.messages = messages;
        this.history = history;
        this.longestRouteHolder = longestRouteHolder;
        this.started = started;
        this.turnsLeft = turnsLeft;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getTrainDeck() {
        return trainDeck;
    }

    public List<TrainType> getFaceUpDeck() {
        return faceUpDeck;
    }

    public int getDestDeck() {
        return destDeck;
    }

    public List<Route> getOpenRoutes() {
        return openRoutes;
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<String> getHistory() {
        return history;
    }

    public String getLongestRouteHolder() {
        return longestRouteHolder;
    }

    public boolean isStarted() {
        return started;
    }

    public int getTurnsLeft() {
        return turnsLeft;
    }
}
