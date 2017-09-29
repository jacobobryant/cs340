package com.thefunteam.android.presenter;

import com.thefunteam.android.ClientCommunicator;
import com.thefunteam.android.NextLayerFacade;
import com.thefunteam.android.activity.LoginActivity;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;

import java.util.Observable;
import java.util.Observer;

public class LoginPresenter extends Presenter {

    public static String mockData = "{\n" +
            "  \"sessionId\": \"abcd\",\n" +
            "  \"availableGames\": [\n" +
            "    {\n" +
            "      \"gameId\": \"xyz\",\n" +
            "      \"started\": false,\n" +
            "      \"players\": [\n" +
            "        \"Bob\",\n" +
            "        \"Jim\"\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"gameId\": \"pqr\",\n" +
            "      \"started\": false,\n" +
            "      \"players\": [\n" +
            "        \"Joe\",\n" +
            "        \"Jill\"\n" +
            "      ]\n" +
            "    },\n" +
            "\n" +
            "    {\n" +
            "      \"gameId\": \"asd\",\n" +
            "      \"started\": false,\n" +
            "      \"players\": [\n" +
            "        \"Joe\",\n" +
            "        \"Jill\",\n" +
            "        \"Bill\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"currentGame\": null\n" +
            "}";

    private final LoginActivity loginActivity;

    public LoginPresenter(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    public void login(String username, String password) {
        ClientCommunicator.getInstance().mockData = mockData;
        NextLayerFacade.getInstance().login(username, password);
    }


    @Override
    public void update(Observable observable, Object o) {
        update();
    }

    public void update() {
        Model model = Atom.getInstance().getModel();

        if (model.getSessionId() != null) {
            this.loginActivity.presentAvailableGames();
        }
    }
}
