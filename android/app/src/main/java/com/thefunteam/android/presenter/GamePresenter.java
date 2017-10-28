package com.thefunteam.android.presenter;

import com.thefunteam.android.NextLayerFacade;
import com.thefunteam.android.activity.GameActivity;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;
import com.thefunteam.android.model.shared.*;
import com.thefunteam.android.presenter.Presenter;

import java.util.List;
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

                gameActivity.update(model);
            }
        });
    }

    public void returnDestCard(int returnCard) {
        Player player = Atom.getInstance().getModel().getCurrentPlayer();
        if(player != null && player.getDestCards() != null) {
            List<DestinationCard> cards = player.getDestCards();
            if(returnCard >= 0) {
                NextLayerFacade.getInstance().returnCard(cards.get(returnCard));
            } else {
                NextLayerFacade.getInstance().returnCard(null);
            }
        }
    }

    public void addPoints() {
        List<Player> players = Atom.getInstance().getModel().getCurrentGame().getPlayers();
        for (Player player : players) {
            player.score += 2;
        }
        gameActivity.update(Atom.getInstance().getModel());
    }

    public void addTrainCard() {
        Player player = Atom.getInstance().getModel().getCurrentPlayer();
        if(player != null) {
            player.trainCards.add(TrainType.any);
            Atom.getInstance().getModel().getCurrentGame().trainDeck--;
        }
        gameActivity.update(Atom.getInstance().getModel());

    }

    public void addDestCard() {
        Player player = Atom.getInstance().getModel().getCurrentPlayer();
        if(player != null) {
            player.destCards.add(new DestinationCard(City.KansasCity, City.OklahomaCity, 10));
            Atom.getInstance().getModel().getCurrentGame().destDeck--;
        }
        gameActivity.update(Atom.getInstance().getModel());

    }

    public void removeDestCard() {
        Player player = Atom.getInstance().getModel().getCurrentPlayer();
        if(player != null && player.destCards.size() > 0) {
            player.destCards.remove(0);
            Atom.getInstance().getModel().getCurrentGame().destDeck--;
        }
        gameActivity.update(Atom.getInstance().getModel());
    }

    public void addTrainCardOther() {
        Player otherPlayer = null;
        List<Player> players = Atom.getInstance().getModel().getCurrentGame().getPlayers();
        for (Player player : players) {
            if(!player.currentPlayer) {
                otherPlayer = player;
                break;
            }
        }
        if(otherPlayer != null) {
            otherPlayer.trainCards.add(null);
            Atom.getInstance().getModel().getCurrentGame().trainDeck--;
        }
        gameActivity.update(Atom.getInstance().getModel());
    }

    public void addDestCardOther() {
        Player otherPlayer = null;
        List<Player> players = Atom.getInstance().getModel().getCurrentGame().getPlayers();
        for (Player player : players) {
            if(!player.currentPlayer) {
                otherPlayer = player;
                break;
            }
        }
        if(otherPlayer != null) {
            otherPlayer.destCards.add(null);
            Atom.getInstance().getModel().getCurrentGame().destDeck--;
        }
        gameActivity.update(Atom.getInstance().getModel());
    }

    public void claimRoute() {
        Player player = Atom.getInstance().getModel().getCurrentPlayer();
        if(player != null) {
            List<Route> openRoutes = Atom.getInstance().getModel().getCurrentGame().getOpenRoutes();
            player.routes.add(openRoutes.get(0));
            openRoutes.remove(0);
        }
        gameActivity.update(Atom.getInstance().getModel());
    }

    public void claimRouteOther() {
        Player otherPlayer = null;
        List<Player> players = Atom.getInstance().getModel().getCurrentGame().getPlayers();
        for (Player player : players) {
            if(!player.currentPlayer) {
                otherPlayer = player;
                break;
            }
        }
        if(otherPlayer != null) {
            List<Route> openRoutes = Atom.getInstance().getModel().getCurrentGame().getOpenRoutes();
            otherPlayer.routes.add(openRoutes.get(0));
            openRoutes.remove(0);
        }
        gameActivity.update(Atom.getInstance().getModel());
    }

    public void addGameHistory() {
        List<String> history = Atom.getInstance().getModel().getCurrentGame().getHistory();
        history.add("hello");
    }

    public void drawFaceUpCard() {
        List<TrainType> faceUp = Atom.getInstance().getModel().getCurrentGame().getFaceUpDeck();
        Player player = Atom.getInstance().getModel().getCurrentPlayer();
        player.getTrainCards().add(faceUp.get(0));
        faceUp.remove(0);
        faceUp.add(TrainType.any);
        gameActivity.update(Atom.getInstance().getModel());

    }
}
