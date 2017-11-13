package ticket;

import clojure.lang.IFn;
import com.google.gson.Gson;
import shared.command.*;
import shared.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static shared.model.TrainType.any;
import static shared.model.TurnState.*;

public class Facade {
    private static class Cmd {
        String username;
        String sessionId;
    }

    public static final IFn swapfn = (IFn)C.swapperToFn.invoke((State.ISwapFN)
            (State state, String endpoint, String json) ->
                dispatch(state, endpoint, json).incrementEventId());

    public static Object handle(String endpoint, String json) {
        List<String> readOnlyEndpoints = (List)C.vector.invoke("/state");
        State s;

        if (readOnlyEndpoints.contains(endpoint)) {
            s = dispatch(State.getState(), endpoint, json);
        } else {
            try {
                s = State.swap(swapfn, endpoint, json);
            } catch (BadJuju e) {
                return e.toMap();
            }
        }

        if (endpoint.equals("/clear")) {
            return new HashMap();
        }

        Gson gson = new Gson();
        Cmd cmd = gson.fromJson(json, Cmd.class);
        String sessionId = (cmd.sessionId != null) ? cmd.sessionId :
                s.getNewestSession(cmd.username);

        return s.getClientModel(sessionId);
    }

    private static State dispatch(State state, String endpoint, String json) {
        Gson gson = new Gson();
        if (endpoint.equals("/register")) {
            LoginCommand cmd = gson.fromJson(json, LoginCommand.class);
            return Facade.register(state, cmd.username, cmd.password);
        } else if (endpoint.equals("/login")) {
            LoginCommand cmd = gson.fromJson(json, LoginCommand.class);
            return Facade.login(state, cmd.username, cmd.password);
        } else if (endpoint.equals("/create")) {
            UserCommand cmd = gson.fromJson(json, UserCommand.class);
            return Facade.create(state, cmd.sessionId);
        } else if (endpoint.equals("/join")) {
            GameCommand cmd = gson.fromJson(json, GameCommand.class);
            return Facade.join(state, cmd.sessionId, cmd.gameId);
        } else if (endpoint.equals("/leave")){
            UserCommand cmd = gson.fromJson(json, UserCommand.class);
            return Facade.leave(state, cmd.sessionId);
        } else if (endpoint.equals("/start")) {
            UserCommand cmd = gson.fromJson(json, UserCommand.class);
            return Facade.start(state, cmd.sessionId);
        } else if (endpoint.equals("/chat")){
            MessageCommand cmd = gson.fromJson(json, MessageCommand.class);
            return Facade.chat(state, cmd.sessionId, cmd.message);
        } else if (endpoint.equals("/return-dest")) {
            ReturnDestCommand cmd = gson.fromJson(json, ReturnDestCommand.class);
            return Facade.returnDest(state, cmd.sessionId, cmd.cards);
        } else if (endpoint.equals("/draw-dest")) {
            UserCommand cmd = gson.fromJson(json, UserCommand.class);
            return Facade.drawDest(state, cmd.sessionId);
        } else if (endpoint.equals("/draw-train")) {
            UserCommand cmd = gson.fromJson(json, UserCommand.class);
            return Facade.drawTrain(state, cmd.sessionId);
        } else if (endpoint.equals("/draw-faceup-train")) {
            FaceupTrainCommand cmd = gson.fromJson(json, FaceupTrainCommand.class);
            return Facade.drawFaceupTrain(state, cmd.sessionId, cmd.index);
        } else if (endpoint.equals("/build")) {
            BuildCommand cmd = gson.fromJson(json, BuildCommand.class);
            return Facade.build(state, cmd.sessionId, cmd.route, cmd.cards);
        } else if (endpoint.equals("/clear")) {
            return new State();
        } else if (endpoint.equals("/state")) {
            return state;
        } else {
            throw new BadJuju("endpoint " + endpoint + " doesn't exist");
        }
    }

