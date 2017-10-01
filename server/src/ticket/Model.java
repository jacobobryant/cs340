package ticket;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Holds the global state. Includes methods that provide interaction between individually
 * models and the entire model.
 */
public class Model {
    private static final Object globalState = C.atom.invoke(new Model());
    private final Object state;

    public interface Swapper {
        Model swap(Model oldState);
    }

    private Model() {
        this(C.readString.invoke("{\"users\" {}, \"games\" {}, \"sessions\" {}}"));
    }

    public Model(Object state) {
        this.state = state;
    }

    public Model commit(BaseModel model) {
        return new Model(C.assocIn.invoke(this.state, model.path, model.data));
    }

    public String toString() {
        return this.state.toString();
    }

    public static Model swap(Swapper swapper) {
        return (Model)C.swap.invoke(globalState, C.swapperToFn, swapper);
    }

    public static Model getState() {
        return (Model)C.deref.invoke(globalState);
    }

    private boolean exists(Object... path) {
        return C.getIn.invoke(state, path) != null;
    }

    // USER
    public Model createUser(String name, String password) {
        return commit(new User(name, password, userPath(name)));
    }

    public User getUser(String name) {
        Object[] path = userPath(name);
        return new User((Map)C.getIn.invoke(state, path), path);
    }

    public void authenticate(String username, String password) {
        if (!userExists(username) || !getUser(username).getPassword().equals(password)) {
            throw new E.LoginException();
        }
    }

    public void authenticate(String sessionId) {
        if (!exists("sessions", sessionId)) {
            throw new E.SessionException();
        }
    }

    public boolean userExists(String name) {
        return exists("users", name);
    }

    private Object[] userPath(String name) {
        return new Object[] {"users", name};
    }

    // SESSION
    public Model createSession(String username) {
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
    public Model createGame(String sessionId) {
        if (exists("sessions", sessionId, "gameId")) {
            throw new E.HasGameException();
        }
        String gameId = UUID.randomUUID().toString();
        Object[] path = {"games", gameId};
        return commit(new Game(gameId, sessionId, false, path))
              .commit(getSession(sessionId).setGameId(gameId));
    }

    public Model joinGame(String sessionId, String gameId){
        if (exists("sessions", sessionId, "gameId")){
            throw new E.HasGameException();
        }
        return commit(this.getGame(gameId))
            .commit(getSession(sessionId).setGameId(gameId));
    }

    public Model startGame(String sessionId, String gameId){
        return commit(this.getGame(gameId));
    }

    public Game getGameBySession(String sessionId) {
        return getGame(getSession(sessionId).getGameId());
    }

    public Game getGame(String gameId) {
        Object[] path = {"games", gameId};
        return new Game((Map)C.getIn.invoke(state, path), path);
    }

    public List<String> getPlayerNames(String gameId) {
        return getGame(gameId).getSessionIds().stream()
            .map((sessionId) -> getSession(sessionId).getUsername())
            .collect(Collectors.toList());
    }
}
