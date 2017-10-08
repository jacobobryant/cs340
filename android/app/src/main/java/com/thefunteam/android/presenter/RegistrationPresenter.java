package com.thefunteam.android.presenter;

import com.thefunteam.android.NextLayerFacade;
import com.thefunteam.android.activity.RegistrationActivity;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;

import java.util.Observable;

public class RegistrationPresenter extends Presenter {

    private final RegistrationActivity registrationActivity;

    public RegistrationPresenter(RegistrationActivity registrationActivity) {
        this.registrationActivity = registrationActivity;
        Atom.getInstance().addObserver(this);
    }

    public void register(String username, String password) {
        NextLayerFacade.getInstance().register(username, password);
    }

    @Override
    public void update(Observable observable, Object o) {
        Model model = Atom.getInstance().getModel();

        if(model.getSessionId() != null) {
            this.registrationActivity.finish();
        }
    }
}
