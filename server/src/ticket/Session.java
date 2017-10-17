package ticket;

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
                            "destinationCards", C.vector.invoke(),
                            "trainCards", C.vector.invoke(),
                            "longestPath", 0,
                            "score", 0},
              path);
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
