package com.thefunteam.android.model.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public enum TrainType {
    purple, white, blue, yellow, orange, black, red, green, any;
    public static final List<TrainType> DECK;
    public static final List<String> strings;
    static {
        List<TrainType> tdeck = new ArrayList<>();
        strings = new LinkedList<>();
        for (TrainType type : TrainType.values()) {
            for (int i = 0; i < type.numInDeck(); i++) {
                tdeck.add(type);
            }
            strings.add(type.toString());
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

    public static int countCards(TrainType trainType, List<TrainType> cards) {
        int count = 0;
        for(TrainType card : cards) {
            if(card == trainType) {
                count = count + 1;
            }
        }
        return count;
    }

    public String cardName() {
        if (this.equals(TrainType.any)) {
            return "locomotive";
        }
        return this.toString();
    }

    public boolean match(TrainType other) {
        return (equals(any) || any.equals(other) || equals(other));
    }
}
