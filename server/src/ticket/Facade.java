package ticket;

import java.util.Map;

public class Facade {
    public static Map register(String username, String password) {
        try {
            Model.swap((state) -> {
                if (state.userExists(username)) {
                    throw new E.UserExistsException();
                }
                return state.createUser(username, password);
            });
        } catch (E.UserExistsException e) {
            return Server.error(E.USERNAME_TAKEN,
                    "That username has already been taken");
        }
        return login(username, password);
    }

    public static Map login(String username, String password) {
        Model model;
        try {
            model = Model.swap((state) -> {
                state.authenticate(username, password);
                return state.createSession(username);
            });
        } catch (E.LoginException e) {
            return Server.error(E.LOGIN_FAILED,
                    "Invalid username/password combination");
        }
        return (Map)C.hashMap.invoke("sessionId", model.getNewestSession(username));
    }

    public static Map create(String sessionId) {
        Model model;
        try {
            model = Model.swap((state) -> {
                state.authenticate(sessionId);
                return state.createGame(sessionId);
            });
        } catch (E.SessionException e) {
            return Server.error(E.INVALID_SESSION_ID, "Invalid session ID");
        } catch (E.HasGameException e) {
            return Server.error(E.HAS_GAME, "Session is already part of a game");
        }
        Game game = model.getGameBySession(sessionId);
        return (Map)C.hashMap.invoke("currentGame",
                C.hashMap.invoke(
                    "gameId", game.getGameId(),
                    "started", game.started(),
                    "players", model.getPlayerNames(game.getGameId())));
    }

    public static Map join(String sessionId, String gameId){
        Model model;
        try {
            model = Model.swap((state) -> {
                    state.authenticate(sessionId);
                    return state.joinGame(gameId, sessionId);
                });
        } catch (E.SessionException e) {
            return Server.error(E.INVALID_SESSION_ID, "Invalid session ID");
        } catch (E.HasGameException e) {
            return Server.error(E.HAS_GAME, "Session is already part of a game");
        }
        Game game = model.getGame(gameId);
        return (Map)C.hashMap.invoke("currentGame",
                                     "gameId", game.getGameId(),
                                     "started", game.started(),
                                     "players", model.getPlayerNames(game.getGameId()));

    }

    public static Map start(String sessionId, String gameId){
        Model model;
        try {
            model = Model.swap((state) -> {
                    state.authenticate(sessionId);
                    return state.startGame(sessionId, gameId);
                });
        } catch (E.SessionException e) {
            return Server.error(E.INVALID_SESSION_ID, "Invalid session ID");
        } catch (E.HasGameException e) {
            return Server.error(E.HAS_GAME, "Session is already part of a game");
        }
        Game game = model.getGame(gameId);
        return (Map)C.hashMap.invoke("currentgame",
                                     "gameId", game.getGameId(),
                                     "started", "true",
                                     "players", model.getPlayerNames(game.getGameId()));
    }

}
