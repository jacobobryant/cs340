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

    public String getName() {
        return (String)data.get("name");
    }

    public String getPassword() {
        return (String)data.get("password");
    }

    public int countSessions() {
        return ((List)data.get("sessions")).size();
    }

    //public User setPassword(String newPassword) {
    //    return (User)set("password", newPassword, User.class);
    //}

    //public User setName(String newName) {
    //    return (User)set("name", newName, User.class);
    //}
}
