package ticket;

public class E {
    public static final int CLIENT_CODE = 666;
    public static final int SERVER = 42;
    public static final int USERNAME_TAKEN = 0;
    public static final int LOGIN_FAILED = 1;
    public static final int INVALID_SESSION_ID = 2;
    public static final int HAS_GAME = 3;

    public static class UserExistsException extends RuntimeException { }
    public static class LoginException extends RuntimeException { }
    public static class SessionException extends RuntimeException { }
    public static class HasGameException extends RuntimeException { }
}
