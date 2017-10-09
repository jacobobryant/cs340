package com.thefunteam.android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.thefunteam.android.R;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Game;
import com.thefunteam.android.model.Model;
import com.thefunteam.android.presenter.CurrentGamePresenter;

import java.util.ArrayList;
import java.util.List;

public class CurrentGameActivity extends ObservingActivity {

    CurrentGamePresenter currentGamePresenter = new CurrentGamePresenter(this);

    private RecyclerView playerListView;
    private List<String> playerList = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private Button startGameButton;
    private Button leaveGameButton;

    public CurrentGameActivity() {
        super();

        this.presenter = currentGamePresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_game);

        playerListView = (RecyclerView) findViewById(R.id.playerList);
        playerListView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter(playerList);
        playerListView.setAdapter(adapter);

        startGameButton = (Button) findViewById(R.id.startGame);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentGamePresenter.startGame();
            }
        });

        leaveGameButton = (Button) findViewById(R.id.leaveGame);
        leaveGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentGamePresenter.leaveGame();
            }
        });

        update(Atom.getInstance().getModel());
    }

    public void update(Model model) {
        List<String> players = model.getCurrentGame().getPlayers();
        playerList.clear();
        if (players != null) {
            playerList.addAll(players);
            startGameButton.setEnabled(playerList.size() > 1 && playerList.size() < 6);
        }
        adapter.notifyDataSetChanged();
    }

    public void presentGame() {
        startActivity(new Intent(this, GameActivity.class));
    }

    public class ListAdapter extends RecyclerView.Adapter<CurrentGameActivity.ListAdapter.ViewHolder>{

        private List<String> listItems;

        ListAdapter(List<String> listItems) {
            this.listItems = listItems;
        }

        @Override
        public CurrentGameActivity.ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_view, parent, false);
            return new CurrentGameActivity.ListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CurrentGameActivity.ListAdapter.ViewHolder holder, int position) {
            final String player = listItems.get(position);

            holder.textDescription.setText(player);
        }


        @Override
        public int getItemCount() {
            return listItems.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView textDescription;
            LinearLayout cardview;

            ViewHolder(View itemView) {
                super(itemView);

                textDescription = (TextView) itemView.findViewById(R.id.description);
                cardview = (LinearLayout) itemView.findViewById(R.id.cardView);
            }
        }
    }
}
