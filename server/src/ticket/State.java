package ticket;

import shared.AvailableGame;
import shared.ClientModel;
import shared.DestinationCard;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds the global state. Includes methods that provide interaction between individually
 * models and the entire model.
 */
public class State {
    private static final Object globalState = C.atom.invoke(new State());
    private final Object state;

    public interface Swapper {
        State swap(State oldState);
    }

    public State() {
        this(C.readString.invoke("{\"users\" {}, \"games\" {}, \"sessions\" {}}"));
    }

    public State(Object state) {
        this.state = state;
    }

    public State commit(BaseModel model) {
        return new State(C.assocIn.invoke(this.state, model.path, model.data));
    }

    public String toString() {
        return state.toString();
    }

    public void pprint() {
        C.pprint.invoke(state);
    }

    public static State swap(Swapper swapper) {
        return (State)C.swap.invoke(globalState, C.swapperToFn, swapper);
    }

    public static State getState() {
        return (State)C.deref.invoke(globalState);
    }

    private boolean exists(Object... path) {
        return C.getIn.invoke(state, path) != null;
    }

    private State delete(BaseModel bm) {
        return new State(C.dissocIn.invoke(this.state, bm.path));
    }

    // USER
    public State createUser(String name, String password) {
        return commit(new User(name, password, userPath(name)));
    }

    public User getUser(String name) {
        Object[] path = userPath(name);
        return new User((Map)C.getIn.invoke(state, path), path);
    }

    public boolean userExists(String name) {
        return exists("users", name);
    }

    private Object[] userPath(String name) {
        return new Object[] {"users", name};
    }

    public User getUserBySessionId(String sessionId) {
        return getUser(getSession(sessionId).getUsername());
    }


    // SESSION
    public State createSession(String username) {
        String id = UUID.randomUUID().toString();
        Object[] path = {"sessions", id};
        return commit(new Session(id, username, path))
              .commit(getUser(username).addSessionId(id));
    }

    public String getNewestSession(String username) {
        return (String)C.last.invoke(getUser(username).getSessionIds());
    }

    public Session getSession(String sessionId) {
        Object[] path = {"sessions", sessionId};
        return new Session((Map)C.getIn.invoke(state, path), path);
    }

    // GAME
    public State createGame(String sessionId) {
        String gameId = UUID.randomUUID().toString();
        Object[] path = {"games", gameId};
        Game g = new Game(gameId, sessionId, false, path);

        // create game history
        StringBuilder sb = new StringBuilder();
        User u = this.getUserBySessionId(sessionId);
        sb.append(u.data.get("name"));
        sb.append(" created the game");

        return commit(g.addHistory(sb.toString()))
              .commit(getSession(sessionId).setGameId(gameId));
    }

    public State joinGame(String sessionId, String gameId) {
        Game game = getGame(gameId);

        // create game history
        StringBuilder sb = new StringBuilder();
        User u = this.getUserBySessionId(sessionId);
        sb.append(u.data.get("name"));
        sb.append(" joined the game");

        return commit(game.addSessionId(sessionId).addHistory(sb.toString()))
              .commit(getSession(sessionId).setGameId(gameId));
    }

    public State leaveGame(String sessionId) {
        Session session = getSession(sessionId);
        Game game = getGameBySession(sessionId);
        State m = commit(session.setGameId(null));
        if (game.getSessionIds().size() == 1) {
            return m.delete(game);
        } else {
            // create game history
            StringBuilder sb = new StringBuilder();
            User u = this.getUserBySessionId(sessionId);
            sb.append(u.data.get("name"));
            sb.append(" left the game");

            return m.commit(game.removeSessionId(sessionId)
                                .addHistory(sb.toString()));
        }
    }

    public State startGame(String sessionId){
        Game game = getGameBySession(sessionId);

        // create game history
        StringBuilder sb = new StringBuilder();
        User u = this.getUserBySessionId(sessionId);
        sb.append(u.data.get("name"));
        sb.append(" started the game");

        return game.addHistory(sb.toString()).start(this);
    }

