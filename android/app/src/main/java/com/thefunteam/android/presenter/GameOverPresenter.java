package com.thefunteam.android.presenter;

import android.widget.TextView;
import com.thefunteam.android.activity.GameOverActivity;

import java.util.List;
import java.util.Observable;

public class GameOverPresenter {//extends Presenter{
    private final GameOverActivity goActivity;

    public GameOverPresenter(GameOverActivity goActivity) {
        this.goActivity = goActivity;
    }

    public void goGameMenu(){
        goActivity.goGameMenu();
    }

    public void showScores() {
        goActivity.showScores();
    }
}
