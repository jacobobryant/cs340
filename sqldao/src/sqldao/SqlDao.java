package sqldao;

import shared.dao.Dao;
import shared.dao.Event;

import java.util.ArrayList;
import java.util.List;

public class SqlDao implements Dao {
    public void init(boolean wipe) {
        System.out.println("init");
    }

    public void startTransaction() {
        System.out.println("starting transaction");
    }

    public void endTransaction() {
        System.out.println("ending transaction");
    }

    public void saveEvent(int eventId, String endpoint, String json) {
        System.out.println("saving event");
    }

    public void saveState(int eventId, Object state) {
        System.out.println("saving state");
    }

    public Object loadState() {
        System.out.println("loading state");
        return null;
    }

    public List<Event> getEventsAfter(int eventId) {
        System.out.println("getting recent events");
        return new ArrayList<>();
    }
}