    public State chat(String sessionId, String message){
        Game game = getGameBySession(sessionId);

        // create game history
        StringBuilder sb = new StringBuilder();
        User u = this.getUserBySessionId(sessionId);
        sb.append(u.data.get("name"));
        sb.append(" sent a message");

        return commit(game.sendMessage(message).addHistory(sb.toString()));
    }

    public Game getGameBySession(String sessionId) {
        return getGame(getSession(sessionId).getGameId());
    }

    public Game getGame(String gameId) {
        Object[] path = {"games", gameId};
        return new Game((Map)C.getIn.invoke(state, path), path);
    }

    public List<Game> getGames() {
        return ((Set<String>)((Map)C.get.invoke(state, "games")).keySet()).stream()
                .map((gameId) -> getGame(gameId)).collect(Collectors.toList());
    }

    public List<AvailableGame> getAvailableGames(String sessionId) {
        return getGames().stream()
            .filter((game) -> game.isAvailable(getUserBySessionId(sessionId)))
            .map((game) -> game.getAvailableModel(this))
            .collect(Collectors.toList());
    }

    // TODO make it so after the user returns a null card, we don't let them hit
    // the endpoint again
    public State returnDest(String sessionId, DestinationCard card) {
        if (card == null) {
            return this;
        }
        Session ses = getSession(sessionId);

        // create game history
        StringBuilder sb = new StringBuilder();
        User u = this.getUserBySessionId(sessionId);
        sb.append(u.data.get("name"));
        sb.append(" returned a destination card");

        return commit(ses.returnCard(card))
              .commit(getGame(ses.getGameId()).discard(card)
                      .addHistory(sb.toString()));
    }

    // OTHER
    public ClientModel getClientModel(String sessionId) {
        String gameId = getSession(sessionId).getGameId();
        List<AvailableGame> availableGames = null;
        shared.Game currentGame = null;
        if (gameId == null) {
            availableGames = getAvailableGames(sessionId);
        } else {
            currentGame = getGame(gameId).getCurrentModel(sessionId, this);
        }

        return new ClientModel(sessionId, availableGames, currentGame);
    }

    // PRECONDITION CHECKERS

    public void authenticate(String username, String password) {
        if (!userExists(username) || !getUser(username).getPassword().equals(password)) {
            throw new BadJuju("Invalid username/password combination");
        }
    }

    public void authenticate(String sessionId) {
        if (!exists("sessions", sessionId)) {
            throw new BadJuju("Invalid session ID");
        }
    }

    public void checkUsernameAvailable(String username) {
        if (userExists(username)) {
            throw new BadJuju("That username has already been taken");
        }
    }

    public void checkNoGame(String sessionId) {
        if (exists("sessions", sessionId, "gameId")) {
            throw new BadJuju("Session is already part of a game");
        }
    }

    public void checkGameAvailable(String sessionId, String gameId) {
        if (!exists("games", gameId)) {
            throw new BadJuju("Game isn't available");
        }
        Game game = getGame(gameId);
        if (!game.isAvailable(getUserBySessionId(sessionId))) {
            throw new BadJuju("Game isn't available");
        }
    }

    public void checkHasGame(String sessionId) {
        Session session = getSession(sessionId);
        String gameId = session.getGameId();
        if (gameId == null) {
            throw new BadJuju("Session isn't part of a game");
        }
    }

    public void checkStarted(Game game, boolean shouldBeStarted) {
        if (game.started() != shouldBeStarted) {
            throw new BadJuju("Game " +
                              ((shouldBeStarted) ? "not" : "already") +
                              " started");
        }
    }

    public void checkEnoughPlayers(Game game) {
        if (game.getSessionIds().size() < 2) {
            throw new BadJuju("Not enough players to start");
        }
    }

    public void checkCanReturn(Session s) {
        if (s.getDestCards().size() < 3) {
            throw new BadJuju("Already returned card");
        } 
    }

    public void checkHasCard(Session s, DestinationCard card) {
        if (card != null && !s.getDestCards().contains(card)) {
            throw new BadJuju("Player doesn't have that card");
        }
    }
}
