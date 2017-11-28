package shared.dao;

import java.util.List;

public interface UserDao {
    void saveUser(String user, String password);

    // Note: the server will never actually call this method
    List<User> loadUsers();
}
