package ticket;

import java.util.Map;

public class User extends BaseModel {
    public String getName() {
        return (String)data.get("name");
    }

    public String getPassword() {
        return (String)data.get("password");
    }

    public User(Map data) {
        super(data);
    }

    public static User getUser(int userPosition) {
        return new User((Map)Main.get("users", userPosition));
    }

    public static User createUser(String name, String password) {
        Map hashmap = (Map)Main.hashMap.invoke("name", name, "password", password);
        return new User(hashmap);
    }

    public static void setUser(User user, int userPosition) {
        Main.updateState(() -> {
            Main.state.set(Main.assoc(user.data, "users",
                                      userPosition));
            return null;
        });
    }

    public String toString() {
        return data.toString();
    }
}
