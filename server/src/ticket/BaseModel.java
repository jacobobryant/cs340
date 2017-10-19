package ticket;

import clojure.lang.IFn;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

// Base class for data objects that must remember their location in the state
// (e.g. for use with State.commit). For example, Route doesn't extend this
// class because we never need to change an individual Route object.
public class BaseModel {
    public final Map data;
    public final Object[] path;

    protected BaseModel(Object[] associations, Object[] path) {
        this.data = (Map)C.apply.invoke(C.hashMap, associations);
        this.path = path;
    }

    protected BaseModel(Map data, Object[] path) {
        if (data == null) {
            throw new NullPointerException("BaseModel data is null. path=" +
                    C.vec.invoke(path));
        }
        this.data = data;
        this.path = path;
    }

    protected Map set(Object key, Object value) {
        return (Map)C.assocIn.invoke(this.data, new Object[] {key}, value);
    }

    protected Map update(Object key, IFn fn, Object... fnargs) {
        return (Map)C.apply.invoke(C.update, this.data, key, fn, fnargs);
    }

    protected Map remove(Object key, Object item) {
        return update(key, C.vecrm, item);
    }

    public String toString() {
        return data.toString();
    }
}
