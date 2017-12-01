package com.thefunteam.android.model.shared;

public class ReturnDestCommand {
    public final String sessionId;
    public final DestinationCard[] cards;

    public ReturnDestCommand(String sessionId, DestinationCard[] cards) {
        this.sessionId = sessionId;
        this.cards = cards;
    }
}
