package fileDao;

import com.google.gson.Gson;
import shared.dao.User;
import shared.dao.UserDao;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUserDao implements UserDao {
    @Override
    public void saveUser(String user, String password) {
        Gson gson = new Gson();

        // serialize the object
        try {
            User user_ = new User(user, password);
            File source = new File("/tmp/users.ticket");
            Scanner scanner = new Scanner(source).useDelimiter("\\Z");
            String old_data = "";
            if(scanner.hasNext()) {
                old_data = scanner.next();
            }
            UserArray userArray = gson.fromJson(old_data, UserArray.class);
            if(userArray == null) { userArray = new UserArray(new ArrayList<>()); }
            if(userArray.userList == null) { userArray.userList = new ArrayList<>(); }
            userArray.userList.add(user_);

            FileWriter fileWriter = new FileWriter(source, false);
            fileWriter.write(gson.toJson(userArray));
            fileWriter.flush();

        } catch (Exception e) {
            System.out.println(e + " save User");
        }

    }

    @Override
    public List<User> loadUsers() {
        Gson gson = new Gson();

        // serialize the object
        try {
            File source = new File("/tmp/users.ticket");
            Scanner scanner = new Scanner(source).useDelimiter("\\Z");
            String old_data = "";
            if(scanner.hasNext()) {
                old_data = scanner.next();
            }
            UserArray userArray = gson.fromJson(old_data, UserArray.class);
            return userArray.userList;

        } catch (Exception e) {
            System.out.println(e + " save User");
        }
        return null;
    }
}
