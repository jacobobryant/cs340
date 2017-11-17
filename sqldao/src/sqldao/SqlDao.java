package sqldao;

import shared.dao.Dao;
import shared.dao.Event;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlDao implements Dao {
    public static final String DB = "jdbc:sqlite:ticket.db";

    interface Transaction {
        void run(Statement st) throws SQLException;
    }

    public void init(boolean wipe) {
        System.out.println("init");
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        runTransaction((Statement st) -> {
            if (wipe) {
                st.executeUpdate("drop table if exists events");
                st.executeUpdate("drop table if exists states");
            }
            st.executeUpdate("create table if not exists events " +
                    "(id integer, endpoint string, json string)");
            st.executeUpdate("create table if not exists states " +
                    "(id integer, state blob)");
        });
        System.out.println("finished SqlDao.init()");
    }

    private void runTransaction(Transaction transaction) {
        try {
            Connection conn = DriverManager.getConnection(DB);
            Statement st = conn.createStatement();
            st.setQueryTimeout(30);
            transaction.run(st);
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void startTransaction() {
        System.out.println("starting transaction");
    }

    public void endTransaction() {
        System.out.println("ending transaction");
    }

    public void saveEvent(int eventId, String endpoint, String json) {
        System.out.println("saving event");
    }

    public void saveState(int eventId, Object state) {
        System.out.println("saving state");
    }

    public Object loadState() {
        System.out.println("loading state");
        return null;
    }

    public List<Event> getEventsAfter(int eventId) {
        System.out.println("getting recent events");
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        new SqlDao().init(false);
    }
}
