package ticket;

public class E {
    public static final int CLIENT_CODE = 666;
    public static final int SERVER = 42;
    public static final int USERNAME_TAKEN = 0;
    public static final int LOGIN_FAILED = 1;

    public static class UserExistsException extends RuntimeException { }
    public static class LoginException extends RuntimeException { }
}
