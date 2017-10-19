package ticket;

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

    public Map getClientModel(boolean isCurrentPlayer) {
        Map ret = (Map)C.selectKeys.invoke(data, new String[] {"username",
            "routes", "trainsLeft", "destCards", "trainCards", "score"});

        if (!isCurrentPlayer) {
            ret = (Map)C.assoc.invoke(ret,
                    "trainCards", new Object[getTrainCards().size()],
                    "destCards", new Object[getDestCards().size()]);
        }

        return ret;
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
        return (Session)set("gameId", gameId, Session.class);
    }
}
