package com.thefunteam.android.presenter;

import com.thefunteam.android.NextLayerFacade;
import com.thefunteam.android.activity.RejoinableGameActivity;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;

import java.util.Observable;

public class RejoinableGamePresenter extends  Presenter{
    RejoinableGameActivity Ractivity;

    public RejoinableGamePresenter(RejoinableGameActivity ractivity) {
        super();
        Ractivity = ractivity;
    }

    @Override
    public void update(Observable observable, Object o) {
        Ractivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Model model = Atom.getInstance().getModel();

                if(model.getSessionId() == null){
                    Atom.reset();
                    Ractivity.finish();
                }
                else if (model.getCurrentGame() != null){
                    Ractivity.showGameView();
                }
                Ractivity.update(model);
                if(model.getErrorMessage() != null) {
                    Ractivity.showError(model.getErrorMessage());
                }
            }
        });
    }

    public void rejoinGame(String sessionId){
        Atom.getInstance().getModel().setSessionId(sessionId);
    }

}
