package com.thefunteam.android.model.InGameModel;

import java.util.List;

public class InGame {

    //list of players
    private List<Player> players;
    //face down deck - train
    private List<Card> trainDeck;

    //face down deck - route
    private List<Card> destDeck2;

    //face up deck - train
    private List<Card> faceUpDeck;

    //all trains that will be used by the game
    private List<Train> trains;

    //game board
    private gameBoard gameboard;
    //score board
    private scoreBoard scoreboard;
}
