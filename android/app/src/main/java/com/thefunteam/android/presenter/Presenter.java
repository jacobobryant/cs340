package com.thefunteam.android.presenter;

import com.thefunteam.android.model.Atom;

import java.util.Observer;

public abstract class Presenter implements Observer {

    public void beginObserving() {
        Atom.getInstance().addObserver(this);
    }

    public void endObserving() {
        Atom.getInstance().deleteObserver(this);
    }
}
