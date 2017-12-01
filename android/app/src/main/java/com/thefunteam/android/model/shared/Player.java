package com.thefunteam.android.model.shared;

import com.thefunteam.android.activity.GameOverActivity;
import com.thefunteam.android.model.Atom;

import java.util.Comparator;
import java.util.List;

public class Player {
    public final String username;
    public final List<Route> routes;
    public final List<TrainType> trainCards;
    public final List<DestinationCard> destCards;
    public final List<DestinationCard> pending;

    //phase 3 models
    public final int routePoints;
    public final int longestRoutePoints;
    public final int destPoints;
    public final int destPenalty;
    public final int completedDest;

    //summary methods
    public String summary;

    public int trainsLeft;
    public boolean currentPlayer;
    public final TurnState turnState;

    public Player(String username, List<Route> routes, List<TrainType> trainCards, List<DestinationCard> destCards, List<DestinationCard> pending,
                  int routePoints, int longestRoutePoints, int destPoints, int destPenalty, int completedDest, int trainsLeft, boolean currentPlayer, TurnState turnState) {
        this.username = username;
        this.routes = routes;
        this.trainCards = trainCards;
        this.destCards = destCards;
        this.pending = pending;
        this.routePoints = routePoints;
        this.longestRoutePoints = longestRoutePoints;
        this.destPoints = destPoints;
        this.destPenalty = destPenalty;
        this.completedDest = completedDest;
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

    public int totalScore() {
        return routePoints + longestRoutePoints + destPoints + destPenalty;
    }

    public String getScores(){
        String scoresum = new String();
        //show username
        scoresum += "\n\n" + username + "\n\n";
        //show claimed route points
        scoresum += "Claimed Route Score: \n\t" + Integer.toString(routePoints) + "\n\n";
        scoresum += "Longest Path Bonus: \n\t" + Integer.toString(longestRoutePoints) + "\n\n";
        scoresum += "Reached Destination Score: \n\t" + Integer.toString(destPoints) + "\n\n";
        scoresum += "Unreached Destination Panalty: \n\t" + Integer.toString(destPenalty) + "\n\n";
        int total = routePoints + longestRoutePoints + destPoints + destPenalty;
        scoresum += "total: \n\t" + Integer.toString(total);

        List<Player> players = Atom.getInstance().getModel().getCurrentGame().getPlayers();
        players.stream().max(Comparator.comparing(Player::totalScore));

        return scoresum;
    }

}