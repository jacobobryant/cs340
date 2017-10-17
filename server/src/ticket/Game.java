package ticket;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class Game extends BaseModel {
            
    public Game(Map data, Object[] path) {
        super(data, path);
    }

    public Game(String gameId, String sessionId, boolean started, Object[] path) {
        super(new Object[] {"gameId", gameId, 
                            "sessionIds", C.vector.invoke(sessionId),
                            "started", started,
                            "trainDeck", C.shuffle.invoke(TrainType.DECK),
                            "faceUpTrainCards", C.vector.invoke(),
                            "trainDiscard", C.vector.invoke(),
                            "destinationDeck", C.shuffle.invoke(DestinationCard.DECK),
                            "destinationDiscard", C.vector.invoke(),
                            "openRoutes", Route.ROUTES,
                            "messages", C.vector.invoke(),
                            "gameHistory", C.vector.invoke()},
                path);
    }

    public String getGameId() {
        return (String)data.get("gameId");
    }

    public boolean started() {
        return (boolean)data.get("started");
    }

    public Game setStarted(boolean p){
        return (Game)this.set("started", p, Game.class);
    }

    public List<String> getSessionIds() {
        return (List<String>)data.get("sessionIds");
    }

    public Game addSessionId(String sessionId) {
        return new Game(update("sessionIds", C.conj, sessionId), path);
    }

    public Game removeSessionId(String sessionId) {
        return new Game(remove("sessionIds", sessionId), path);
    }

    public boolean isAvailable(User u) {
        List<String> ids = getSessionIds();
        return (!started() && ids.size() < 5 &&
                ids.stream().filter(u.getSessionIds()::contains)
                .collect(toList()).size() == 0);
    }
}
