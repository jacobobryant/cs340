package shared.model;

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
