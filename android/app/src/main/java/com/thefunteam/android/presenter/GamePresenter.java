package com.thefunteam.android.presenter;

import android.content.Intent;
import com.thefunteam.android.NextLayerFacade;
import com.thefunteam.android.activity.GameActivity;
import com.thefunteam.android.activity.GameOverActivity;
import com.thefunteam.android.activity.LoginActivity;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;
import com.thefunteam.android.model.shared.*;
import com.thefunteam.android.model.userInfo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class GamePresenter extends Presenter {

    GameActivity gameActivity;

    public TurnState turnState = TurnState.init;

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

                if(model.getCurrentGame() == null) {
                    gameActivity.leaveGame();
                    return;
                }

                turnState = model.getCurrentPlayer().getTurnState();
                if(model.getCurrentGame().getTurnsLeft() == 0) {
                    gameActivity.showGameOver();
                    return;
                }

                if(model.getErrorMessage() != null) {
                    gameActivity.showError(model.getErrorMessage());
                }

                gameActivity.update(model);
            }
        });
    }


    public void drawDestCard() {
        turnState.drawDestCard();
        turnState = TurnState.waiting;
    }

    public void returnDestCard(DestinationCard[] returnCards) {
        turnState.returnDestCard(returnCards);
        turnState = TurnState.waiting;
    }

    public void drawTrainCard() {
        turnState.drawTrainCard();
        turnState = TurnState.waiting;
    }

    public void drawFaceUpCard(int index) {
        turnState.drawFaceUpCard(index);
        turnState = TurnState.waiting;
    }

    public void claimRoute(Route route, TrainType type) {
        Player currentPlayer = Atom.getInstance().getModel().getCurrentPlayer();
        List<TrainType> trainCards = currentPlayer.getTrainCards();
        int count = TrainType.countCards(type, trainCards);

        if(count + TrainType.countCards(TrainType.any, trainCards) >= route.length) {
            List<TrainType> cards;
            if(count >= route.length) {
                cards = Collections.nCopies(route.length, type);
            } else {
                cards = new LinkedList<>(Collections.nCopies(count, type));
                cards.addAll(Collections.nCopies(route.length - count, TrainType.any));
            }
            turnState.claimRoute(route, cards);
        } else {
            gameActivity.showError("You don't have sufficient cards for that route");
        }
    }

    public void leaveGame() {
        NextLayerFacade.getInstance().login(userInfo.username, userInfo.password);
    }
}
