package ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class Server extends NanoHTTPD {
    private interface FacadeMethod {
        Map run();
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
            Map response = serveHelper(session);
            System.out.println(Model.getState());
            return newFixedLengthResponse(
                    new ObjectMapper().writeValueAsString(response));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return newFixedLengthResponse(
                        new ObjectMapper().writeValueAsString(
                            error(E.SERVER, "internal server error")));
            } catch (IOException ioe) {
                e.printStackTrace();
                return newFixedLengthResponse("something has gone terribly, terribly wrong");
            }
        }
    }

    private Map serveHelper(IHTTPSession session) {
        String endpoint = session.getUri();
        System.out.println(session.getMethod() + ": " + endpoint);

        Map<String, String> files = new HashMap<>();
        try {
            session.parseBody(files);
        } catch (IOException | ResponseException e) {
            return error(E.CLIENT_CODE, "couldn't parse request body");
        }

        String json = session.getQueryParameterString();
        Map body;
        try {
            body = new ObjectMapper().readValue(json, HashMap.class);
        } catch (IOException | NullPointerException e) {
            return error(E.CLIENT_CODE, "couldn't parse json from request body");
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
            } else {
                return error(E.CLIENT_CODE, "endpoint " + endpoint + " doesn't exist");
            }
        } catch (InvalidParameterException e) {
            return error(E.CLIENT_CODE, "request body doesn't contain required parameters");
        } catch (ClassCastException e) {
            return error(E.CLIENT_CODE, "request body contains arguments with invalid type");
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

    public static Map error(int code, String message) {
        return (Map)C.hashMap.invoke("code", code, "message", message);
    }
}
