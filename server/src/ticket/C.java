package ticket;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

import java.util.ArrayList;
import java.util.Collection;

public class C {
    public static final IFn assocIn = Clojure.var("clojure.core", "assoc-in");
    public static final IFn assoc = Clojure.var("clojure.core", "assoc");
    public static final IFn getIn = Clojure.var("clojure.core", "get-in");
    public static final IFn get = Clojure.var("clojure.core", "get");
    public static final IFn hashMap = Clojure.var("clojure.core", "hash-map");
    public static final IFn apply = Clojure.var("clojure.core", "apply");
    public static final IFn atom = Clojure.var("clojure.core", "atom");
    public static final IFn swap = Clojure.var("clojure.core", "swap!");
    public static final IFn deref = Clojure.var("clojure.core", "deref");
    public static final IFn eval = Clojure.var("clojure.core", "eval");
    public static final IFn readString = Clojure.var("clojure.core", "read-string");
    public static final IFn vector = Clojure.var("clojure.core", "vector");
    public static final IFn conj = Clojure.var("clojure.core", "conj");
    public static final IFn update = Clojure.var("clojure.core", "update");
    public static final IFn last = Clojure.var("clojure.core", "last");
    public static final IFn vec = Clojure.var("clojure.core", "vec");
    public static final IFn subvec = Clojure.var("clojure.core", "subvec");
    public static final IFn shuffle = Clojure.var("clojure.core", "shuffle");
    public static final IFn selectKeys = Clojure.var("clojure.core", "select-keys");
    public static final IFn println = Clojure.var("clojure.core", "println");
    public static final IFn partial = Clojure.var("clojure.core", "partial");
    public static final IFn reduce = Clojure.var("clojure.core", "reduce");
    public static final IFn minus = Clojure.var("clojure.core", "-");
    public static final IFn dec = Clojure.var("clojure.core", "dec");

    public static final IFn swapperToFn = (IFn)eval.invoke(readString.invoke(
            "(fn [old-state swapper] (.swap swapper old-state))"));
    public static final IFn vecrm = (IFn)eval.invoke(readString.invoke(
            "(fn [v item] (vec (remove #(= item %) v)))"));
    public static final IFn dissocIn = (IFn)eval.invoke(readString.invoke(
            "(fn [m path] (update-in m (butlast path) dissoc (last path)))"));
    public static final IFn vconcat = (IFn)eval.invoke(readString.invoke(
            "(fn [& args] (vec (apply concat args)))"));
    public static final IFn removeAt = (IFn)eval.invoke(readString.invoke(
            "(fn [v i] (reduce conj (subvec v 0 i) (subvec v (inc i))))"));
        
    static {
        IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read("clojure.pprint"));
    }

    public static final IFn pprint = Clojure.var("clojure.pprint", "pprint");

    public static Collection removeAll(Collection a, Collection b) {
        ArrayList list = new ArrayList(a);
        for (Object item : b) {
            if (!list.remove(item)) {
                throw new RuntimeException("couldn't remove all values");
            }
        }
        return (Collection)C.vec.invoke(list);
    }

    public static boolean containsAll(Collection a, Collection b) {
        ArrayList list = new ArrayList(a);
        for (Object item : b) {
            if (!list.remove(item)) {
                return false;
            }
        }
        return true;
    }

    public static int castInt(Object o) {
        try {
            return (int)o;
        } catch (ClassCastException e) {
            return (int)(long)o;
        }
    }
}
