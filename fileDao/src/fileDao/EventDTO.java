package fileDao;


public class EventDTO {
    public final int id;
    public final String endpoint;
    public final String json;

    public EventDTO(int id, String endpoint, String json) {
        this.id = id;
        this.endpoint = endpoint;
        this.json = json;
    }

    public static int idCompare(EventDTO t, EventDTO t1) {
        return (t.id - t1.id);
    }
}
