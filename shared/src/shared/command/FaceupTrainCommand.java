package shared.command;

public class FaceupTrainCommand {
    public final String sessionId;
    public final int index;

    public FaceupTrainCommand(String sessionId, int index) {
        this.sessionId = sessionId;
        this.index = index;
    }
}
