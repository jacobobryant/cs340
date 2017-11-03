package ticket;

import shared.ClientModel;
import shared.DestinationCard;

import java.util.HashMap;

public class Facade {
    private static Object run(State.Swapper swapper, String key, boolean getByUsername) {
        State m;
        try {
            m = State.swap(swapper);
        } catch (BadJuju e) {
            return e.toMap();
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

    public static Object register(String username, String password) {
        return run((state) -> {
            state.checkUsernameAvailable(username);
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
            state.checkNoGame(sessionId);
            return state.createGame(sessionId);
        }, sessionId);
    }

    public static Object join(String sessionId, String gameId) {
        return run((state) -> {
            state.authenticate(sessionId);
            state.checkNoGame(sessionId);
            state.checkGameAvailable(sessionId, gameId);
            return state.joinGame(sessionId, gameId);
        }, sessionId);
    }

    public static Object leave(String sessionId){
        return run((state) -> {
                state.authenticate(sessionId);
                state.checkHasGame(sessionId);
                state.checkStarted(state.getGameBySession(sessionId), false);
                return state.leaveGame(sessionId);
            }, sessionId);
    }

    public static Object start(String sessionId){
        return run((state) -> {
            state.authenticate(sessionId);
            state.checkHasGame(sessionId);
            Game game = state.getGameBySession(sessionId);
            state.checkStarted(game, false);
            state.checkEnoughPlayers(game);
            return state.startGame(sessionId);
        }, sessionId);
    }

    public static Object chat(String sessionId, String message){

        return run((state) -> {
            state.authenticate(sessionId);
            state.checkHasGame(sessionId);


            // append the username to the message
            User u = state.getUserBySessionId(sessionId);
            StringBuilder sb = new StringBuilder();
            sb.append(u.data.get("name"));
            sb.append(": ");
            sb.append(message);

            return state.chat(sessionId, sb.toString());
        }, sessionId);
    }

    public static Object returnDest(String sessionId, DestinationCard card) {
        return run((state) -> {
            state.authenticate(sessionId);
            state.checkHasGame(sessionId);
            Session s = state.getSession(sessionId);
            state.checkStarted(state.getGameBySession(sessionId), true);
            state.checkCanReturn(s);
            state.checkHasCard(s, card);
            return state.returnDest(sessionId, card);
        }, sessionId);
    }

    public static Object state(String sessionId) {
        State s = State.getState();
        try {
            s.authenticate(sessionId);
        } catch (BadJuju e) {
            return e.toMap();
        }
        return success(s, sessionId);
    }

    public static Object clear() {
        // For testing purposes. Hopefully none of our users find out
        // about this endpoint.
        State.swap((state) -> new State());
        return new HashMap();
    }
}
