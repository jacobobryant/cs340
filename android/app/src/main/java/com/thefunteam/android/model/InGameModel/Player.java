package com.thefunteam.android.model.InGameModel;

import java.util.List;

public class Player {
    //user name of the player
    private String username;
    //player's color
    private String color;
    //cards that belongs to the player
    private List<DestCard> cards;
    //route that belongs to the player
    private List<Route> claimedroute;
    //trains that belongs to the player
    private List<TrainCard> trains;
    //score of the player
    private int personalScore;

    public Player(String username, String color, List<DestCard> cards, List<Route> claimedroute, List<TrainCard> trains, int personalScore) {
        this.username = username;
        this.color = color;
        this.cards = cards;
        this.claimedroute = claimedroute;
        this.trains = trains;
        this.personalScore = personalScore;
    }

    public String getUsername() {
        return username;
    }

    public String getColor() {
        return color;
    }

    public List<DestCard> getCards() {
        return cards;
    }

    public List<Route> getClaimedroute() {
        return claimedroute;
    }

    public List<TrainCard> getTrains() {
        return trains;
    }

    public int getPersonalScore() {
        return personalScore;
    }
    
}
