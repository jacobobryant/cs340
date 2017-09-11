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

    public User(String name, String password) {
        this((Map)Main.hashMap.invoke("name", name, "password", password));
    }

    public User setPassword(String newPassword) {
        return new User(getName(), newPassword);
    }

    public void commit(int userPosition) {
        Main.updateState(() -> {
            Main.state.set(Main.assoc(this.data, "users", userPosition));
            return null;
        });
    }

    public static User getUser(int userPosition) {
        return new User((Map)Main.get("users", userPosition));
    }


    public String toString() {
        return data.toString();
    }
}
