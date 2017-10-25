package com.thefunteam.android.presenter;

import com.thefunteam.android.NextLayerFacade;
import com.thefunteam.android.activity.MessageActivity;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;

import java.util.Observable;

public class MessagePresenter extends Presenter{

    MessageActivity msgActivity;

    public MessagePresenter(MessageActivity messageActivity) {
        super();
        this.msgActivity = messageActivity;
    }

    public void sendMessage(String message){
        NextLayerFacade.getInstance().sendMessage(message);
    }

    @Override
    public void update(Observable observable, Object o) {
        msgActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Model model = Atom.getInstance().getModel();
                msgActivity.update(model);
            }
        });
    }
}
