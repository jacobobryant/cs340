package ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.iki.elonen.NanoHTTPD;
import shared.dao.Dao;
import shared.dao.Event;
import shared.dao.GeneralPurposeToolBuildingFactoryFactoryFactory;
import shared.dao.UserDao;

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
    private final UserDao udao;
    private final int checkpoint;

    public Server(Dao jones, int checkpoint, UserDao udao) throws IOException {
        super(8080);
        this.jones = jones;
        this.checkpoint = checkpoint;
        this.udao = udao;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nServer running on http://localhost:8080/ \n");
    }

    public static void main(String[] args) {
        // parse cli args
        String persister = "sql";
        int checkpoint = 10;
        boolean wipe = false;
        if (args.length >= 1) {
            persister = args[0];
        }
        if (args.length >= 2) {
            checkpoint = Integer.parseInt(args[1]);
        }
        if (args.length >= 3 && args[2].equals("wipe")) {
            wipe = true;
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

        // load the selected plugin
        GeneralPurposeToolBuildingFactoryFactoryFactory factory = null;
        ServiceLoader<GeneralPurposeToolBuildingFactoryFactoryFactory> loader =
                ServiceLoader.load(GeneralPurposeToolBuildingFactoryFactoryFactory.class);
        for (GeneralPurposeToolBuildingFactoryFactoryFactory f : loader) {
            System.out.println("found provider: " + f.getName());
            if (f.getName().equals(persister)) {
                factory = f;
                break;
            }
        }
        if (factory == null) {
            throw new RuntimeException("Couldn't find persistence provider \"" + persister + "\"");
        }
        factory.init(wipe);
        Dao jones = factory.makeGameDao();
        UserDao udao = factory.makeUserDao();

        // restore state
        Object blob = jones.loadState();
        if (blob != null) {
            State.deserialize(blob);
        }
        int eventId = State.getState().getLatestEventId();
        System.out.println("latest eventid in state: " + eventId);
        for (Event e : jones.getEventsAfter(eventId)) {
            State.swap(Facade.swapfn, e.endpoint, e.json);
        }

        // start server
        try {
            new Server(jones, checkpoint, udao);
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

        return Facade.handle(this.jones, this.checkpoint, endpoint, json, this.udao);
    }
}
