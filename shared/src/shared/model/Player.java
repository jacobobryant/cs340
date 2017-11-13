package shared.model;

import java.util.List;

public class Player {
    public final String username;
    public final List<Route> routes;
    public final List<TrainType> trainCards;
    public final List<DestinationCard> destCards;
    public final List<DestinationCard> pending;
    public final int routePoints;
    public final int longestRoutePoints;
    public final int destPoints;
    public final int destPenalty;
    public final int trainsLeft;
    public final boolean currentPlayer;
    public final TurnState turnState;

    public Player(String username, List<Route> routes, List<TrainType>
            trainCards, List<DestinationCard> destCards, List<DestinationCard>
            pending, int routePoints, int longestRoutePoints, int destPoints,
            int destPenalty, int trainsLeft, boolean currentPlayer, TurnState
            turnState) {
        this.username = username;
        this.routes = routes;
        this.trainCards = trainCards;
        this.destCards = destCards;
        this.pending = pending;
        this.routePoints = routePoints;
        this.longestRoutePoints = longestRoutePoints;
        this.destPoints = destPoints;
        this.destPenalty = destPenalty;
        this.trainsLeft = trainsLeft;
        this.currentPlayer = currentPlayer;
        this.turnState = turnState;
    }
}
