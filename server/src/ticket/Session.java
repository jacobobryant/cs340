package ticket;

import shared.DestinationCard;
import shared.Player;
import shared.Route;
import shared.TrainType;

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
                            "destCards", C.vector.invoke(),
                            "trainCards", C.vector.invoke(),
                            "score", 0},
              path);
    }

    public Player getClientModel(boolean isCurrentPlayer) {
        List<TrainType> trainCards;
        List<DestinationCard> destCards;
        if (isCurrentPlayer) {
            trainCards = getTrainCards();
            destCards = getDestCards();
        } else {
            trainCards = Arrays.asList(new TrainType[getTrainCards().size()]);
            destCards = Arrays.asList(new DestinationCard[getDestCards().size()]);
        }
        return new Player(getUsername(), getRoutes(), trainCards,
                destCards, getScore(), getTrainsLeft());
    }

    public List<Route> getRoutes() {
        return (List)data.get("routes");
    }

    public int getScore() {
        return (int)data.get("score");
    }

    public int getTrainsLeft() {
        return (int)data.get("trainsLeft");
    }

    public Session giveTrain(TrainType train) {
        return new Session(update("trainCards", C.conj, train), path);
    }

    public Session giveDest(DestinationCard dest) {
        return new Session(update("destCards", C.conj, dest), path);
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
}
