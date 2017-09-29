package com.thefunteam.android.activity;

import android.support.v7.app.AppCompatActivity;
import com.thefunteam.android.presenter.Presenter;

import static junit.framework.Assert.assertTrue;

public class ObservingActivity extends AppCompatActivity {

    Presenter presenter;

    @Override
    protected void onResume() {
        super.onResume();


        assertTrue("You need set presenter in the constructor of the class that extends this.", presenter != null);
        presenter.beginObserving();
    }

    @Override
    protected void onPause() {
        super.onPause();

        assertTrue("You need set presenter in the constructor of the class that extends this.", presenter != null);
        presenter.endObserving();
    }
}
