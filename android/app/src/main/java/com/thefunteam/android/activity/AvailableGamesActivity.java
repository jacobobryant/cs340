package com.thefunteam.android.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.thefunteam.android.presenter.AvailableGamesPresenter;

import java.util.List;

public class AvailableGamesActivity extends ObservingActivity {

    AvailableGamesPresenter availableGamesPresenter = new AvailableGamesPresenter(this);

    private RecyclerView gameListView;
    private List<Game> gameList;
    private ListAdapter adapter;
    private Button createGameButton;

    public AvailableGamesActivity() {
        super();

        presenter = availableGamesPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_games);

        gameListView = (RecyclerView) findViewById(R.id.gameList);
        gameListView.setHasFixedSize(true);
        gameListView.setLayoutManager(new LinearLayoutManager(this));
        gameList = Atom.getInstance().getModel().getAvailableGames();

        adapter = new ListAdapter(gameList);
        gameListView.setAdapter(adapter);

        createGameButton = (Button) findViewById(R.id.createGame);
        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                availableGamesPresenter.createGame();
            }
        });
    }

    private void askToJoinGame(View view, final Game game) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Join Game")
                .setMessage("Do you want to join this game?")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        availableGamesPresenter.joinGame(game.getGameId());
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void presentCurrentGame() {
        startActivity(new Intent(this, CurrentGameActivity.class));
    }

    public void update(Model model) {
        List<Game> games = model.getAvailableGames();
        gameList.clear();
        if (games != null) {
            gameList.addAll(games);
        }
        adapter.notifyDataSetChanged();
    }

    class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

        public List<Game> listItems;

        ListAdapter(List<Game> listItems) {
            this.listItems = listItems;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_view, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Game game = listItems.get(position);

            String ownerName = game.getPlayers().get(0);
            holder.textDescription.setText(ownerName + "'s game (" + Integer.toString(game.getPlayers().size()) + " players)");

            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    askToJoinGame(view, game);
                }
            });
        }


        @Override
        public int getItemCount() {
            if(listItems != null) {
                return listItems.size();
            } else {
                return 0;
            }
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
