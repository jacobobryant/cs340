package com.thefunteam.android.model.shared;

import java.util.List;

public class Player {
    public final String username;
    public final List<Route> routes;
    public final List<TrainType> trainCards;
    public final List<DestinationCard> destCards;
    public final List<DestinationCard> pending;

    public final int routePoints;
    public final int longestRoutePoints;
    public final int destPoints;
    public final int destPenalty;
    public int trainsLeft;
    public boolean currentPlayer;
    public final TurnState turnState;

    public Player(String username, List<Route> routes, List<TrainType> trainCards, List<DestinationCard> destCards, List<DestinationCard> pending, int routePoints, int longestRoutePoints, int destPoints, int destPenalty, int trainsLeft, boolean currentPlayer, TurnState turnState) {
        this.username = username;
        this.routes = routes;
        this.trainCards = trainCards;
        this.destCards = destCards;
        this.pending = pending;
        this.routePoints = routePoints;
        this.longestRoutePoints = longestRoutePoints;
        this.destPoints = destPoints;
        this.destPenalty = destPenalty;
        this.trainsLeft = trainsLeft;
        this.currentPlayer = currentPlayer;
        this.turnState = turnState;
    }

    public String getUsername() {
        return username;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public List<TrainType> getTrainCards() {
        return trainCards;
    }

    public List<DestinationCard> getDestCards() {
        return destCards;
    }

    public List<DestinationCard> getPending() {
        return pending;
    }

    public int getRoutePoints() {
        return routePoints;
    }

    public int getLongestRoutePoints() {
        return longestRoutePoints;
    }

    public int getDestPoints() {
        return destPoints;
    }

    public int getDestPenalty() {
        return destPenalty;
    }

    public int getTrainsLeft() {
        return trainsLeft;
    }

    public boolean isCurrentPlayer() {
        return currentPlayer;
    }

    public TurnState getTurnState() {
        return turnState;
    }
}