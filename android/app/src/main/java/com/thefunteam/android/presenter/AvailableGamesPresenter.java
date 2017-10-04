package com.thefunteam.android.presenter;

import com.thefunteam.android.NextLayerFacade;
import com.thefunteam.android.activity.AvailableGamesActivity;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;

import java.util.Observable;

/**
 * Created by msi on 2017-10-02.
 */

public class AvailableGamesPresenter extends Presenter {

    AvailableGamesActivity availableGamesActivity;

    public AvailableGamesPresenter(AvailableGamesActivity availableGamesActivity) {
        this.availableGamesActivity = availableGamesActivity;
    }

    void joinGame(String gameId) {
        NextLayerFacade.getInstance().joinGame(gameId);
    }

    @Override
    public void update(Observable observable, Object o) {
        Model model = Atom.getInstance().getModel();

        if( model.getCurrentGame() != null) {
            availableGamesActivity.showCurrentGame();
        }
    }
}
