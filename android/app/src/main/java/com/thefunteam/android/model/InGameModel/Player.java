package com.thefunteam.android.model.InGameModel;

import java.util.List;

public class Player {
    //user name of the player
    private String username;
    //player's color
    private String color;
    //cards that belongs to the player
    private List<DestinationCard> cards;
    //route that belongs to the player
    private List<Route> claimedRoute;
    //trains that belongs to the player
    private List<TrainType> trainCards;

    private List<DestinationCard> destCards;

    private int trainsLeft;
    //score of the player
    private int score;

    public Player(String username, String color, List<DestinationCard> cards, List<Route> claimedRoute, List<TrainType> trainCards, List<DestinationCard> destCards, int score) {
        this.username = username;
        this.color = color;
        this.cards = cards;
        this.claimedRoute = claimedRoute;
        this.trainCards = trainCards;
        this.destCards = destCards;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public String getColor() {
        return color;
    }

    public List<DestinationCard> getCards() {
        return cards;
    }

    public List<Route> getClaimedRoute() {
        return claimedRoute;
    }

    public List<TrainType> getTrainCards() {
        return trainCards;
    }

    public int getScore() {
        return score;
    }

    public List<DestinationCard> getDestCards() {
        return destCards;
    }
}
