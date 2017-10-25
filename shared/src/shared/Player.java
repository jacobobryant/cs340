package shared;

import java.util.List;

public class Player {
    public final String username;
    public final List<Route> routes;
    public final List<TrainType> trainCards;
    public final List<DestinationCard> destCards;
    public final int score;
    public final int trainsLeft;
    public final boolean currentPlayer;

    public Player(String username, List<Route> routes, List<TrainType> trainCards,
                  List<DestinationCard> destCards, int score, int trainsLeft,
                  boolean currentPlayer) {
        this.username = username;
        this.routes = routes;
        this.trainCards = trainCards;
        this.destCards = destCards;
        this.score = score;
        this.trainsLeft = trainsLeft;
        this.currentPlayer = currentPlayer;
    }
}
