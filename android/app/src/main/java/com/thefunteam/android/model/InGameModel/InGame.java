package com.thefunteam.android.model.InGameModel;

import java.util.List;

public class InGame {

    //list of players
    private List<Player> players;

    //number of cards in face down train deck - train
    private int trainDeck;

    //number of cards in face up train deck - train
    private int faceUpDeck;

    //number of cards in face down destination deck - route
    private int destDeck;

    //number of all trains that will be used by the game
    private int trains;

    //game board
    private GameBoard gameboard;
    //score board
    private ScoreBoard scoreboard;

    public InGame(List<Player> players, int trainDeck, int faceUpDeck, int destDeck, int trains, GameBoard gameboard, ScoreBoard scoreboard) {
        this.players = players;
        this.trainDeck = trainDeck;
        this.faceUpDeck = faceUpDeck;
        this.destDeck = destDeck;
        this.trains = trains;
        this.gameboard = gameboard;
        this.scoreboard = scoreboard;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getTrainDeck() {
        return trainDeck;
    }

    public int getFaceUpDeck() {
        return faceUpDeck;
    }

    public int getDestDeck() {
        return destDeck;
    }

    public int getTrains() {
        return trains;
    }

    public GameBoard getGameboard() {
        return gameboard;
    }

    public ScoreBoard getScoreboard() {
        return scoreboard;
    }

}
