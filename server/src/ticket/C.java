package ticket;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

public class C {
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
}
