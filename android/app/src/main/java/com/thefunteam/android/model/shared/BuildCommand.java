package com.thefunteam.android.model.shared;

import java.util.List;

public class BuildCommand {
    public final String sessionId;
    public final Route route;
    public final List<TrainType> cards;

    public BuildCommand(String sessionId, Route route,
                        List<TrainType> cards) {
        this.sessionId = sessionId;
        this.route = route;
        this.cards = cards;
    }
}
