package com.thefunteam.android.presenter;

import com.thefunteam.android.MyTurn;
import com.thefunteam.android.NextLayerFacade;
import com.thefunteam.android.OthersTurn;
import com.thefunteam.android.TurnState;
import com.thefunteam.android.activity.GameActivity;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;
import com.thefunteam.android.model.shared.*;
import com.thefunteam.android.presenter.Presenter;

import java.util.List;
import java.util.Observable;

public class GamePresenter extends Presenter {

    GameActivity gameActivity;

    TurnState turnState = new OthersTurn();

    public GamePresenter(GameActivity gameActivity) {
        super();

        this.gameActivity = gameActivity;
    }

    @Override
    public void update(Observable observable, Object o) {
        if(false /* is my turn*/) {
            turnState = new MyTurn();
        } else {
            turnState = new OthersTurn();
        }

        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Model model = Atom.getInstance().getModel();

                if(model.getErrorMessage() != null) {
                    gameActivity.showError(model.getErrorMessage());
                }

                gameActivity.update(model);
            }
        });
    }


    public void drawDestCard() {


    }

    public void returnDestCard(int returnCard) {

    }

    public void drawTrainCard() {

    }

    public void drawFaceUpCard(int index) {

    }

    public void claimRoute() {

    }

}
