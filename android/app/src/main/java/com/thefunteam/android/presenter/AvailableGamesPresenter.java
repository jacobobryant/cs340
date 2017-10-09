package com.thefunteam.android.presenter;

import com.thefunteam.android.NextLayerFacade;
import com.thefunteam.android.activity.AvailableGamesActivity;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;

import java.util.Observable;

public class AvailableGamesPresenter extends Presenter {

    AvailableGamesActivity availableGamesActivity;

    public AvailableGamesPresenter(AvailableGamesActivity availableGamesActivity) {
        super();

        this.availableGamesActivity = availableGamesActivity;
    }

    public void joinGame(String gameId) {
        NextLayerFacade.getInstance().joinGame(gameId);
    }

    public void createGame() {
        NextLayerFacade.getInstance().createGame();
    }

    @Override
    public void update(Observable observable, Object o) {
        availableGamesActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Model model = Atom.getInstance().getModel();

                if (model.getSessionId() == null) {
                    Atom.reset();
                    availableGamesActivity.finish();
                    return;
                } else if (model.getCurrentGame() != null) {
                    availableGamesActivity.presentCurrentGame();
                }
                availableGamesActivity.update(model);

                if(model.getErrorMessage() != null) {
                    availableGamesActivity.showError(model.getErrorMessage());
                }
            }
        });
    }
}
