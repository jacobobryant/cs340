package shared.command;

public class MessageCommand {
    public final String sessionId;
    public final String message;

    public MessageCommand(String sessionId, String message) {
        this.sessionId = sessionId;
        this.message = message;
    }
}
