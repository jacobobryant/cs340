package com.thefunteam.android.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.thefunteam.android.R;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.presenter.GamePresenter;
import com.thefunteam.android.view.Map;

public class GameActivity extends ObservingActivity {

    GamePresenter gamePresenter = new GamePresenter(this);
    Map map;
    private GestureDetector mDetector;

    public GameActivity() {
        super();

        this.presenter = gamePresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        map = (Map) findViewById(R.id.map);
        map.setBackgroundColor(Color.rgb(248,233,213));

        mDetector = new GestureDetector(this, new MyGestureListener());
        map.setOnTouchListener(touchListener);

        map.updateRoutes(Atom.getInstance().getModel());
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return mDetector.onTouchEvent(event);
        }
    };

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return true;
        }
    }
}
