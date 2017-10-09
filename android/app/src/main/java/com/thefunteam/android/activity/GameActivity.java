package com.thefunteam.android.activity;

import android.os.Bundle;
import com.thefunteam.android.R;
import com.thefunteam.android.presenter.GamePresenter;

public class GameActivity extends ObservingActivity {

    GamePresenter gamePresenter = new GamePresenter(this);

    public GameActivity() {
        super();

        this.presenter = gamePresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }
}
