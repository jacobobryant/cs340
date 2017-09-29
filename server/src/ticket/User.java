package ticket;

import java.util.List;
import java.util.Map;

public class User extends BaseModel {
    public User(Map data, Object[] path) {
        super(data, path);
    }

    public User(String name, String password, Object[] path) {
        super(new Object[] {"name", name, "password", password,
                            "sessionIds", C.vector.invoke()}, path);
    }

    public String getPassword() {
        return (String)data.get("password");
    }

    public List<String> getSessionIds() {
        return (List<String>)data.get("sessionIds");
    }

    public User addSessionId(String sessionId) {
        return new User(update("sessionIds", C.conj, sessionId), path);
    }
}
