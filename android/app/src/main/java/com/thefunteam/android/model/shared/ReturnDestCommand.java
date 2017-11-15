package com.thefunteam.android.model.shared;

import com.google.gson.JsonElement;
import com.thefunteam.android.model.shared.DestinationCard;

public class ReturnDestCommand {

    String sessionId;
    DestinationCard[] dest;

    public ReturnDestCommand(String sessionId, DestinationCard[] destinationCard) {
        this.sessionId = sessionId;
        this.dest = destinationCard;
    }
}
