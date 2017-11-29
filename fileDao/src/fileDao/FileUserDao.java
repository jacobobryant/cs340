package fileDao;

import com.google.gson.Gson;
import shared.dao.User;
import shared.dao.UserDao;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;

public class FileUserDao implements UserDao {
    @Override
    public void saveUser(String user, String password) {
        Gson gson = new Gson();

        // serialize the object
        try {
            User user_ = new User(user, password);
            File source = new File("/tmp/event.ticket");
            String old_data = new Scanner(source).useDelimiter("\\Z").next();
            UserArray userArray = gson.fromJson(old_data, UserArray.class);
            userArray.userList.add(user_);

            FileWriter fileWriter = new FileWriter(source, false);
            fileWriter.write(gson.toJson(userArray));

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @Override
    public List<User> loadUsers() {
        Gson gson = new Gson();

        // serialize the object
        try {
            File source = new File("/tmp/event.ticket");
            String old_data = new Scanner(source).useDelimiter("\\Z").next();
            UserArray userArray = gson.fromJson(old_data, UserArray.class);
            return userArray.userList;

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