    public static State register(State state, String username, String password) {
        state.checkUsernameAvailable(username);

        Object[] path = new Object[] {"users", username};
        User u = new User(username, password, path);
        state = state.commit(u);
        return createSession(state, username);
    }

    public static State login(State state, String username, String password) {
        state.authenticate(username, password);
        return createSession(state, username);
    }

    public static State create(State state, String sessionId) {
        state.authenticate(sessionId);
        state.checkNoGame(sessionId);

        String gameId = UUID.randomUUID().toString();
        Object[] path = {"games", gameId};
        Game g = new Game(gameId, sessionId, false, path)
                .addHistory(state, sessionId, "created the game");
        Session s = state.getSession(sessionId).setGameId(gameId);
        return state.commit(g, s);
    }

    public static State join(State state, String sessionId, String gameId) {
        state.authenticate(sessionId);
        state.checkNoGame(sessionId);
        state.checkGameAvailable(sessionId, gameId);

        Game game = state.getGame(gameId)
                    .addSessionId(sessionId)
                    .addHistory(state, sessionId, "joined the game");

        Session ses = state.getSession(sessionId).setGameId(gameId);
        return state.commit(game, ses);
    }

    public static State leave(State state, String sessionId) {
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
    }

    public static State start(State state, String sessionId) {
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
                ses = ses.givePendingDest(game.topDest());
                game = game.drawCard("destDeck");
            }
            ses = ses.setTurnState(init);
            state = state.commit(ses);
        }
        game = game.fillFaceUp();
        return state.commit(game);
    }

    public static State chat(State state, String sessionId, String message) {
        state.authenticate(sessionId);
        state.checkHasGame(sessionId);

        String name = state.getUserBySessionId(sessionId).getName();
        Game game = state.getGameBySession(sessionId)
                    .sendMessage(name + ": " + message)
                    .addHistory(state, sessionId, "sent a message");

        return state.commit(game);
    }

    public static State drawDest(State state, String sessionId) {
        state.checkTurnState(sessionId, beginning);
        Session ses = state.getSession(sessionId);
        Game game = state.getGame(ses.getGameId());
        state.checkDestDeckNotEmpty(game);

        int nCards = Math.min(3, game.getDestDeck().size());
        for (int i = 0; i < nCards; i++) {
            ses = ses.givePendingDest(game.topDest());
            game = game.drawCard("destDeck");
        }
        ses = ses.setTurnState(returnDest);
        game = game.addHistory(state, sessionId,
                "drew destination cards");
        return state.commit(game, ses);
    }

    public static State returnDest(State state, String sessionId,
            DestinationCard[] cards) {
        state.authenticate(sessionId);
        state.checkTurnState(sessionId, init, returnDest);
        Session ses = state.getSession(sessionId);
        state.checkHasPending(ses, cards);
        int max = ses.getTurnState().maxReturnCards();
        if (cards.length > max) {
            throw new BadJuju("You may return at most " + max + " card(s)");
        }

        ses = ses.returnCards(cards);
        Game game = state.getGame(ses.getGameId())
                   .discard(cards)
                   .addHistory(state, sessionId, "returned " + cards.length +
                           " destination card(s)");
        state = state.commit(ses, game);
        state = updatePoints(state, game, sessionId, false);
        return endTurn(state, sessionId);
    }

    public static State drawTrain(State state, String sessionId) {
        state.authenticate(sessionId);
        state.checkTurnState(sessionId, beginning, drawTrain);
        Session ses = state.getSession(sessionId);
        Game game = state.getGame(ses.getGameId());
        state.checkTrainDeckNotEmpty(game);

        TrainType card = game.topTrain();
        ses = ses.giveTrain(card);
        game = game.drawTrainCard()
                   .addHistory(state, sessionId, "drew a train card");
        state = state.commit(ses, game);

        if (ses.getTurnState().equals(beginning) && game.canDrawAgain()) {
            return state.commit(ses.setTurnState(drawTrain));
        }
        return endTurn(state, sessionId);
    }

    public static State drawFaceupTrain(State state, String sessionId, int index) {
        state.authenticate(sessionId);
        state.checkTurnState(sessionId, beginning, drawTrain);
        Session ses = state.getSession(sessionId);
        Game game = state.getGame(ses.getGameId());
        state.checkValidFaceupIndex(game, index);
        TrainType card = game.getFaceUpDeck().get(index);
        if (ses.getTurnState().equals(drawTrain) && card.equals(any)) {
            throw new BadJuju("Can't take locomotive on second draw");
        }

        game = game.discardFaceUp(index).addHistory(state, sessionId,
                "took a face up " + card.cardName() + " card");
        ses = ses.giveTrain(card);
        state = state.commit(game, ses);

        if (ses.getTurnState().equals(beginning) && !card.equals(any)
                && game.canDrawAgain()) {
            return state.commit(ses.setTurnState(drawTrain));
        }
        return endTurn(state, sessionId);
    }

    public static State build(State state, String sessionId, Route route, List<TrainType> cards) {
        state.authenticate(sessionId);
        state.checkTurnState(sessionId, beginning);
        Session ses = state.getSession(sessionId);
        Game game = state.getGame(ses.getGameId());
        state.checkClaimable(game, route);
        state.checkCanClaim(ses, route, cards);

        game = game.claim(route, cards)
                   .addHistory(state, sessionId, "claimed a route");
        ses = ses.claim(route, cards);
        if (game.getTurnsLeft() == 666 && ses.getTrainsLeft() < 3) {
            game = game.setLastRound();
        }
        state = state.commit(game, ses);
        state = updatePoints(state, game, sessionId, true);
        return endTurn(state, sessionId);
    }

    public static Object state(String sessionId) {
        State s = State.getState();
        try {
            s.authenticate(sessionId);
        } catch (BadJuju e) {
            return e.toMap();
        }
        return s.getClientModel(sessionId);
    }

    private static State createSession(State s, String username) {
        String id = UUID.randomUUID().toString();
        Object[] path = {"sessions", id};
        return s.commit(new Session(id, username, path),
                        s.getUser(username).addSessionId(id));
    }

    private static State endTurn(State s, String sessionId) {
        Session ses = s.getSession(sessionId);
        TurnState oldState = ses.getTurnState();

        ses = ses.setTurnState(waiting);
        s = s.commit(ses);
        Game game = s.getGame(ses.getGameId());

        if (oldState.equals(init)) {
            final State sf = s;
            if (game.getSessionIds().stream().allMatch(sid ->
                    sf.getSession(sid).getTurnState().equals(waiting))) {
                ses = s.getSession(game.getSessionIds().get(0));
                s = s.commit(ses.setTurnState(beginning));
            }
        } else {
            if (game.getTurnsLeft() != 666) {
                game = game.decrementTurnsLeft();
                s = s.commit(game);
            }
            if (game.getTurnsLeft() > 0) {
                List<String> sids = game.getSessionIds();
                int nextIndex = (sids.indexOf(sessionId) + 1) % sids.size();
                Session next = s.getSession(sids.get(nextIndex))
                                .setTurnState(beginning);
                s = s.commit(next);
            }
        }
        return s;
    }

    private static State updatePoints(State s, Game g, String sessionId,
            boolean builtRoute) {
        final State sf = s;
        int longestRouteLength = g.getSessionIds().stream().mapToInt((sid) ->
                sf.getSession(sid).getLongestRouteLength()).max().orElse(0);
        for (String sid : g.getSessionIds()) {
            boolean cur = sessionId.equals(sid);
            s = s.commit(s.getSession(sid).updatePoints(
                        builtRoute && cur,
                        !builtRoute && cur,
                        longestRouteLength));
        }
        return s;
    }
}
