package fileDao;

import shared.dao.User;

import java.util.List;

public class UserArray {
    public List<User> userList;

    public UserArray(List<User> user) {
        this.userList = user;
    }
}