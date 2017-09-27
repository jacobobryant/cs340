package ticket;

import java.util.List;
import java.util.Map;

// DEMO: how to create a new data model
public class User extends BaseModel {
    public User(Map data, Object[] path) {
        super(data, path);
    }

    public User(String name, String password, Object[] path) {
        super(new Object[] {"name", name, "password", password,
                            "sessions", C.vector.invoke()}, path);
    }

    public String getPassword() {
        return (String)data.get("password");
    }

    public List<String> getSessions() {
        return (List<String>)data.get("sessions");
    }

    public User addSession(String sessionId) {
        return new User(update("sessions", C.conj, sessionId), path);
    }
}
