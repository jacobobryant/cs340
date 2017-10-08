package com.thefunteam.android.activity;

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

import java.util.List;

public class CurrentGameActivity extends AppCompatActivity {

    private RecyclerView playerListView;
    private List<String> playerList;
    private RecyclerView.Adapter adapter;
    Button startGameButton;
    Button leaveGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_game);

        playerListView = (RecyclerView) findViewById(R.id.playerList);
        playerListView.setLayoutManager(new LinearLayoutManager(this));
        playerList = Atom.getInstance().getModel().getCurrentGame().getPlayers();
        adapter = new ListAdapter(playerList);
        playerListView.setAdapter(adapter);

        startGameButton = (Button) findViewById(R.id.leaveGame);
        leaveGameButton = (Button) findViewById(R.id.startGame);

    }

    public class ListAdapter extends RecyclerView.Adapter<CurrentGameActivity.ListAdapter.ViewHolder>{

        private List<String> listItems;

        public ListAdapter(List<String> listItems) {
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

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView textDescription;
            public LinearLayout cardview;

            public ViewHolder(View itemView) {
                super(itemView);

                textDescription = (TextView) itemView.findViewById(R.id.description);
                cardview = (LinearLayout) itemView.findViewById(R.id.cardView);
            }
        }
    }
}
