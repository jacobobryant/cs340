package ticket;

import java.util.Map;

public class Session extends BaseModel {
    public Session(Map data, Object[] path) {
        super(data, path);
    }

    public Session(String id, String username, Object[] path) {
        super(new Object[] {"id", id, "username", username, "gameId", null}, path);
    }

    public String getGame() {
        return (String)data.get("gameId");
    }

    public String getUsername() {
        return (String)data.get("username");
    }

    public Session setGame(String gameId) {
        return (Session)set("gameId", gameId, Session.class);
    }
}
