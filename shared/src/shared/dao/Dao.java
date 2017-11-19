package shared.dao;

import java.util.List;

public interface Dao {
    // If wipe is true, delete any existing data. Else, make sure there aren't
    // any missing events in the data. For example, if we have events saved with
    // IDs 1, 2, 3, 5, 6, then event 4 is missing and we should delete events 5
    // and 6. (In practice this isn't likely to happen, but it could if we
    // closed the server while it was handling concurrent requests).
    // Actually, instead of dealing with deleting corrupted data, just wait for
    // the server to finish processing any requests before you shut it down.
    void init(boolean wipe);

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
