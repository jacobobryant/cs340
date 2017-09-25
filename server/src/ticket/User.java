package ticket;

import java.util.Map;

// DEMO: how to create a new data model
public class User extends BaseModel {
    public User(Map data, Object[] path) {
        super(data, path);
    }

    public User(String name, String password, Object[] path) {
        super(new Object[] {"name", name, "password", password}, path);
    }

    public String getName() {
        return (String)data.get("name");
    }

    public String getPassword() {
        return (String)data.get("password");
    }

    public User setPassword(String newPassword) {
        return (User)set("password", newPassword, User.class);
    }

    public User setName(String newName) {
        return (User)set("name", newName, User.class);
    }
}
