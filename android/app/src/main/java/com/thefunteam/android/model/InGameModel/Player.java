package com.thefunteam.android.model.InGameModel;

import java.util.List;

public class Player {
    //user name of the player
    private String username;
    //player's color
    private String color;
    //cards that belongs to the player
    private List<Card> cards;
    //route that belongs to the player
    private List<Route> claimedroute;
    //trains that belongs to the player
    private List<Train> trains;
    //score of the player
    private int personalScore;
}
