package shared;

import java.util.Collections;
import java.util.List;

public class RejoinableGame {
    public final String gameId;
    public final List<String> players;
    public final String sessionId;

    public RejoinableGame(String gameId, List<String> players, String sessionId) {
        this.gameId = gameId;
        this.sessionId = sessionId;
        this.players = (players == null) ? null :
                Collections.unmodifiableList(players);
    }
}
