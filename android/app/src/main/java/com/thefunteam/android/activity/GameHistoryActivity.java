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
import com.thefunteam.android.model.InGameModel.InGame;
import com.thefunteam.android.model.Model;
import com.thefunteam.android.presenter.GameHistoryPresenter;

import java.util.ArrayList;
import java.util.List;

public class GameHistoryActivity extends ObservingActivity {

    GameHistoryPresenter gameHistoryPresenter =  new GameHistoryPresenter(this);

    public GameHistoryActivity(){
        super();
        presenter = gameHistoryPresenter;
    }

    private RecyclerView gameHistoryView;
    private List<String> gameLog = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private Button backToGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);

        gameHistoryView = (RecyclerView) findViewById(R.id.gameLog);
        gameHistoryView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter(gameLog);
        gameHistoryView.setAdapter(adapter);

        backToGameButton = (Button) findViewById(R.id.Back_to_Game);
        backToGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we do not need a presenter function right? we do not have to report to the server
                finish();
            }
        });
        update(Atom.getInstance().getModel());
    }

    public void update(Model model){

        //curinGame should be updated how can I do that?

        if (model.getInGameObject() == null) {
            return;
        }
        List<String> newLog = model.getInGameObject().getGameHistory();
        gameLog.clear();

        if(newLog == null){
            gameLog.add("no history");
        }
        if(newLog != null){
            gameLog.addAll(newLog);
        }

        adapter.notifyDataSetChanged();
    }

    public class ListAdapter extends RecyclerView.Adapter<GameHistoryActivity.ListAdapter.ViewHolder>{

        private List<String> listItems;

        ListAdapter(List<String> listItems) {
            this.listItems = listItems;
        }

        @Override
        public GameHistoryActivity.ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_view, parent, false);
            return new GameHistoryActivity.ListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(GameHistoryActivity.ListAdapter.ViewHolder holder, int position) {
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
