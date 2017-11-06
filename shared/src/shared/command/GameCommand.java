package shared.command;

public class GameCommand {
    public final String sessionId;
    public final String gameId;

    public GameCommand(String sessionId, String gameId) {
        this.sessionId = sessionId;
        this.gameId = gameId;
    }
}
