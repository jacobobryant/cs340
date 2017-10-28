package com.thefunteam.android.model;

import java.util.Observable;

public class Atom extends Observable {
    private static Atom ourInstance = new Atom();

    public static Atom getInstance() {
        return ourInstance;
    }

    private Model model = new Model();

    private Atom() {
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
        setChanged();
        notifyObservers();
    }

    public void setError(String error) {
        model.setErrorMessage(error);
        setChanged();
        notifyObservers();
    }

    public static void reset() {
        ourInstance.model = new Model();
    }
}
