package ticket;

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
        this(C.readString.invoke("{\"users\" {}}"));
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
}
