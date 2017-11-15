package shared;

import java.util.Collections;
import java.util.List;

public class ClientModel {
    public final String sessionId;
    public final List<AvailableGame> availableGames;  // nullable
    public final List<RejoinableGame> rejoinableGames;
    public final Game currentGame;  // nullable

    public ClientModel(String sessionId,
            List<AvailableGame> availableGames,
                       List<RejoinableGame> rejoinableGames, Game currentGame) {
        this.sessionId = sessionId;
        this.availableGames = (availableGames == null) ? null :
                Collections.unmodifiableList(availableGames);
        this.rejoinableGames = (rejoinableGames == null) ? null :
                Collections.unmodifiableList(rejoinableGames);
        this.currentGame = currentGame;
    }
}
