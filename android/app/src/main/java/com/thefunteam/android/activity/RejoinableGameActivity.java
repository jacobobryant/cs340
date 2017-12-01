package com.thefunteam.android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.thefunteam.android.model.Model;
import com.thefunteam.android.model.shared.AvailableGame;
import com.thefunteam.android.model.shared.RejoinableGame;
import com.thefunteam.android.presenter.RejoinableGamePresenter;

import java.util.List;

public class RejoinableGameActivity extends  ObservingActivity{

    RejoinableGamePresenter rgPresenter = new RejoinableGamePresenter(this);

    RecyclerView gameListView;
    private List<RejoinableGame> gameList;
    private ListAdapter adapter;
    private Button goAvailableList;

    public RejoinableGameActivity(){
        super();
        presenter = rgPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejoinable_game);

        gameListView = (RecyclerView) findViewById(R.id.gameList);
        gameListView.setHasFixedSize(true);
        gameListView.setLayoutManager(new LinearLayoutManager(this));
        gameList = Atom.getInstance().getModel().getRejoinableGames();

        adapter = new RejoinableGameActivity.ListAdapter(gameList);
        gameListView.setAdapter(adapter);

        goAvailableList = (Button) findViewById(R.id.createGame);
        goAvailableList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RejoinableGameActivity.this, AvailableGamesActivity.class));
            }
        });

    }

    private void askToReJoinGame(View view, final RejoinableGame game) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Join Game")
                .setMessage("Do you want to rejoin this game?")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RejoinableGamePresenter.rejoinGame(game.getGameId());
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void update(Model model){
        List<RejoinableGame> games = model.getRejoinableGames();
        gameList.clear();;
        if(games != null){
            gameList.addAll(games);
        }
        adapter.notifyDataSetChanged();
    }


    class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{



        public List<RejoinableGame> listItems;

        ListAdapter(List<RejoinableGame> listItems) {
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
            final RejoinableGame game = listItems.get(position);

            String ownerName = game.getPlayers().get(0);
            holder.textDescription.setText(ownerName + "'s game (" + Integer.toString(game.getPlayers().size()) + " players)");

            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    askToReJoinGame(view, game);
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
