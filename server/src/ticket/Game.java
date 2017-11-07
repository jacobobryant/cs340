package ticket;

import shared.AvailableGame;
import shared.DestinationCard;
import shared.Player;
import shared.Route;
import shared.TrainType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Game extends BaseModel {
            
    public Game(Map data, Object[] path) {
        super(data, path);
    }

    public Game(String gameId, String sessionId, boolean started, Object[] path) {
        super(new Object[] {"gameId", gameId, 
                            "sessionIds", C.vector.invoke(sessionId),
                            "started", started,
                            "trainDeck", C.shuffle.invoke(TrainType.DECK),
                            "faceUpDeck", C.vector.invoke(),
                            "trainDiscard", C.vector.invoke(),
                            "destDeck", C.shuffle.invoke(DestinationCard.DECK),
                            "openRoutes", Route.ROUTES,
                            "messages", C.vector.invoke(),
                            "history", C.vector.invoke(),
                            "turn", -1,
                            "longestRouteHolder", null},
                path);
    }

    public shared.Game getCurrentModel(String curSession, State state) {
        List<Player> players = getSessionIds().stream()
            .map((sessionId) -> state.getSession(sessionId)
                        .getClientModel(sessionId.equals(curSession)))
            .collect(Collectors.toList());

        String holder = getLongestRouteHolder();
        String username = (holder == null) ? null
                    : state.getSession(holder).getUsername();

        return new shared.Game(players, getTrainDeck().size(),
                getFaceUpDeck(), getDestDeck().size(), 
                getOpenRoutes(), getMessages(), getHistory(),
                username, started());
    }

    public shared.AvailableGame getAvailableModel(State state) {
        List<String> players = getSessionIds().stream()
            .map((sessionId) -> state.getSession(sessionId).getUsername())
            .collect(Collectors.toList());
        return new AvailableGame(getGameId(), players);
    }

    public String getLongestRouteHolder() {
        return (String)data.get("longestRouteHolder");
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

    public String getGameId() {
        return (String)data.get("gameId");
    }

    public boolean started() {
        return (boolean)data.get("started");
    }

    public Game start() {
        return new Game(set("started", true), path);
    }

    public Game turnFaceUp() {
        return new Game(update("faceUpDeck", C.conj, topTrain()), path)
                    .drawCard("trainDeck");
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

    public Game drawCard(String deck) {
        return new Game(update(deck, C.subvec, 1), path);
    }

    public Game discard(DestinationCard[] cards) {
        List lcards = Arrays.asList(cards);
        C.println.invoke("lcards");
        C.pprint.invoke(lcards);
        System.out.println(lcards);
        return new Game(update("destDeck", C.vconcat, lcards), path);
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
                ids.stream().filter(u.getSessionIds()::contains)
                .collect(toList()).size() == 0);
    }
}
