package sqldao;

import shared.dao.Dao;
import shared.dao.Event;

import java.io.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SqlDao implements Dao {
    public static final String DB = "jdbc:sqlite:ticket.db";

    interface Transaction {
        void run(Connection conn) throws SQLException;
    }

    public void init(boolean wipe) {
        System.out.println("SQLDao init");
        runTransaction((Connection conn) -> {
            Statement st = conn.createStatement();
            if (wipe) {
                st.executeUpdate("drop table if exists events");
                st.executeUpdate("drop table if exists states");
            }
            st.executeUpdate("create table if not exists events " +
                    "(id integer, endpoint string, json string)");
            st.executeUpdate("create table if not exists states " +
                    "(id integer, state blob)");
        });
    }

    private void runTransaction(Transaction transaction) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB);
            conn.setAutoCommit(false);
            transaction.run(conn);
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e2) {
                    throw new RuntimeException(e2);
                }
            }
            throw new RuntimeException(e);
        }
    }

    public void saveEvent(int eventId, String endpoint, String json) {
        System.out.println("saving event with id " + eventId);
        runTransaction((Connection conn) -> {
            PreparedStatement st = conn.prepareStatement(
                    "insert into events (id, endpoint, json) values (?, ?, ?)");
            st.setInt(1, eventId);
            st.setString(2, endpoint);
            st.setString(3, json);
            st.executeUpdate();
            st.close();
        });
    }

    public void saveState(int eventId, Object state) {
        System.out.println("saving state with id " + eventId);
        try {
            Connection conn = DriverManager.getConnection(DB);
            PreparedStatement st = conn.prepareStatement(
                    "insert into states (id, state) values (?, ?)");
            st.setInt(1, eventId);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(state);
            st.setBytes(2, baos.toByteArray());

            st.executeUpdate();
            st.close();
            conn.close();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object loadState() {
        System.out.println("loading state");
        Object ret = null;
        try {
            Connection conn = DriverManager.getConnection(DB);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select state from states order by id desc limit 1");
            if (rs.next()) {
                byte[] stream = (byte[]) rs.getObject(1);
                ByteArrayInputStream baip = new ByteArrayInputStream(stream);
                ObjectInputStream ois = new ObjectInputStream(baip);
                ret = ois.readObject();
            }
            st.close();
            rs.close();
            conn.close();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    public List<Event> getEventsAfter(int eventId) {
        System.out.println("getting events after id " + eventId);
        List<Event> ret = new LinkedList<>();
        try {
            Connection conn = DriverManager.getConnection(DB);
            PreparedStatement st = conn.prepareStatement("select endpoint, json from events " +
                            "where id > ? order by id asc");
            st.setInt(1, eventId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ret.add(new Event(rs.getString(1), rs.getString(2)));
            }
            st.close();
            rs.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("got " + ret.size() + " event");
        return ret;
    }
}
