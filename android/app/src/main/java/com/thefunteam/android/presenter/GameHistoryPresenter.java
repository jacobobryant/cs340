package com.thefunteam.android.presenter;

import com.thefunteam.android.NextLayerFacade;
import com.thefunteam.android.activity.AvailableGamesActivity;
import com.thefunteam.android.activity.GameHistoryActivity;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;

import java.util.Observable;

public class GameHistoryPresenter extends Presenter {
    GameHistoryActivity gamehistoryActivity;

    public GameHistoryPresenter( GameHistoryActivity gamehistoryActivity){
        super();
        this.gamehistoryActivity = gamehistoryActivity;
    }

    @Override
    public void update(Observable observable, Object o) {
        gamehistoryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Model model = Atom.getInstance().getModel();
                gamehistoryActivity.update(model);
            }
        });
    }

}
