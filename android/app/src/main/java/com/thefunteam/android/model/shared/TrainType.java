package com.thefunteam.android.model.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum TrainType {
    purple, white, blue, yellow, orange, black, red, green, any;
    public static final List<TrainType> DECK;
    static {
        List<TrainType> tdeck = new ArrayList<>();
        for (TrainType type : TrainType.values()) {
            for (int i = 0; i < type.numInDeck(); i++) {
                tdeck.add(type);
            }
        }
        DECK = Collections.unmodifiableList(tdeck);
    }

    public int numInDeck() {
        if (this.equals(TrainType.any)) {
            return 14;
        }
        return 12;
    }
    public static final int size = 9;
}
