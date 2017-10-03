package ticket;

public class E {
    public static final int CLIENT_CODE = 666;
    public static final int SERVER = 42;
    public static final int USERNAME_TAKEN = 0;
    public static final int LOGIN_FAILED = 1;
    public static final int INVALID_SESSION_ID = 2;
    public static final int HAS_GAME = 3;

    public static class BaseException extends RuntimeException {
        @Override
        public String getMessage() {
            throw new UnsupportedOperationException();
        }

        public int getCode() {
            throw new UnsupportedOperationException();
        }
    }

    public static class UserExistsException extends BaseException {
        @Override
        public String getMessage() {
            return "That username has already been taken";
        }

        @Override
        public int getCode() {
            return E.USERNAME_TAKEN;
        }
    }

    public static class LoginException extends BaseException {
        @Override
        public String getMessage() {
            return "Invalid username/password combination";
        }

        @Override
        public int getCode() {
            return E.LOGIN_FAILED;
        }
    }

    public static class SessionException extends BaseException {
        @Override
        public String getMessage() {
            return "Invalid session ID";
        }

        @Override
        public int getCode() {
            return E.INVALID_SESSION_ID;
        }
    }

    public static class HasGameException extends BaseException {
        @Override
        public String getMessage() {
            return "Session is already part of a game";
        }

        @Override
        public int getCode() {
            return E.HAS_GAME;
        }
    }
}
