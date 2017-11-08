package ticket;

import shared.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
                            "score", 0,
                            "turnState", TurnState.lobby},
              path);
    }

    public Player getClientModel(boolean isCurrentPlayer) {
        List<TrainType> trainCards;
        List<DestinationCard> destCards;
        List<DestinationCard> pending;
        if (isCurrentPlayer) {
            trainCards = getTrainCards();
            destCards = getDestCards();
            pending = getPendingDestCards();
        } else {
            trainCards = Arrays.asList(new TrainType[getTrainCards().size()]);
            destCards = Arrays.asList(new DestinationCard[getDestCards().size()]);
            pending = Arrays.asList(new DestinationCard[getPendingDestCards().size()]);
        }
        return new Player(getUsername(), getRoutes(), trainCards,
                destCards, pending, getScore(), getTrainsLeft(), isCurrentPlayer,
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

    public int getScore() {
        return (int)data.get("score");
    }

    public int getTrainsLeft() {
        try {
            return (int)data.get("trainsLeft");
        } catch (ClassCastException e) {
            return (int)(long)data.get("trainsLeft");
        }
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

    public int countTrainCards(TrainType color) {
        return (int)(getTrainCards().stream()
                    .filter((card) -> card.equals(color)).count());
    }
}
