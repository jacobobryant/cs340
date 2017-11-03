package ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import fi.iki.elonen.NanoHTTPD;
import shared.DestinationCard;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class Server extends NanoHTTPD {
    private interface FacadeMethod {
        Object run();
    }

    public Server() throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nServer running on http://localhost:8080/ \n");
    }

    public static void main(String[] args) {
        try {
            new Server();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            Object response = serveHelper(session);
            //System.out.println(State.getState());
            //State.getState().pprint();
            //System.out.println();
            return newFixedLengthResponse(
                    new ObjectMapper().writeValueAsString(response));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return newFixedLengthResponse(
                        new ObjectMapper().writeValueAsString(
                            BadJuju.map("internal server error")));
            } catch (IOException ioe) {
                e.printStackTrace();
                return newFixedLengthResponse("something has gone terribly, terribly wrong");
            }
        }
    }

    private Object serveHelper(IHTTPSession session) {
        String endpoint = session.getUri();
        System.out.println(session.getMethod() + ": " + endpoint);

        // read post body
        Map<String, String> files = new HashMap<>();
        try {
            session.parseBody(files);
        } catch (IOException | ResponseException e) {
            return BadJuju.map("couldn't parse request body");
        }
        String json = (files.containsKey("postData"))
            ? files.get("postData") : session.getQueryParameterString();

        // parse json
        Map body;
        try {
            body = new ObjectMapper().readValue(json, HashMap.class);
        } catch (IOException | NullPointerException e) {
            System.out.println(json);
            return BadJuju.map("couldn't parse json from request body");
        }

        // We use FacadeMethod so that invocation occurs outside the try/catch block.
        FacadeMethod method;
        try {
            if (endpoint.equals("/register")) {
                String username = (String)get(body, "username");
                String password = (String)get(body, "password");
                method = () -> Facade.register(username, password);
            } else if (endpoint.equals("/login")) {
                String username = (String)get(body, "username");
                String password = (String)get(body, "password");
                method = () -> Facade.login(username, password);
            } else if (endpoint.equals("/create")) {
                String sessionId = (String)get(body, "sessionId");
                method = () -> Facade.create(sessionId);
            } else if (endpoint.equals("/join")) {
                String sessionId = (String)get(body, "sessionId");
                String gameId = (String)get(body, "gameId");
                method = () -> Facade.join(sessionId, gameId);
            } else if (endpoint.equals("/leave")){
                String sessionId = (String)get(body, "sessionId");
                method = () -> Facade.leave(sessionId);
            } else if (endpoint.equals("/start")) {
                String sessionId = (String)get(body, "sessionId");
                method = () -> Facade.start(sessionId);
            } else if (endpoint.equals("/state")) {
                String sessionId = (String)get(body, "sessionId");
                method = () -> Facade.state(sessionId);
            } else if (endpoint.equals("/chat")){
                String sessionId = (String)get(body, "sessionId");
                String message = (String)get(body, "message");
                method = () -> Facade.chat(sessionId, message);
            } else if (endpoint.equals("/return-dest")) {
                String sessionId = (String)get(body, "sessionId");
                Map arg = (Map)body.get("dest");
                // TODO throw error if "dest" not in body?
                final DestinationCard card;
                
                if (arg != null) {
                    Gson gson = new Gson();
                    card = gson.fromJson(gson.toJsonTree(arg), DestinationCard.class);
                } else {
                    card = null;
                }
                method = () -> Facade.returnDest(sessionId, card);
            } else if (endpoint.equals("/clear")) {
                method = () -> Facade.clear();
            } else {
                return BadJuju.map("endpoint " + endpoint + " doesn't exist");
            }
        } catch (InvalidParameterException e) {
            return BadJuju.map("request body doesn't contain required parameters");
        } catch (ClassCastException e) {
            e.printStackTrace();
            return BadJuju.map("request body contains arguments with invalid type");
        }
        return method.run();
    }

    private Object get(Map m, Object key) {
        Object ret = m.get(key);
        if (ret == null) {
            throw new InvalidParameterException();
        }
        return ret;
    }
}
