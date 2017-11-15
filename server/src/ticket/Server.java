package ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.iki.elonen.NanoHTTPD;
import shared.dao.Dao;
import shared.dao.Event;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class Server extends NanoHTTPD {
    private final Dao jones;
    private final int checkpoint;

    public Server(Dao jones, int checkpoint) throws IOException {
        super(8080);
        this.jones = jones;
        this.checkpoint = checkpoint;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nServer running on http://localhost:8080/ \n");
    }

    public static void main(String[] args) {
        // parse cli args
        String persister = "SqlDao";
        int checkpoint = 10;
        if (args.length >= 1) {
            persister = args[0];
        }
        if (args.length >= 2) {
            checkpoint = Integer.parseInt(args[1]);
        }

        // add plugins to class path
        File pluginFolder = new File(System.getProperty("user.dir"))
                .getParentFile().toPath().resolve("plugins").toFile();
        try {
            URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            java.lang.reflect.Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            for (File jar : pluginFolder.listFiles()) {
                URL url = jar.toURI().toURL();
                method.invoke(classLoader, url);
            }
        } catch (MalformedURLException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // load the select plugin
        Dao jones = null;
        ServiceLoader<Dao> loader = ServiceLoader.load(Dao.class);
        for (Dao d : loader) {
            if (d.getClass().getSimpleName().equals(persister)) {
                jones = d;
                break;
            }
        }
        if (jones == null) {
            throw new RuntimeException("Couldn't load persistence provider \"" + persister + "\"");
        }

        // restore state
        Object blob = jones.loadState();
        if (blob != null) {
            State.deserialize(blob);
        }
        State s = State.getState();
        for (Event e : jones.getEventsAfter(s.getLatestEventId())) {
            Facade.handle(jones, checkpoint, e.endpoint, e.json);
        }

        // start server
        try {
            new Server(jones, checkpoint);
        } catch (IOException e) {
            System.err.println("Couldn't start server:\n" + e);
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

        return Facade.handle(this.jones, this.checkpoint, endpoint, json);
    }
}
