package ticket;

import java.util.Map;

public class Session extends BaseModel {
    public Session(Map data, Object[] path) {
        super(data, path);
    }

    public Session(String id, Object[] path) {
        super(new Object[] {"id", id}, path);
    }

    public String getId() {
        return (String)data.get("id");
    }
}
