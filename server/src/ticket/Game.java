package ticket;

import shared.model.AvailableGame;
import shared.model.DestinationCard;
import shared.model.Player;
import shared.model.Route;
import shared.model.TrainType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static shared.model.TrainType.any;

public class Game extends BaseModel {
            
    public Game(Map data, Object[] path) {
        super(data, path);
    }

    public Game(String gameId, String sessionId, boolean started, Object[] path) {
        super(new Object[] {"gameId", gameId, 
                            "sessionIds", C.vector.invoke(sessionId),
                            "started", started,
                            "trainDeck", C.shuffle.invoke(TrainType.DECK, sessionId.hashCode()),
                            "faceUpDeck", C.vector.invoke(),
                            "trainDiscard", C.vector.invoke(),
                            "destDeck", C.shuffle.invoke(DestinationCard.DECK, sessionId.hashCode()),
                            "openRoutes", Route.ROUTES,
                            "messages", C.vector.invoke(),
                            "turnsLeft", 666,
                            "history", C.vector.invoke()},
                path);
    }

    public shared.model.Game getCurrentModel(String curSession, State state) {
        List<Player> players = getSessionIds().stream()
            .map((sessionId) -> state.getSession(sessionId)
                        .getClientModel(sessionId.equals(curSession),
                            getTurnsLeft() <= 0))
            .collect(Collectors.toList());

        return new shared.model.Game(players, getTrainDeck().size(),
                getFaceUpDeck(), getDestDeck().size(), 
                getOpenRoutes(), getMessages(), getHistory(),
                started(), getTurnsLeft());
    }

    public AvailableGame getAvailableModel(State state) {
        List<String> players = getSessionIds().stream()
            .map((sessionId) -> state.getSession(sessionId).getUsername())
            .collect(Collectors.toList());
        return new AvailableGame(getGameId(), players);
    }

    public int getTurnsLeft() {
        return C.castInt(data.get("turnsLeft"));
    }

    public List<String> getHistory() {
        return (List)data.get("history");
    }

    public List<String> getMessages() {
        return (List)data.get("messages");
    }

    public List<Route> getOpenRoutes() {
        return (List)data.get("openRoutes");
    }

    public List<TrainType> getFaceUpDeck() {
        return (List)data.get("faceUpDeck");
    }

    public List<TrainType> getTrainDeck() {
        return (List)data.get("trainDeck");
    }

    public List<DestinationCard> getDestDeck() {
        return (List)data.get("destDeck");
    }

    public List<TrainType> getDiscard() {
        return (List)data.get("trainDiscard");
    }

    public String getGameId() {
        return (String)data.get("gameId");
    }

    public boolean started() {
        return (boolean)data.get("started");
    }

    public Game start() {
        return new Game(set("started", true), path);
    }

    public Game discardFaceUp(int index) {
        return new Game(update("faceUpDeck", C.removeAt, index), path)
                .shuffleDiscardIfNeeded()
                .fillFaceUp();
    }

    public boolean canDrawAgain() {
        int faceupNonLocomotives = (int)(getFaceUpDeck().stream()
                    .filter((type) -> !type.equals(any)).count());
        return (getTrainDeck().size() + faceupNonLocomotives > 0);
    }

    public Game fillFaceUp() {
        Game g = this;
        while (g.getFaceUpDeck().size() < 5 && g.getTrainDeck().size() > 0) {
            g = g.turnFaceUp();

            int nLocomotives = (int)(g.getFaceUpDeck().stream()
                    .filter((type) -> type.equals(any)).count());
            int nTotalNonLocomotives = (int)(((List)C.vconcat.invoke(
                    g.getFaceUpDeck(), g.getTrainDeck(), g.getDiscard()))
                    .stream().filter((type) -> !type.equals(any)).count());
            if (nLocomotives >= 3 && nTotalNonLocomotives > 2) {
                Object data = C.update.invoke(g.data, "trainDiscard",
                        C.vconcat, g.getFaceUpDeck());
                data = C.assoc.invoke(data, "faceUpDeck", C.vector.invoke());
                g = new Game((Map)data, g.path);
            }
            g = g.shuffleDiscardIfNeeded();
        }
        return g;
    }

    private Game turnFaceUp() {
        return new Game(update("faceUpDeck", C.conj, topTrain()), path)
                    .drawTrainCard();
    }

    private Game shuffleDiscardIfNeeded() {
        if (getTrainDeck().size() == 0 && getFaceUpDeck().size() > 0) {
            List<TrainType> newTrainDeck = (List)C.vconcat.invoke(
                    getTrainDeck(), C.shuffle.invoke(getDiscard(), data.hashCode()));
            Object data = C.assoc.invoke(this.data, "trainDeck", newTrainDeck);
            data = C.assoc.invoke(data, "trainDiscard", C.vector.invoke());
            return new Game((Map)data, path);
        }
        return this;
    }

    public List<Session> getSessions(State state) {
        return getSessionIds().stream()
            .map((sessionId) -> state.getSession(sessionId))
            .collect(Collectors.toList());
    }

    public TrainType topTrain() {
        return getTrainDeck().get(0);
    }

    public DestinationCard topDest() {
        return getDestDeck().get(0);
    }

    public Game drawTrainCard() {
        return drawCard("trainDeck").shuffleDiscardIfNeeded();
    }

    public Game drawCard(String deck) {
        return new Game(update(deck, C.subvec, 1), path);
    }

    public Game discard(DestinationCard[] cards) {
        return new Game(update("destDeck", C.vconcat, Arrays.asList(cards)), path);
    }

    public Game claim(Route r, List<TrainType> cards) {
        Object data = this.data;
        data = C.update.invoke(data, "trainDiscard", C.vconcat, cards);
        data = C.update.invoke(data, "openRoutes", C.vecrm, r);
        return new Game((Map)data, path).shuffleDiscardIfNeeded();
    }

    public Game sendMessage(String message){return new Game(update("messages", C.conj, message), path);}

    public Game addHistory(String item){return new Game(update("history", C.conj, item), path);}

    public Game addHistory(State s, String sessionId, String action) {
        User u = s.getUserBySessionId(sessionId);
        String message = u.getName() + " " + action;
        return new Game(update("history", C.conj, message), path);
    }

    public List<String> getSessionIds() {
        return (List<String>)data.get("sessionIds");
    }

    public Game addSessionId(String sessionId) {
        return new Game(update("sessionIds", C.conj, sessionId), path);
    }

    public Game removeSessionId(String sessionId) {
        return new Game(remove("sessionIds", sessionId), path);
    }

    public boolean isAvailable(User u) {
        List<String> ids = getSessionIds();
        return (!started() && ids.size() < 5 &&
                ids.stream().noneMatch(u.getSessionIds()::contains));
    }

    public Game decrementTurnsLeft() {
        return new Game(update("turnsLeft", C.dec), path);
    }

    public Game setLastRound() {
        return new Game(set("turnsLeft", getSessionIds().size() + 1), path);
    }
}
