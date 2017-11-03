package ticket;

import java.util.Map;

public class BadJuju extends RuntimeException {
    public Map toMap() {
        return map(getMessage());
    }

    public BadJuju(String message) {
        super(message);
    }

    public static Map map(String msg) {
        return (Map)C.hashMap.invoke("code", 666, "message", msg);
    }
}
