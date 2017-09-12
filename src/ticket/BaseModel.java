package ticket;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BaseModel {
    protected final Map data;
    protected final Object[] path;

    protected BaseModel(Object[] associations, Object[] path) {
        this.data = (Map)Main.apply.invoke(Main.hashMap, associations);
        this.path = path;
    }

    protected BaseModel(Map data, Object[] path) {
        this.data = data;
        this.path = path;
    }

    public void commit() {
        Main.updateState(() -> {
            Main.state.set(Main.assoc(this.path, this.data));
            return null;
        });
    }

    protected BaseModel set(Object key, Object value, Class<? extends BaseModel> clazz) {
        Map data = (Map)Main.assocIn.invoke(this.data, new Object[] {key}, value);
        try {
            Constructor<?> constructor = clazz.getConstructor(Map.class, Object[].class);
            return (BaseModel)constructor.newInstance(new Object[]{data, this.path});
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // Programming error, no way to handle.
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return data.toString();
    }
}
