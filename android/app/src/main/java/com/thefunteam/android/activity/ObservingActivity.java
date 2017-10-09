package com.thefunteam.android.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;
import com.thefunteam.android.presenter.Presenter;

import static junit.framework.Assert.assertTrue;

public class ObservingActivity extends AppCompatActivity {

    Presenter presenter;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

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

    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
