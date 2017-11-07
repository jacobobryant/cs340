package shared;

import java.util.List;

public class Player {
    public final String username;
    public final List<Route> routes;
    public final List<TrainType> trainCards;
    public final List<DestinationCard> destCards;
    public final List<DestinationCard> pending;
    public final int score;
    public final int trainsLeft;
    public final boolean currentPlayer;
    public final TurnState turnState;

    public Player(String username, List<Route> routes, List<TrainType> trainCards,
                  List<DestinationCard> destCards, List<DestinationCard> pending, 
                  int score, int trainsLeft, boolean currentPlayer,
                  TurnState turnState) {
        this.username = username;
        this.routes = routes;
        this.trainCards = trainCards;
        this.destCards = destCards;
        this.pending = pending;
        this.score = score;
        this.trainsLeft = trainsLeft;
        this.currentPlayer = currentPlayer;
        this.turnState = turnState;
    }
}
