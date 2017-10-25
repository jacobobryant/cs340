package com.thefunteam.android.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.thefunteam.android.R;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;
import com.thefunteam.android.model.shared.DestinationCard;
import com.thefunteam.android.model.shared.MapHelper;
import com.thefunteam.android.model.shared.Player;
import com.thefunteam.android.model.shared.TrainType;
import com.thefunteam.android.presenter.GamePresenter;
import com.thefunteam.android.view.Map;

import java.util.List;

public class GameActivity extends ObservingActivity {

    GamePresenter gamePresenter = new GamePresenter(this);
    Map map;
    private GestureDetector mDetector;
    private RecyclerView playerInfoView;
    private ListAdapter adapter;

    public GameActivity() {
        super();

        this.presenter = gamePresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        map = (Map) findViewById(R.id.map);
        map.setBackgroundColor(Color.rgb(248,233,213));

        mDetector = new GestureDetector(this, new MyGestureListener());
        map.setOnTouchListener(touchListener);

        map.updateRoutes(Atom.getInstance().getModel());


        // Player info
        adapter = new ListAdapter();
        playerInfoView = (RecyclerView) findViewById(R.id.playerinfo);
        playerInfoView.setHasFixedSize(true);
        playerInfoView.setLayoutManager(new LinearLayoutManager(this));
        playerInfoView.setAdapter(adapter);
        playerInfoView.setBackgroundColor(Color.RED);
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return mDetector.onTouchEvent(event);
        }
    };

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return true;
        }
    }

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == 0) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.playercard, parent, false);
                return new ViewHolder(v);
            } else {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.otherplayercard, parent, false);
                return new ViewHolder(v);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if(Atom.getInstance().getModel().getCurrentGame() == null || Atom.getInstance().getModel().getCurrentGame().getPlayers() == null) { return; }
            List<Player> players = Atom.getInstance().getModel().getCurrentGame().getPlayers();
            String username = "roy";
            Player player = players.get(0);
            if(position == 0) {
                int color = 0;
                for(int i = 0; i < players.size(); i++) {
                    if(players.get(i).getUsername().equals(username)) {
                        player = players.get(i);
                        color = i; break;
                    }
                }
                holder.username.setText("You");
                holder.color.setText("Color: " + MapHelper.getPlayerColorName(color));
                holder.score.setText("Score: " + player.getScore());

                StringBuilder destCardString = new StringBuilder("Destination Cards:\n");
                for(int i = 0; i < player.getDestCards().size(); i++) {
                    DestinationCard destCard = player.getDestCards().get(i);
                    destCardString.append(" \t");
                    destCardString.append(MapHelper.getName(destCard.city1));
                    destCardString.append(" to ");
                    destCardString.append(MapHelper.getName(destCard.city2));
                    destCardString.append(" (");
                    destCardString.append(Integer.toString(destCard.points));
                    destCardString.append(").\n");
                }
                holder.destCards.setText(destCardString.toString());

                List<TrainType> trainCards = player.getTrainCards();
                StringBuilder trainCardString = new StringBuilder("Train Cards:\n");
                for(TrainType trainType : TrainType.values()) {
                    trainCardString.append("\t");
                    trainCardString.append(trainType.name());
                    trainCardString.append(": ");
                    trainCardString.append(Integer.toString(countCards(trainType, trainCards)));
                    trainCardString.append("\n");
                }
                holder.trainCards.setText(trainCardString.toString());

                holder.trainsLeft.setText("Trains left: " + player.getTrainsLeft());
            } else {
                boolean skip = false;
                for(int i = 0; i < position; i++) {
                    if(!players.get(i).getUsername().equals(username)) {
                        skip = true;
                    }
                }
                int color;
                if(skip) {
                    color = position + 1;
                } else {
                    color = position;
                }
                player = players.get(color);

                holder.username.setText(player.getUsername());
                holder.color.setText("Color: " + MapHelper.getPlayerColorName(color));
                holder.score.setText("Score: " + player.getScore());

                holder.destCards.setText("Destination Cards: " + player.getDestCards().size());
                holder.trainCards.setText("Train Cards: " + player.getTrainCards().size());
                holder.trainsLeft.setText("Trains left: " + player.getTrainsLeft());
            }
        }

        private int countCards(TrainType trainType, List<TrainType> cards) {
            int count = 0;
            for(TrainType card : cards) {
                if(card == trainType) {
                    count = count + 1;
                }
            }
            return count;
        }

        @Override
        public int getItemCount() {
            if(Atom.getInstance().getModel().getCurrentGame() == null || Atom.getInstance().getModel().getCurrentGame().getPlayers() == null) { return 0; }
            return Atom.getInstance().getModel().getCurrentGame().getPlayers().size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView username;
            TextView color;
            TextView score;
            TextView destCards;
            TextView trainCards;
            TextView trainsLeft;
            LinearLayout cardview;

            ViewHolder(View itemView) {
                super(itemView);

                username = (TextView) itemView.findViewById(R.id.username);
                color = (TextView) itemView.findViewById(R.id.playercolor);
                score = (TextView) itemView.findViewById(R.id.score);
                destCards = (TextView) itemView.findViewById(R.id.destcards);
                trainCards = (TextView) itemView.findViewById(R.id.traincards);
                trainsLeft = (TextView) itemView.findViewById(R.id.trainsleft);
                cardview = (LinearLayout) itemView.findViewById(R.id.cardView);
            }
        }
    }
}
