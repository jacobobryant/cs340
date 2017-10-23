package com.thefunteam.android.model.InGameModel;

import java.util.List;

public class InGame {

    //list of players
    private List<Player> players;

    //number of cards in face down train deck - train
    private int trainDeck;

    //number of cards in face up train deck - train
    private List<TrainType> faceUpDeck;

    //number of cards in face down destination deck - route
    private int destDeck;

    //game history
    private List<String> gameHistory;

    public InGame(List<Player> players, int trainDeck, List<TrainType> faceUpDeck, int destDeck, List<String> gameHistory) {
        this.players = players;
        this.trainDeck = trainDeck;
        this.faceUpDeck = faceUpDeck;
        this.destDeck = destDeck;
        this.gameHistory = gameHistory;
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

    public List<String> getGameHistory() { return gameHistory;  }
}
