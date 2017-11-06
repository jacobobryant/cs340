package shared.command;

import shared.DestinationCard;

public class ReturnDestCommand {
    public final String sessionId;
    public final DestinationCard dest;

    public ReturnDestCommand(String sessionId, DestinationCard destinationCard) {
        this.sessionId = sessionId;
        this.dest = destinationCard;
    }
}
