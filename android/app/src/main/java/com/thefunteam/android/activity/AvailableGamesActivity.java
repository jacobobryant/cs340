package com.thefunteam.android.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.thefunteam.android.R;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;

public class AvailableGamesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_games);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Atom.getInstance().setModel(new Model());
    }
}
