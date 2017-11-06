package com.thefunteam.android;

public interface TurnState {

    void drawDestCard();
    void returnDestCard(int returnCard);
    void drawTrainCard();
    void drawFaceUpCard(int index);
    void claimRoute();
}
