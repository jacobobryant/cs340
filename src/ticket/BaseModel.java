package ticket;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BaseModel {
    public final Map data;
    public final Object[] path;

    protected BaseModel(Object[] associations, Object[] path) {
        this.data = (Map)C.apply.invoke(C.hashMap, associations);
        this.path = path;
    }

    protected BaseModel(Map data, Object[] path) {
        this.data = data;
        this.path = path;
    }

    protected BaseModel set(Object key, Object value, Class<? extends BaseModel> clazz) {
        Map data = (Map)C.assocIn.invoke(this.data, new Object[] {key}, value);
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
