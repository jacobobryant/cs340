package ticket;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

public class Main {
    static final IFn assocIn = Clojure.var("clojure.core", "assoc-in");
    static final IFn getIn = Clojure.var("clojure.core", "get-in");
    static final IFn hashMap = Clojure.var("clojure.core", "hash-map");
    static final IFn apply = Clojure.var("clojure.core", "apply");
    static final IFn atom = Clojure.var("clojure.core", "atom");
    static final IFn swap = Clojure.var("clojure.core", "swap!");
    static final IFn deref = Clojure.var("clojure.core", "deref");
    static final IFn eval = Clojure.var("clojure.core", "eval");
    static final IFn readString = Clojure.var("clojure.core", "read-string");
    static final IFn swapperToFn = (IFn)eval.invoke(readString.invoke(
                "(fn [old-state swapper] (.swap swapper old-state))"));

    private static final Object EXAMPLE_DATA = readString.invoke(
            "{\"users\" [{\"name\" \"John\" \"password\" \"abc123\"} " +
                        "{\"name\" \"Suzy\" \"password\" \"correcthorsebatterystaple\"}]}");
    static final Object state = atom.invoke(EXAMPLE_DATA);

    public interface Swapper {
        public Object swap(Object oldState);
    }

    // DEMO: how to use the data objects
    public static void main(String[] args) {
        System.out.println(deref.invoke(state));

        User john = User.getUser(0);
        System.out.println(john);
        System.out.println(john.getName());
        System.out.println(john.getPassword());

        john = john.setPassword("mynewpassword");
        john.commit();
        System.out.println(john);

        User fred = new User("Fred", "ilikecheese", 2);
        fred.commit();
        System.out.println(deref.invoke(state));
    }

    public static Object assoc(Object[] path, Object value) {
        return assocIn.invoke(deref.invoke(state), path, value);
    }

    public static Object get(Object[] path) {
        return getIn.invoke(deref.invoke(state), path);
    }
}
