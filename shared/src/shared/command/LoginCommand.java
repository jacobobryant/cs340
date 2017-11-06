package shared.command;

public class LoginCommand {
    public final String username;
    public final String password;

    public LoginCommand(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
