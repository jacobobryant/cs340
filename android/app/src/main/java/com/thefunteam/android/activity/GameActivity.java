package com.thefunteam.android.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;
import com.thefunteam.android.Poller;
import com.thefunteam.android.R;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;
import com.thefunteam.android.model.shared.*;
import com.thefunteam.android.presenter.GamePresenter;
import com.thefunteam.android.view.Map;

import java.util.List;

public class GameActivity extends ObservingActivity {

    GamePresenter gamePresenter = new GamePresenter(this);
    Map map;
    private GestureDetector mDetector;
    private Button chatButton;
    private Button historyButton;
    private RecyclerView playerInfoView;
    private ListAdapter adapter;

    // destination card picker
    private LinearLayout destinationPicker;
    private CheckBox destChooser1;
    private CheckBox destChooser2;
    private CheckBox destChooser3;
    private Button destSubmit;

    // card buttons
    private Button destDrawPile;
    private Button trainDrawPile;
    private Button faceUp0;
    private Button faceUp1;
    private Button faceUp2;
    private Button faceUp3;
    private Button faceUp4;

    private boolean showDestPicker = true;

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

        // Player info
        adapter = new ListAdapter();
        playerInfoView = (RecyclerView) findViewById(R.id.playerinfo);
        playerInfoView.setHasFixedSize(true);
        playerInfoView.setLayoutManager(new LinearLayoutManager(this));
        playerInfoView.setAdapter(adapter);
        playerInfoView.setBackgroundColor(Color.RED);


        //chat button
        chatButton = (Button) findViewById(R.id.chatbutton);
        chatButton.setOnClickListener(v -> startActivity(new Intent(GameActivity.this, MessageActivity.class)));

        historyButton = (Button) findViewById(R.id.historybutton);
        historyButton.setOnClickListener(v -> startActivity( new Intent(GameActivity.this, GameHistoryActivity.class)));

        destinationPicker = (LinearLayout) findViewById(R.id.destinationcardpicker);
        destinationPicker.setVisibility(View.GONE);
        destChooser1 = (CheckBox) findViewById(R.id.checkBox0);
        destChooser2 = (CheckBox) findViewById(R.id.checkBox1);
        destChooser3 = (CheckBox) findViewById(R.id.checkBox2);
        destSubmit = (Button) findViewById(R.id.submitDest);
        destSubmit.setOnClickListener(v -> {
            int total = 0;
            int returnCard = -1;
            if(destChooser1.isChecked()) { total++; } else { returnCard = 0; }
            if(destChooser2.isChecked()) { total++; } else { returnCard = 1; }
            if(destChooser3.isChecked()) { total++; } else { returnCard = 2; }

            if(total >= 2) {
                showDestPicker = false;
                gamePresenter.returnDestCard(returnCard);
            } else {
                Toast.makeText(this,"You must select at least two destination cards.", Toast.LENGTH_SHORT).show();
            }
        });

        // card buttons
        destDrawPile = (Button) findViewById(R.id.destDeckButton);
        trainDrawPile = (Button) findViewById(R.id.trainDeckButton);
        faceUp0 = (Button) findViewById(R.id.faceUpTrain0);
        faceUp1 = (Button) findViewById(R.id.faceUpTrain1);
        faceUp2 = (Button) findViewById(R.id.faceUpTrain2);
        faceUp3 = (Button) findViewById(R.id.faceUpTrain3);
        faceUp4 = (Button) findViewById(R.id.faceUpTrain4);

        update(Atom.getInstance().getModel());
    }


    public void update(Model model) {
        Player currentPlayer = model.getCurrentPlayer();
        Game game = model.getCurrentGame();
        if(currentPlayer != null) {

            // Update dest choosing
            if(showDestPicker) {
                List<DestinationCard> cards = currentPlayer.getDestCards();
                destinationPicker.setVisibility(View.VISIBLE);
                destChooser1.setText(cards.get(0).description());
                destChooser2.setText(cards.get(1).description());
                destChooser3.setText(cards.get(2).description());
            } else {
                destinationPicker.setVisibility(View.GONE);
                destChooser1.setChecked(false);
                destChooser2.setChecked(false);
                destChooser3.setChecked(false);
            }
        }

        // Update destination deck
        int destCardCount = game.getDestDeck();
        destDrawPile.setText("Destination Draw Deck\n" + Integer.toString(destCardCount) + " Cards Left");
        int trainCardCount = game.getTrainDeck();
        trainDrawPile.setText("Train Draw Deck\n" + Integer.toString(trainCardCount) + " Cards Left.");

        if(game.getFaceUpDeck().size() > 0) {
            faceUp0.setBackgroundColor(MapHelper.getColor(game.getFaceUpDeck().get(0)));
        } else {
            faceUp0.setVisibility(View.INVISIBLE);
        }
        if(game.getFaceUpDeck().size() > 1) {
            faceUp1.setBackgroundColor(MapHelper.getColor(game.getFaceUpDeck().get(1)));
        } else {
            faceUp1.setVisibility(View.INVISIBLE);
        }
        if(game.getFaceUpDeck().size() > 2) {
            faceUp2.setBackgroundColor(MapHelper.getColor(game.getFaceUpDeck().get(2)));
        } else {
            faceUp2.setVisibility(View.INVISIBLE);
        }
        if(game.getFaceUpDeck().size() > 3) {
            faceUp3.setBackgroundColor(MapHelper.getColor(game.getFaceUpDeck().get(3)));
        } else {
            faceUp3.setVisibility(View.INVISIBLE);
        }
        if(game.getFaceUpDeck().size() > 4) {
            faceUp4.setBackgroundColor(MapHelper.getColor(game.getFaceUpDeck().get(4)));
        } else {
            faceUp4.setVisibility(View.INVISIBLE);
        }

        adapter.notifyDataSetChanged();

        map.updateRoutes(model);

        Poller.getInstance().stopPolling();
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return mDetector.onTouchEvent(event);
        }
    };

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

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
            Player player = players.get(0);
            if(position == 0) {
                int color = 0;
                for(int i = 0; i < players.size(); i++) {
                    if(players.get(i).isCurrentPlayer()) {
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
                    if(players.get(i).isCurrentPlayer()) {
                        skip = true;
                    }
                }
                int color;
                if(skip) {
                    color = position;
                } else {
                    color = position - 1;
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
