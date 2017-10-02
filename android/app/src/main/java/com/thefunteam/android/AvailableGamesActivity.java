package com.thefunteam.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AvailableGamesActivity extends AppCompatActivity {

    private RecyclerView gameList;
    private RecyclerView.Adapter adapter;

    private List<ListItem> listitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_games);

        gameList = (RecyclerView) findViewById(R.id.gameList);
        gameList.setHasFixedSize(true);
        gameList.setLayoutManager(new LinearLayoutManager(this));

        listitems = new ArrayList<>();

        //add fetching list fucntions here

        adapter = new ListAdapter(listitems, this);
        gameList.setAdapter(adapter);
    }
}
