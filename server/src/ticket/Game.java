package ticket;

import java.util.List;
import java.util.Map;

public class Game extends BaseModel {
    public Game(Map data, Object[] path) {
        super(data, path);
    }

    public Game(String gameId, String sessionId, boolean started, Object[] path) {
        super(new Object[] {"gameId", gameId, 
            "players", C.vector.invoke(sessionId),
            "started", started}, path);
    }

    public boolean hasPlayer(String sessionId) {
        return ((List)data.get("players")).contains(sessionId);
    }

    public String getId() {
        return (String)data.get("gameId");
    }

    public boolean started() {
        return (boolean)data.get("started");
    }

    public List getPlayers() {
        return (List)data.get("players");
    }
}
