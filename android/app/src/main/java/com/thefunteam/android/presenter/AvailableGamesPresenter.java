package com.thefunteam.android.presenter;

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

    @Override
    public void update(Observable observable, Object o) {
        Model model = Atom.getInstance().getModel();

        if (model.getCurrentGame() != null) {
            // start the current game activity here.
        }
    }
}
