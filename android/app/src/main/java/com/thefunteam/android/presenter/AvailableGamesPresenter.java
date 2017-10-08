package com.thefunteam.android.presenter;

import com.thefunteam.android.ClientCommunicator;
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

    @Override
    public void update(Observable observable, Object o) {
        Model model = Atom.getInstance().getModel();

        if (model.getCurrentGame() != null) {
            availableGamesActivity.presentCurrentGame();
        }
    }
}
