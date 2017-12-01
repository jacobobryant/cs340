package ticket;

import clojure.lang.IFn;
import shared.model.*;

import java.util.*;
import java.util.stream.Collectors;

import static shared.model.TrainType.any;

/**
 * Holds the global state. Includes methods that provide interaction between individually
 * models and the entire model.
 */
public class State {
    private static final Object globalState = C.atom.invoke(new State());
    private final Object state;

    public interface ISwapFN {
        State swap(State oldState, String endpoint, String json);
    }

    public State() {
        this(C.readString.invoke("{\"users\" {}, \"games\" {}, \"sessions\" {}, \"eventId\" 0}"));
    }

    public State(int eventId) {
        this(C.assoc.invoke(new State().state, "eventId", eventId));
    }

    public State(Object state) {
        this.state = state;
    }

    public State commit(BaseModel... models) {
        Object data = this.state;
        for (BaseModel m : models) {
            data = C.assocIn.invoke(data, m.path, m.data);
        }
        return new State(data);
    }

    public String toString() {
        return state.toString();
    }

    public void pprint() {
        C.pprint.invoke(state);
    }

    public static State swap(IFn swapfn, String endpoint, String json) {
        return (State)C.swap.invoke(globalState, swapfn, endpoint, json);
    }

    public static void deserialize(Object blob) {
        C.reset.invoke(globalState, new State(blob));
    }

    public Object serialize() {
        return state;
    }

    public int getLatestEventId() {
        return C.castInt(C.get.invoke(state, "eventId"));
    }

    public static State getState() {
        return (State)C.deref.invoke(globalState);
    }

    public State incrementEventId() {
        return new State(C.update.invoke(state, "eventId", C.inc));
    }

    private boolean exists(Object... path) {
        return C.getIn.invoke(state, path) != null;
    }

    public State delete(BaseModel bm) {
        return new State(C.dissocIn.invoke(this.state, bm.path));
    }

    public User getUser(String name) {
        Object[] path = new Object[] {"users", name};
        return new User((Map)C.getIn.invoke(state, path), path);
    }

    public boolean userExists(String name) {
        return exists("users", name);
    }

    public User getUserBySessionId(String sessionId) {
        return getUser(getSession(sessionId).getUsername());
    }

    public String getNewestSession(String username) {
        return (String)C.last.invoke(getUser(username).getSessionIds());
    }

    public Session getSession(String sessionId) {
        Object[] path = {"sessions", sessionId};
        return new Session((Map)C.getIn.invoke(state, path), path);
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

    public List<RejoinableGame> getRejoinableGames(String sessionId) {
        User u = getUserBySessionId(sessionId);
        return getGames().stream()
            .filter((game) -> game.isRejoinable(u))
            .map((game) -> game.getRejoinableModel(this, u))
            .collect(Collectors.toList());
    }

    public ClientModel getClientModel(String sessionId) {
        String gameId = getSession(sessionId).getGameId();
        List<AvailableGame> availableGames = null;
        List<RejoinableGame> rejoinableGames = null;
        shared.Game currentGame = null;
        
        if (gameId == null) {
            availableGames = getAvailableGames(sessionId);
            rejoinableGames = getRejoinableGames(sessionId);
        } else {
            currentGame = getGame(gameId).getCurrentModel(sessionId, this);
        }

        return new ClientModel(sessionId, availableGames, rejoinableGames, currentGame);
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
        if (!s.getDestCards().contains(card)) {
            throw new BadJuju("Player doesn't have that card");
        }
    }

    public void checkTurnState(String sessionId, TurnState... states) {
        authenticate(sessionId);
        TurnState ts = getSession(sessionId).getTurnState();
        if (!Arrays.asList(states).contains(ts)) { 
            throw new BadJuju("Wrong turn state");
        }
    }

    public void checkDestDeckNotEmpty(Game game) {
        if (game.getDestDeck().size() == 0) {
            throw new BadJuju("Destination card deck is empty");
        }
    }

    public void checkHasPending(Session ses, DestinationCard[] cards) {
        List<DestinationCard> list = new ArrayList<>(ses.getPendingDestCards());
        if (cards == null) {
            throw new BadJuju("cards can't be null");
        }

        for (DestinationCard card : cards) {
            if (!list.remove(card)) {
                throw new BadJuju("returned cards aren't pending");
            }
        }
    }

    public void checkTrainDeckNotEmpty(Game game) {
        if (game.getTrainDeck().size() == 0) {
            throw new BadJuju("Train card deck is empty");
        }

    }

    public void checkValidFaceupIndex(Game game, int index) {
        if (index < 0 || index >= game.getFaceUpDeck().size()) {
            throw new BadJuju("invalid faceup card index");
        }
    }

    public void checkClaimable(Game g, Route r) {
        if (!g.getOpenRoutes().contains(r) ||
                (g.getSessionIds().size() < 4 && r.hasDouble() &&
                 !g.getOpenRoutes().contains(r.fooDouble()))) {
            throw new BadJuju("that route is unclaimable");
        }
    }

    public void checkCanClaim(Session s, Route r, List<TrainType> cards) {
        final List<TrainType> nonlocos = cards.stream().filter((card) ->
                !card.equals(any)).collect(Collectors.toList());
        if (!nonlocos.stream().allMatch((card) -> card.match(nonlocos.get(0)) &&
                                             card.match(r.type))
                || !C.containsAll(s.getTrainCards(), cards)
                || cards.size() != r.length
                || s.getTrainsLeft() < r.length
                || s.getRoutes().contains(r.fooDouble())) {
            throw new BadJuju("You can't claim that route with those cards");
        }
    }
}
