package ticket;

import shared.ClientModel;
import shared.DestinationCard;

import java.util.HashMap;
import java.util.UUID;

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

            Object[] path = new Object[] {"users", username};
            User u = new User(username, password, path);
            state = state.commit(u);
            return createSession(state, username);
        }, username, true);
    }

    public static Object login(String username, String password) {
        return run((state) -> {
            state.authenticate(username, password);
            return createSession(state, username);
        }, username, true);
    }

    public static Object create(String sessionId) {
        return run((state) -> {
            state.authenticate(sessionId);
            state.checkNoGame(sessionId);

            String gameId = UUID.randomUUID().toString();
            Object[] path = {"games", gameId};
            Game g = new Game(gameId, sessionId, false, path)
                    .addHistory(state, sessionId, "created the game");
            return state.commit(g).commit(
                    state.getSession(sessionId).setGameId(gameId));
        }, sessionId);
    }

    public static Object join(String sessionId, String gameId) {
        return run((state) -> {
            state.authenticate(sessionId);
            state.checkNoGame(sessionId);
            state.checkGameAvailable(sessionId, gameId);

            Game game = state.getGame(gameId)
                        .addSessionId(sessionId)
                        .addHistory(state, sessionId, "joined the game");

            return state.commit(game).commit(
                    state.getSession(sessionId).setGameId(gameId));
        }, sessionId);
    }

    public static Object leave(String sessionId){
        return run((state) -> {
            state.authenticate(sessionId);
            state.checkHasGame(sessionId);
            state.checkStarted(state.getGameBySession(sessionId), false);

            Session session = state.getSession(sessionId);
            Game game = state.getGameBySession(sessionId);
            state = state.commit(session.setGameId(null));
            if (game.getSessionIds().size() == 1) {
                return state.delete(game);
            } else {
                game = game.removeSessionId(sessionId)
                           .addHistory(state, sessionId, "left the game");
                return state.commit(game);
            }
        }, sessionId);
    }

    public static Object start(String sessionId){
        return run((state) -> {
            state.authenticate(sessionId);
            state.checkHasGame(sessionId);
            Game game = state.getGameBySession(sessionId);
            state.checkStarted(game, false);
            state.checkEnoughPlayers(game);

            game = game.addHistory(state, sessionId, "started the game")
                       .start();
            for (Session ses : game.getSessions(state)) {
                for (int i = 0; i < 4; i++) {
                    ses = ses.giveTrain(game.topTrain());
                    game = game.drawCard("trainDeck");
                }
                for (int i = 0; i < 3; i++) {
                    ses = ses.giveDest(game.topDest());
                    game = game.drawCard("destDeck");
                }
                state = state.commit(ses);
            }
            for (int i = 0; i < 5; i++) {
                game = game.turnFaceUp();
            }
            return state.commit(game);
        }, sessionId);
    }

    public static Object chat(String sessionId, String message){
        return run((state) -> {
            state.authenticate(sessionId);
            state.checkHasGame(sessionId);

            String name = state.getUserBySessionId(sessionId).getName();
            Game game = state.getGameBySession(sessionId)
                        .sendMessage(name + ": " + message)
                        .addHistory(state, sessionId, "sent a message");

            return state.commit(game);
        }, sessionId);
    }

    public static Object returnDest(String sessionId, DestinationCard card) {
        return run((state) -> {
            state.authenticate(sessionId);
            state.checkHasGame(sessionId);
            Session ses = state.getSession(sessionId);
            state.checkStarted(state.getGameBySession(sessionId), true);
            state.checkCanReturn(ses);
            if (card == null) {
                return state;
            }
            state.checkHasCard(ses, card);

            Game g = state.getGame(ses.getGameId())
                    .discard(card)
                    .addHistory(state, sessionId, "returned a destination card");
            return state.commit(ses.returnCard(card)).commit(g);
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

    private static State createSession(State s, String username) {
        String id = UUID.randomUUID().toString();
        Object[] path = {"sessions", id};
        return s.commit(new Session(id, username, path))
                .commit(s.getUser(username).addSessionId(id));
    }
}
