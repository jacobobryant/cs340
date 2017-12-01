package shared.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum TrainType {
    any {
        public int numInDeck() {
            return 14;
        }

        public String cardName() {
            return "locomotive";
        }

        public boolean match(TrainType other) {
            return true;
        }
    },
    purple, white, blue, yellow, orange, black, red, green;

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
        return 12;
    }

    public String cardName() {
        return this.toString();
    }

    public boolean match(TrainType other) {
        return (any.equals(other) || equals(other));
    }

}
