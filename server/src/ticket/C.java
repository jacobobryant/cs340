package ticket;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

public class C {
    public static final IFn assocIn = Clojure.var("clojure.core", "assoc-in");
    public static final IFn getIn = Clojure.var("clojure.core", "get-in");
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
    public static final IFn swapperToFn = (IFn)eval.invoke(readString.invoke(
                "(fn [old-state swapper] (.swap swapper old-state))"));
}
