package shared.dao;

import java.util.List;

public interface Dao {
    void startTransaction();
    void endTransaction();
    void saveEvent(int eventId, String endpoint, String json);
    void saveState(Object state);
    Object loadState();
    List<Event> getEventsAfter(int eventId);
}
