package ticket;

import shared.ClientModel;
import shared.DestinationCard;

import java.util.HashMap;
import java.util.Map;

public class Facade {
    private static Object run(State.Swapper swapper, String key, boolean getByUsername) {
        State m;
        try {
            m = State.swap(swapper);
        } catch (E.BaseException e) {
            return error(e);
        }
        String sessionId;
        if (getByUsername) {
            sessionId = m.getNewestSession(key);
        } else {
            sessionId = key;
        }
        return success(m, sessionId);
    }

    private static Object run(State.Swapper swapper, String sessionId) {
        return run(swapper, sessionId, false);
    }

    private static ClientModel success(State m, String sessionId) {
        return m.getClientModel(sessionId);
    }

    private static Map error(E.BaseException e) {
        return Server.error(e.getCode(), e.getMessage());
    }

    public static Object register(String username, String password) {
        return run((state) -> {
            if (state.userExists(username)) {
                throw new E.UserExistsException();
            }
            return state.createUser(username, password)
                .createSession(username);
        }, username, true);
    }

    public static Object login(String username, String password) {
        return run((state) -> {
                state.authenticate(username, password);
                return state.createSession(username);
        }, username, true);
    }

    public static Object create(String sessionId) {
        return run((state) -> {
            state.authenticate(sessionId);
            return state.createGame(sessionId);
        }, sessionId);
    }

    public static Object join(String sessionId, String gameId) {
        return run((state) -> {
            state.authenticate(sessionId);
            return state.joinGame(sessionId, gameId);
        }, sessionId);
    }

    public static Object leave(String sessionId){
        return run((state) -> {
                state.authenticate(sessionId);
                return state.leaveGame(sessionId);
            }, sessionId);
    }

    public static Object start(String sessionId){
        return run((state) -> {
            state.authenticate(sessionId);
            return state.startGame(sessionId);
        }, sessionId);
    }

    public static Object chat(String sessionId, String message){

        return run((state) -> {
            state.authenticate(sessionId);

            // append the username to the message
            User u = state.getUserBySessionId(sessionId);
            StringBuilder sb = new StringBuilder();
            sb.append(u.data.get("name"));
            sb.append(": ");
            sb.append(message);

            return state.chat(sb.toString());
        }, sessionId);
    }

    public static Object returnDest(String sessionId,
            DestinationCard card) {
        return run((state) -> {
            state.authenticate(sessionId);
            return state.returnDest(sessionId, card);
        }, sessionId);
    }

    public static Object state(String sessionId) {
        return success(State.getState(), sessionId);
    }

    public static Object clear() {
        // For testing purposes. Hopefully none of our users find out
        // about this endpoint.
        State.swap((state) -> new State());
        return new HashMap();
    }
}
