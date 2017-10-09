package com.thefunteam.android;

import com.google.gson.Gson;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.UserCommand;

import java.util.Timer;
import java.util.TimerTask;

public class Poller {
    private static Poller ourInstance = new Poller();

    public static Poller getInstance() {
        return ourInstance;
    }

    Timer timer;

    private Poller() {}

    public void startPolling() {
        timer = new Timer();
        timer.schedule(new Poll(), 0 , 1000);
    }

    public void stopPolling() {
        timer.cancel();
        timer = null;
    }

    private class Poll extends TimerTask {
        @Override
        public void run() {
            String sessionId = Atom.getInstance().getModel().getSessionId();
            if (sessionId == null) { return; }

            Gson gson = new Gson();
            ClientCommunicator.getInstance().post(
                    "/state",
                    gson.toJson(new UserCommand(sessionId))
            );
        }
    }
}
