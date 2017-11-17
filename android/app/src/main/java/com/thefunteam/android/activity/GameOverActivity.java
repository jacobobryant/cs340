package com.thefunteam.android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.thefunteam.android.Poller;
import com.thefunteam.android.R;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.shared.Player;
import com.thefunteam.android.presenter.GameOverPresenter;

import java.util.ArrayList;
import java.util.List;

public class GameOverActivity extends ObservingActivity {

    private GameOverPresenter goPresentner = new GameOverPresenter(this);
    Button back_to_available_game;
    ArrayList<TextView> playerscores = new ArrayList<>();
    TextView winnerText;

    public GameOverActivity() {
        super();

        this.presenter = goPresentner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        back_to_available_game = (Button) findViewById(R.id.backtomenu);
        back_to_available_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goGameMenu();
            }
        });

        playerscores.add((TextView) findViewById(R.id.textView));
        playerscores.add((TextView) findViewById(R.id.textView2));
        playerscores.add((TextView) findViewById(R.id.textView3));
        playerscores.add((TextView) findViewById(R.id.textView4));
        playerscores.add((TextView) findViewById(R.id.textView5));
        winnerText = (TextView) findViewById(R.id.winnerText);
        
        showScores();
    }

    public void goGameMenu(){
        Atom.reset();
        Intent intent = new Intent(GameOverActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
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

        winnerText.setText("\t\t" + Atom.getInstance().getModel().getWinningPlayer() + " wins!!!");
    }
}