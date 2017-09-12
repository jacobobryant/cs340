package ticket;

import java.util.Map;

public class Data {
    private final Object state;

    public Data(Object state) {
        this.state = state;
    }

    public Data commit(BaseModel model) {
        return new Data(C.assocIn.invoke(this.state, model.path, model.data));
    }

    public String toString() {
        return this.state.toString();
    }

    // DEMO: how to integrate a model with the entire state
    public Data createUser(String name, String password, int userPosition) {
        Object[] path = new Object[] {"users", userPosition};
        return commit(new User(name, password, path));
    }

    public User getUser(int position) {
        Object[] path = new Object[] {"users", position};
        return new User((Map)C.getIn.invoke(state, path), path);
    }
    // ----------------------------------------------------
}
