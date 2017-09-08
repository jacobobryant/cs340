package ticket;

import clojure.java.api.Clojure;
import clojure.lang.*;

import java.util.concurrent.Callable;

public class Main {
    private static final IPersistentMap EXAMPLE_DATA = (IPersistentMap)EdnReader.readString(
            "{\"players\" [{\"name\" \"John\" \"password\" \"abc123\"} " +
                    "{\"name\" \"Suzy\" \"password\" \"correcthorsebatterystaple\"}]}",
            PersistentHashMap.EMPTY);
    private static final Ref state = new Ref(EXAMPLE_DATA);

    static final IFn assocIn = Clojure.var("clojure.core", "assoc-in");
    static final IFn getIn = Clojure.var("clojure.core", "get-in");

    public static void main(String[] args) {
        System.out.println(state.deref());
        setPassword(0, "12345");
        System.out.println(state.deref());
        System.out.println("Suzy's password: " + getPassword(1));
    }

    public static void setPassword(int userPosition, String newPassword) {
        updateState(() -> {
            state.set(assoc(newPassword, "players", userPosition, "password"));
            return null;
        });
    }

    public static String getPassword(int userPosition) {
        return (String)get("players", userPosition, "password");
    }

    public static Object assoc(Object value, Object... path) {
        return assocIn.invoke(state.deref(), path, value);
    }

    public static Object get(Object... path) {
        return getIn.invoke(state.deref(), path);
    }

    public static void updateState(Callable c) {
        try {
            LockingTransaction.runInTransaction(c);
        } catch (Exception e) {
            System.out.println("Couldn't update state");
            e.printStackTrace();
        }
    }
}
