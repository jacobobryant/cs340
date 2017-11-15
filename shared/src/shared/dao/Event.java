package shared.dao;

public class Event {
    public final String endpoint;
    public final String json;

    public Event(String endpoint, String json) {
        this.endpoint = endpoint;
        this.json = json;
    }
}
