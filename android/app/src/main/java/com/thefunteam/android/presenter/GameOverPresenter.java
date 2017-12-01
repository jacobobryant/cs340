package com.thefunteam.android.presenter;

import android.widget.TextView;
import com.thefunteam.android.activity.GameOverActivity;

import java.util.List;
import java.util.Observable;

public class GameOverPresenter extends Presenter {

    private final GameOverActivity goActivity;

    public GameOverPresenter(GameOverActivity goActivity) {
        super();
        this.goActivity = goActivity;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
