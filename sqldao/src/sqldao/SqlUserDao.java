package sqldao;

import shared.dao.User;
import shared.dao.UserDao;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SqlUserDao implements UserDao {
    @Override
    public void saveUser(String name, String password) {
        SqlDaoFactory.sRunTransaction((Connection conn) -> {
            PreparedStatement st = conn.prepareStatement(
                    "insert into users (name, password) values (?, ?)");
            st.setString(1, name);
            st.setString(2, password);
            st.executeUpdate();
            st.close();
        });
    }

    @Override
    public List<User> loadUsers() {
        List<User> ret = new LinkedList<>();
        try {
            Connection conn = DriverManager.getConnection(SqlDaoFactory.DB);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select name, password from users");
            while (rs.next()) {
                ret.add(new User(rs.getString(1), rs.getString(2)));
            }
            st.close();
            rs.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }
}
