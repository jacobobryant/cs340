package ticket;

import java.util.Map;

public class BaseModel {
    protected final Map data;
    protected final Object[] path;

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

    protected Map set(Object key, Object value) {
        return (Map)Main.assocIn.invoke(this.data, new Object[] {key}, value);
    }

    public String toString() {
        return data.toString();
    }
}
