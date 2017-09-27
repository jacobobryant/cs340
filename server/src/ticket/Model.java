package ticket;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        this(C.readString.invoke("{\"users\" {}, \"games\" []}"));
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

    private int count(Object... path) {
        return ((List)C.getIn.invoke(state, path)).size();
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
        if (!userExists(username)) {
            throw new E.LoginException();
        }
        if (!getUser(username).getPassword().equals(password)) {
            throw new E.LoginException();
        }
    }

    public void authenticate(String sessionId) {
        // TODO
    }

    public boolean userExists(String name) {
        return C.getIn.invoke(state, userPath(name)) != null; 
    }

    private Object[] userPath(String name) {
        return new Object[] {"users", name};
    }

    // SESSION
    public Model createSession(String username) {
        String id = UUID.randomUUID().toString();
        int size = getUser(username).countSessions();
        Object[] path = {"users", username, "sessions", size};
        return commit(new Session(id, path));
    }

    public Session getNewestSession(String username) {
        int lastIndex = getUser(username).countSessions() - 1;
        Object[] path = {"users", username, "sessions", lastIndex};
        return new Session((Map)C.getIn.invoke(state, path), path);
    }

    // GAME
    public Model createGame(String sessionId) {
        if (getGame(sessionId) != null) {
            throw new E.HasGameException();
        }
        String gameId = UUID.randomUUID().toString();
        Object[] path = {"games", count("games")};
        return commit(new Game(gameId, sessionId, false, path));
    }

    public Game getGame(String sessionId) {
        int size = count("games");
        for (int position = 0; position < size; position++) {
            Object[] path = {"games", position};
            Game game = new Game((Map)C.getIn.invoke(state, path), path);
            if (game.hasPlayer(sessionId)) {
                return game;
            }
        }
        return null;
    }
}
