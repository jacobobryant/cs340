package com.thefunteam.android.presenter;

import com.thefunteam.android.NextLayerFacade;
import com.thefunteam.android.activity.CurrentGameActivity;

import java.util.Observable;

/**
 * Created by msi on 2017-10-02.
 */

public class CurrentGamePresenter extends Presenter {

    CurrentGameActivity currentGameActivity;

    public CurrentGamePresenter(CurrentGameActivity currentGameActivity) {
        this.currentGameActivity = currentGameActivity;
    }

    void startGame() {
        NextLayerFacade.getInstance().startGame();
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
