package com.thefunteam.android.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;
import com.thefunteam.android.Poller;
import com.thefunteam.android.R;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Cord;
import com.thefunteam.android.model.Model;
import com.thefunteam.android.model.shared.*;
import com.thefunteam.android.presenter.GamePresenter;
import com.thefunteam.android.presenter.Presenter;
import com.thefunteam.android.view.Map;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.thefunteam.android.model.shared.TrainType.any;

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
            List<DestinationCard> pending = Atom.getInstance().getModel().getCurrentPlayer().getPending();
            List<DestinationCard> returnCards = new LinkedList<>();
            if(!destChooser1.isChecked() && pending.size() > 0) { returnCards.add(pending.get(0)); }
            if(!destChooser2.isChecked() && pending.size() > 1) { returnCards.add(pending.get(1)); }
            if(!destChooser3.isChecked() && pending.size() > 2) { returnCards.add(pending.get(2)); }

            if(returnCards.size() <= gamePresenter.turnState.maxReturnCards()) {
                gamePresenter.returnDestCard(returnCards.toArray(new DestinationCard[returnCards.size()]));
            } else {
                Toast.makeText(this,"You must select more destination cards.", Toast.LENGTH_SHORT).show();
            }
        });

        // card buttons
        destDrawPile = (Button) findViewById(R.id.destDeckButton);
        destDrawPile.setOnClickListener(v -> gamePresenter.drawDestCard());
        trainDrawPile = (Button) findViewById(R.id.trainDeckButton);
        trainDrawPile.setOnClickListener(v -> gamePresenter.drawTrainCard());
        faceUp0 = (Button) findViewById(R.id.faceUpTrain0);
        faceUp1 = (Button) findViewById(R.id.faceUpTrain1);
        faceUp2 = (Button) findViewById(R.id.faceUpTrain2);
        faceUp3 = (Button) findViewById(R.id.faceUpTrain3);
        faceUp4 = (Button) findViewById(R.id.faceUpTrain4);
        Button[] faceUpButtons = {faceUp0, faceUp1, faceUp2, faceUp3, faceUp4};
        for(int i = 0; i < faceUpButtons.length; i++) {
            final int index = i;
            faceUpButtons[i].setOnClickListener(v -> gamePresenter.drawFaceUpCard(index));
        }


        mDetector = new GestureDetector(this, new MyGestureListener(this));
        map.setOnTouchListener(touchListener);


        update(Atom.getInstance().getModel());
    }


    public void update(Model model) {
        Player currentPlayer = model.getCurrentPlayer();
        Game game = model.getCurrentGame();
        if(currentPlayer != null) {

            // Update dest choosing

            if(gamePresenter.turnState == TurnState.init || gamePresenter.turnState == TurnState.returnDest) {
                List<DestinationCard> cards = currentPlayer.getPending();
                destinationPicker.setVisibility(View.VISIBLE);
                if(cards.size() > 0) {
                    destChooser1.setVisibility(View.VISIBLE);
                    destChooser1.setText(cards.get(0).description());
                } else {
                    destChooser1.setVisibility(View.GONE);
                }
                if(cards.size() > 1) {
                    destChooser2.setVisibility(View.VISIBLE);
                    destChooser2.setText(cards.get(1).description());
                } else {
                    destChooser2.setVisibility(View.GONE);
                }
                if(cards.size() > 2) {
                    destChooser3.setVisibility(View.VISIBLE);
                    destChooser3.setText(cards.get(2).description());
                } else {
                    destChooser3.setVisibility(View.GONE);
                }
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

    public void showGameOver() {
        startActivity(new Intent(GameActivity.this, GameOverActivity.class));
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        Context context;

        MyGestureListener(Context context) {
            super();
            this.context = context;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(gamePresenter.turnState != TurnState.beginning) { return true; }
            Cord touch = new Cord(e.getX(), e.getY(), false);

            final Route route = MapHelper.getRoute(touch);
            if(route == null) { return true; }

            Model model = Atom.getInstance().getModel();
            Game currentGame = model.getCurrentGame();
            List<Player> players = currentGame.getPlayers();
            for(Player player : players) {
                if(player.getRoutes().contains(route.fooDouble()) && (player == model.getCurrentPlayer() || players.size() < 4)) {
                    return true;
                }
            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Build Route")
                    .setMessage("Do you want to build from " + MapHelper.getName(route.getCity1()) + " to " + MapHelper.getName(route.getCity2()) + " with " + route.getType() + "?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                             if( route.type == any) {
                                 final ArrayAdapter<String> adp = new ArrayAdapter<String>(context,
                                         android.R.layout.simple_spinner_item, TrainType.strings);

                                 final Spinner sp = new Spinner(context);
                                 sp.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                                 sp.setAdapter(adp);
                                 AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                         .setMessage("What train type would you like to use?")
                                         .setView(sp)
                                         .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                             @Override
                                             public void onClick(DialogInterface dialog, int which) {
                                                 gamePresenter.claimRoute(route, TrainType.valueOf((String) sp.getSelectedItem()));
                                             }
                                         });
                                 builder.create().show();
                             } else {
                                 gamePresenter.claimRoute(route, route.type);
                             }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

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
                holder.score.setText("Score: " + player.getRoutePoints());

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
                    trainCardString.append(Integer.toString(TrainType.countCards(trainType, trainCards)));
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
                holder.score.setText("Score: " + player.getRoutePoints());

                holder.destCards.setText("Destination Cards: " + player.getDestCards().size());
                holder.trainCards.setText("Train Cards: " + player.getTrainCards().size());
                holder.trainsLeft.setText("Trains left: " + player.getTrainsLeft());
            }
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
