package com.thefunteam.android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.thefunteam.android.R;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.shared.Player;
import com.thefunteam.android.presenter.GameOverPresenter;

import java.util.ArrayList;
import java.util.List;

public class GameOverActivity extends AppCompatActivity {

    private GameOverPresenter goPresentner;
    Button back_to_available_game;
    ArrayList<TextView> playerscores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        back_to_available_game = (Button) findViewById(R.id.backtomenu);
        back_to_available_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goPresentner.goGameMenu();
            }
        });

        playerscores.add((TextView) findViewById(R.id.textView));
        playerscores.add((TextView) findViewById(R.id.textView2));
        playerscores.add((TextView) findViewById(R.id.textView3));
        playerscores.add((TextView) findViewById(R.id.textView4));
        playerscores.add((TextView) findViewById(R.id.textView5));
        
        goPresentner.showScores();

    }

    public void goGameMenu(){
        Intent intent = new Intent(GameOverActivity.this, AvailableGamesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        //startActivity(new Intent(this, AvailableGamesActivity.class));
    }
    public void showScores(){
        ArrayList<String> scoreDetails = new ArrayList<>();
        List<Player> players = Atom.getInstance().getModel().getCurrentGame().getPlayers();
        for (int i = 0; i < players.size(); i++){
            scoreDetails.add(players.get(i).getScores());
        }
        for (int i = 0; i < scoreDetails.size(); i++){
            playerscores.get(i).setText(scoreDetails.get(i));
        }
    }

}