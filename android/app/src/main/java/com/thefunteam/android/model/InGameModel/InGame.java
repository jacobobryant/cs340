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

    public InGame(List<Player> players, int trainDeck, List<TrainType> faceUpDeck, int destDeck) {
        this.players = players;
        this.trainDeck = trainDeck;
        this.faceUpDeck = faceUpDeck;
        this.destDeck = destDeck;
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
}
