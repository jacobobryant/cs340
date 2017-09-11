package ticket;

import java.util.Map;

// DEMO: how to create a new data model
public class User extends BaseModel {
    public User(String name, String password, int userPosition) {
        super((Map)Main.hashMap.invoke("name", name, "password", password),
                new Object[] {"users", userPosition});
    }

    public User(Map data, Object[] path) {
        super(data, path);
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

    public static User getUser(int userPosition) {
        Object[] path = new Object[] {"users", userPosition};
        return new User((Map)Main.get(path), path);
    }
}
