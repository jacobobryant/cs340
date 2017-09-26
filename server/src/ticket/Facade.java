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
        Session session = model.getNewestSession(username);
        return (Map)C.hashMap.invoke("sessionId", session.getId());
    }
}
