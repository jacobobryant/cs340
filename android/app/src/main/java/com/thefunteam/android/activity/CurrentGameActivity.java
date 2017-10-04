package com.thefunteam.android.activity;

import android.app.LauncherActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thefunteam.android.ListAdapter;
import com.thefunteam.android.ListItem;
import com.thefunteam.android.R;
import com.thefunteam.android.presenter.CurrentGamePresenter;

import java.util.ArrayList;
import java.util.List;

public class CurrentGameActivity extends ObservingActivity {

    CurrentGamePresenter currentGamePresenter = new CurrentGamePresenter(this);

    //gameName Text
    private TextView gameName;

    //Recyclerview
    private RecyclerView playerList;
    private RecyclerView.Adapter adapter;

    private List<ListItem> listitems;

    //start game button
    private Button startButton;

    //leave button
    private Button leaveButton;

    CurrentGameActivity() {
        super();

        this.presenter = currentGamePresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_game);

        //change roomname by
        gameName = (TextView) findViewById(R.id.gameName);

        //using game object you chose, create set text here;
        //gameName.setText();


        // gamelist recycler view
        playerList = (RecyclerView) findViewById(R.id.playerList);
        playerList.setHasFixedSize(true);
        playerList.setLayoutManager(new LinearLayoutManager(this));

        listitems = new ArrayList<>();

        //using game object which is keep updating, create  List here
        //add fetching list fucntions here

        adapter = new ListAdapter(listitems, this);
        playerList.setAdapter(adapter);

        //start button
        startButton = (Button) findViewById(R.id.startGame);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "starting game", Toast.LENGTH_SHORT);
                //add function here
            }
        });

        //leave button
        leaveButton = (Button) findViewById(R.id.leaveGame);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "leaving game", Toast.LENGTH_SHORT);
                finish();
            }
        });

    }
}
