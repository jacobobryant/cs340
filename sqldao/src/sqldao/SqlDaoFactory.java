package sqldao;

import shared.dao.Dao;
import shared.dao.GeneralPurposeToolBuildingFactoryFactoryFactory;
import shared.dao.UserDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlDaoFactory implements GeneralPurposeToolBuildingFactoryFactoryFactory {
    public static final String DB = "jdbc:sqlite:ticket.db";
    public static boolean inTransaction = false;
    public static Connection conn = null;

    interface Transaction {
        void run(Connection conn) throws SQLException;
    }

    public void init(boolean wipe) {
        System.out.println("SQLDaoFactory init");
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        runTransaction((Connection conn) -> {
            Statement st = conn.createStatement();
            if (wipe) {
                st.executeUpdate("drop table if exists events");
                st.executeUpdate("drop table if exists states");
                st.executeUpdate("drop table if exists users");
            }
            st.executeUpdate("create table if not exists events " +
                    "(id integer, endpoint string, json string)");
            st.executeUpdate("create table if not exists states " +
                    "(id integer, state blob)");
            st.executeUpdate("create table if not exists users " +
                    "(name string, password string)");
        });
    }

    public Dao makeGameDao() {
        return new SqlDao();
    }

    public UserDao makeUserDao() {
        return new SqlUserDao();
    }

    public String getName() {
        return "sql";
    }

    public void startTransaction() {
        inTransaction = true;
        try {
            conn = DriverManager.getConnection(DB);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void endTransaction() {
        inTransaction = false;
        try {
            conn.commit();
            conn.close();
            conn = null;
        } catch (SQLException e) {
            try {
                conn.rollback();
                conn.close();
                conn = null;
            } catch (SQLException e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    public void runTransaction(Transaction t) {
        sRunTransaction(t);
    }

    public static void sRunTransaction(Transaction transaction) {
        try {
            if (!inTransaction) {
                conn = DriverManager.getConnection(DB);
                conn.setAutoCommit(false);
            }
            transaction.run(conn);
            if (!inTransaction) {
                conn.commit();
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    conn.close();
                    conn = null;
                } catch (SQLException e2) {
                    throw new RuntimeException(e2);
                }
            }
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SqlDaoFactory f = new SqlDaoFactory();
        System.out.println("name: " + f.getName());
        f.init(false);
    }
}
