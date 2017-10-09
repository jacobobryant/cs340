package com.thefunteam.android.presenter;

import com.thefunteam.android.NextLayerFacade;
import com.thefunteam.android.activity.CurrentGameActivity;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;

import java.util.Observable;

public class CurrentGamePresenter extends Presenter {

    CurrentGameActivity currentGameActivity;

    public CurrentGamePresenter(CurrentGameActivity currentGameActivity) {
        this.currentGameActivity = currentGameActivity;

    }

    public void startGame() {
        NextLayerFacade.getInstance().startGame();
    }

    public void leaveGame() {
        NextLayerFacade.getInstance().leaveGame();
    }

    @Override
    public void update(Observable observable, Object o) {
        currentGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Model model = Atom.getInstance().getModel();

                if (model.getCurrentGame() == null) {
                    currentGameActivity.finish();
                } else if (model.getCurrentGame().isStarted()) {
                    currentGameActivity.presentGame();
                }
                currentGameActivity.update(model);

                if(model.getErrorMessage() != null) {
                    currentGameActivity.showError(model.getErrorMessage());
                }
            }
        });
    }
}
