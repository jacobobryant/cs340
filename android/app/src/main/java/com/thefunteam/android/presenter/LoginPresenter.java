package com.thefunteam.android.presenter;

import com.thefunteam.android.ClientCommunicator;
import com.thefunteam.android.NextLayerFacade;
import com.thefunteam.android.activity.LoginActivity;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;

import java.util.Observable;
import java.util.Observer;

public class LoginPresenter extends Presenter {

    private final LoginActivity loginActivity;

    public LoginPresenter(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    public void login(String username, String password) {
        NextLayerFacade.getInstance().login(username, password);
    }


    @Override
    public void update(Observable observable, Object o) {
        update();
    }

    public void update() {
        loginActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Model model = Atom.getInstance().getModel();

                if (model.getSessionId() != null) {
                    loginActivity.presentAvailableGames();
                }

                if(model.getErrorMessage() != null) {
                    loginActivity.showError(model.getErrorMessage());
                }
            }
        });
    }
}
