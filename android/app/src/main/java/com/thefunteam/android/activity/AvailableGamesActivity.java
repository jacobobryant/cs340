package com.thefunteam.android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.thefunteam.android.ListAdapter;
import com.thefunteam.android.ListItem;
import com.thefunteam.android.R;
import com.thefunteam.android.presenter.AvailableGamesPresenter;

import java.util.ArrayList;
import java.util.List;

public class AvailableGamesActivity extends ObservingActivity {

    AvailableGamesPresenter availableGamesPresenter = new AvailableGamesPresenter(this);

    private RecyclerView gameList;
    private RecyclerView.Adapter adapter;

    private List<ListItem> listItems;

    private Button createGame;

    AvailableGamesActivity() {
        super();

        this.presenter = availableGamesPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_games);

        gameList = (RecyclerView) findViewById(R.id.gameList);
        gameList.setHasFixedSize(true);
        gameList.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        //add fetching list fucntions here

        adapter = new ListAdapter(listItems, this);
        gameList.setAdapter(adapter);

        //createGame.setOnClickListener(new );

    }

    public void showCurrentGame() {
        startActivity(new Intent(this, CurrentGameActivity.class));
    }
}
