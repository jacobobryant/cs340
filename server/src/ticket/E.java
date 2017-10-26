package ticket;

public class E {
    public static final int CLIENT_CODE = 666;
    public static final int SERVER = 42;
    public static final int USERNAME_TAKEN = 0;
    public static final int LOGIN_FAILED = 1;
    public static final int INVALID_SESSION_ID = 2;
    public static final int HAS_GAME = 3;
    public static final int NO_CURRENT_GAME = 4;
    public static final int NOT_ENOUGH_USERS = 5;
    public static final int GAME_UNAVAILABLE = 6;
    public static final int GAME_ALREADY_STARTED = 6;

    public static class BaseException extends RuntimeException {
        @Override
        public String getMessage() {
            throw new UnsupportedOperationException();
        }

        public int getCode() {
            throw new UnsupportedOperationException();
        }
    }

    public static class ClientException extends BaseException {
        private String message;

        public ClientException(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }

        public int getCode() {
            return E.CLIENT_CODE;
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

    public static class NoCurrentGameException extends BaseException {
        @Override
        public String getMessage() {
            return "Session is not a member of a game";
        }

        @Override
        public int getCode() {
            return E.NO_CURRENT_GAME;
        }
    }

    public static class NotEnoughUsersException extends BaseException {
        @Override
        public String getMessage() {
            return "Game doesn't have enough users to start";
        }

        @Override
        public int getCode() {
            return E.NOT_ENOUGH_USERS;
        }
    }

    public static class GameUnavailableException extends BaseException {
        @Override
        public String getMessage() {
            return "Game isn't available";
        }

        @Override
        public int getCode() {
            return E.GAME_UNAVAILABLE;
        }
    }

    public static class GameAlreadyStartedException extends BaseException {
        @Override
        public String getMessage() {
            return "Game already started";
        }

        @Override
        public int getCode() {
            return E.GAME_ALREADY_STARTED;
        }
    }
}
