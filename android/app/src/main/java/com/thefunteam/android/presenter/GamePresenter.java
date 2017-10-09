package com.thefunteam.android.presenter;

import com.thefunteam.android.activity.GameActivity;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;
import com.thefunteam.android.presenter.Presenter;

import java.util.Observable;

public class GamePresenter extends Presenter {

    GameActivity gameActivity;

    public GamePresenter(GameActivity gameActivity) {
        super();

        this.gameActivity = gameActivity;
    }

    @Override
    public void update(Observable observable, Object o) {
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Model model = Atom.getInstance().getModel();

                if(model.getErrorMessage() != null) {
                    gameActivity.showError(model.getErrorMessage());
                }
            }
        });
    }
}
