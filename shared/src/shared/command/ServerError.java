package shared.command;

public class ServerError {
    public final String code;
    public final String message;

    public ServerError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
