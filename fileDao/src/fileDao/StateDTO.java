package fileDao;

public class StateDTO {
    final long eventId;
    final String state;

    public StateDTO(long eventId, String state) {
        this.eventId = eventId;
        this.state = state;
    }
}
