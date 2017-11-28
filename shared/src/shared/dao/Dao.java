package shared.dao;

import java.util.List;

// I would've called this GameDao, but in the server code I have a good pun
// that depends on this being called Dao.
public interface Dao {
    // precondition: there is no existing saved event (or state) with the same
    // event ID that we pass in now.
    void saveEvent(int eventId, String endpoint, String json);
    void saveState(int eventId, Object state);

    // return the state object that was passed into saveState with the highest
    // event ID. If there aren't any saved states yet, return null.
    Object loadState();

    // events should be sorted by event ID (ascending). Return only events
    // with IDs that are strictly greater than eventId.
    List<Event> getEventsAfter(int eventId);
}
