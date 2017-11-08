package ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import fi.iki.elonen.NanoHTTPD;
import shared.command.*;

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

        Gson gson = new Gson();
        if (endpoint.equals("/register")) {
            LoginCommand cmd = gson.fromJson(json, LoginCommand.class);
            return Facade.register(cmd.username, cmd.password);
        } else if (endpoint.equals("/login")) {
            LoginCommand cmd = gson.fromJson(json, LoginCommand.class);
            return Facade.login(cmd.username, cmd.password);
        } else if (endpoint.equals("/create")) {
            UserCommand cmd = gson.fromJson(json, UserCommand.class);
            return Facade.create(cmd.sessionId);
        } else if (endpoint.equals("/join")) {
            GameCommand cmd = gson.fromJson(json, GameCommand.class);
            return Facade.join(cmd.sessionId, cmd.gameId);
        } else if (endpoint.equals("/leave")){
            UserCommand cmd = gson.fromJson(json, UserCommand.class);
            return Facade.leave(cmd.sessionId);
        } else if (endpoint.equals("/start")) {
            UserCommand cmd = gson.fromJson(json, UserCommand.class);
            return Facade.start(cmd.sessionId);
        } else if (endpoint.equals("/state")) {
            UserCommand cmd = gson.fromJson(json, UserCommand.class);
            return Facade.state(cmd.sessionId);
        } else if (endpoint.equals("/chat")){
            MessageCommand cmd = gson.fromJson(json, MessageCommand.class);
            return Facade.chat(cmd.sessionId, cmd.message);
        } else if (endpoint.equals("/return-dest")) {
            ReturnDestCommand cmd = gson.fromJson(json, ReturnDestCommand.class);
            return Facade.returnDest(cmd.sessionId, cmd.cards);
        } else if (endpoint.equals("/draw-dest")) {
            UserCommand cmd = gson.fromJson(json, UserCommand.class);
            return Facade.drawDest(cmd.sessionId);
        } else if (endpoint.equals("/draw-train")) {
            UserCommand cmd = gson.fromJson(json, UserCommand.class);
            return Facade.drawTrain(cmd.sessionId);
        } else if (endpoint.equals("/draw-faceup-train")) {
            FaceupTrainCommand cmd = gson.fromJson(json, FaceupTrainCommand.class);
            return Facade.drawFaceupTrain(cmd.sessionId, cmd.index);
        } else if (endpoint.equals("/build")) {
            BuildCommand cmd = gson.fromJson(json, BuildCommand.class);
            return Facade.build(cmd.sessionId, cmd.route, cmd.cards);
        } else if (endpoint.equals("/clear")) {
            return Facade.clear();
        } else {
            return BadJuju.map("endpoint " + endpoint + " doesn't exist");
        }
    }
}
