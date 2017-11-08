package shared;

import java.util.List;

public class Game {
    public final List<Player> players;
    public final int trainDeck;
    public final List<TrainType> faceUpDeck;
    public final int destDeck;
    public final List<Route> openRoutes;
    public final List<String> messages;
    public final List<String> history;
    public final boolean started;
    public final int turnsLeft;

    public Game(List<Player> players, int trainDeck, List<TrainType> faceUpDeck,
                int destDeck, List<Route> openRoutes, List<String> messages,
                List<String> history, boolean started, int turnsLeft) {
        this.players = players;
        this.trainDeck = trainDeck;
        this.faceUpDeck = faceUpDeck;
        this.destDeck = destDeck;
        this.openRoutes = openRoutes;
        this.messages = messages;
        this.history = history;
        this.started = started;
        this.turnsLeft = turnsLeft;
    }
}
