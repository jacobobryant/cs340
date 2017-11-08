package ticket;

import shared.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Session extends BaseModel {
    public Session(Map data, Object[] path) {
        super(data, path);
    }

    public Session(String id, String username, Object[] path) {
        super(new Object[] {"sessionId", id,
                            "username", username,
                            "gameId", null,
                            "routes", C.vector.invoke(),
                            "trainsLeft", 45,
                            "pending", C.vector.invoke(),
                            "destCards", C.vector.invoke(),
                            "trainCards", C.vector.invoke(),
                            "routePoints", 0,
                            "destPoints", 0,
                            "longestRoutePoints", 0,
                            "destPenalty", 0,
                            "turnState", TurnState.lobby},
              path);
    }

    public Player getClientModel(boolean isCurrentPlayer) {
        List<TrainType> trainCards;
        List<DestinationCard> destCards;
        List<DestinationCard> pending;
        int destPoints;
        int destPenalty;
        if (isCurrentPlayer) {
            trainCards = getTrainCards();
            destCards = getDestCards();
            pending = getPendingDestCards();
            destPoints = getDestPoints();
            destPenalty = getDestPenalty();
        } else {
            trainCards = Arrays.asList(new TrainType[getTrainCards().size()]);
            destCards = Arrays.asList(new DestinationCard[getDestCards().size()]);
            pending = Arrays.asList(new DestinationCard[getPendingDestCards().size()]);
            destPoints = 0;
            destPenalty = 0;
        }
        return new Player(getUsername(), getRoutes(), trainCards,
                destCards, pending, 
                
                getRoutePoints(), getLongestRoutePoints(),
                destPoints, destPenalty,
                
                getTrainsLeft(), isCurrentPlayer,
                getTurnState());
    }

    public TurnState getTurnState() {
        return (TurnState)data.get("turnState");
    }

    public Session setTurnState(TurnState ts) {
        return new Session(set("turnState", ts), path);
    }

    public List<Route> getRoutes() {
        return (List)data.get("routes");
    }

    public int getRoutePoints() {
        return C.castInt(data.get("routePoints"));
    }

    public int getLongestRoutePoints() {
        return C.castInt(data.get("longestRoutePoints"));
    }

    public int getDestPoints() {
        return C.castInt(data.get("destPoints"));
    }

    public int getDestPenalty() {
        return C.castInt(data.get("destPenalty"));
    }

    public int getTrainsLeft() {
        return C.castInt(data.get("trainsLeft"));
    }

    public Session giveTrain(TrainType train) {
        return new Session(update("trainCards", C.conj, train), path);
    }

    public Session givePendingDest(DestinationCard dest) {
        return new Session(update("pending", C.conj, dest), path);
    }

    public Session giveDest(DestinationCard dest) {
        return new Session(update("destCards", C.conj, dest), path);
    }

    public Session setDestCards(List<DestinationCard> cards) {
        return new Session(set("destCards", cards), path);
    }

    public List<DestinationCard> getPendingDestCards() {
        return (List)data.get("pending");
    }

    public Session returnCards(DestinationCard[] cards) {
        List<DestinationCard> pending = getPendingDestCards();
        pending = (List<DestinationCard>)C.removeAll(pending, Arrays.asList(cards));
        return new Session(update("destCards", C.vconcat, pending), path)
                .clearPending();
    }

    private Session clearPending() {
        return new Session(set("pending", C.vector.invoke()), path);
    }

    public Session claim(Route r, List<TrainType> cards) {
        Object data = this.data;
        data = C.assoc.invoke(data, "trainCards", 
                C.removeAll(getTrainCards(), cards));
        data = C.update.invoke(data, "routes", C.conj, r);
        data = C.update.invoke(data, "trainsLeft", C.minus, r.length);
        return new Session((Map)data, path);
    }

    public List<TrainType> getTrainCards() {
        return (List)data.get("trainCards");
    }

    public List<DestinationCard> getDestCards() {
        return (List)data.get("destCards");
    }

    public String getGameId() {
        return (String)data.get("gameId");
    }

    public String getUsername() {
        return (String)data.get("username");
    }

    public Session setGameId(String gameId) {
        return new Session(set("gameId", gameId), path);
    }

    private List<City> getChildrenCities(Set<City> visited, City parent) {
        return getRoutes().stream().filter((r) -> r.match(parent) &&
                !visited.contains(r.other(parent))).map((r) ->
                r.other(parent)).collect(Collectors.toList());
    }

    private boolean pathExists(Set<City> visited, City a, City b) {
        if (getRoutes().stream().anyMatch((r) -> r.match(a, b))) {
            return true;
        }
        visited.add(a);
        return getChildrenCities(visited, a).stream().anyMatch((child) ->
                pathExists(visited, child, b));
    }

    private boolean completed(DestinationCard c) {
        return pathExists(new HashSet<>(), c.city1, c.city2);
    }

    private int longestPath(List<Route> routes, City current) {
        return routes.stream().filter((r) -> r.match(current)).mapToInt((r) ->
                r.length + longestPath((List) C.vecrm.invoke(routes, r),
                        r.other(current))).max().orElse(0);
    }

    public int getLongestRouteLength() {
        return Stream.of(City.values()).mapToInt((city) ->
                longestPath(getRoutes(), city)).max().orElse(0);
    }

    private Session updateRoutePoints() {
        return new Session(set("routePoints",
                    getRoutes().stream().mapToInt((r) -> r.points()).sum()), path);
    }

    private Session updateDestPoints() {
        int destPoints = getDestCards().stream().mapToInt((c) ->
                (completed(c)) ? c.points : 0).sum();
        int destPenalty = getDestCards().stream().mapToInt((c) ->
                (completed(c)) ? 0 : -c.points).sum();
        return new Session((Map)C.assoc.invoke(data, "destPoints", destPoints,
                    "destPenalty", destPenalty), path);
    }

    public Session updatePoints(boolean newRoute, boolean newDestCard,
            int longestRouteLength) {
        Session s = this;
        if (newRoute) {
            s = s.updateRoutePoints();
        } else if (newDestCard) {
            s = s.updateDestPoints();
        }
        int longestRoutePoints = (s.getLongestRouteLength() == longestRouteLength
                && longestRouteLength > 0) ? 10 : 0;
        System.out.println("longestRoutePoints: " + longestRoutePoints);
        return new Session(s.set("longestRoutePoints", longestRoutePoints), s.path);
    }
}
