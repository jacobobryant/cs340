package com.thefunteam.android.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.gson.Gson;
import com.thefunteam.android.ClientCommunicator;
import com.thefunteam.android.Poller;
import com.thefunteam.android.R;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;
import com.thefunteam.android.model.UserCommand;
import com.thefunteam.android.presenter.MessagePresenter;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class MessageActivity extends ObservingActivity {

    MessagePresenter msgPresenter = new MessagePresenter(this);

    public MessageActivity(){
        super();
        presenter = msgPresenter;
    }

    private RecyclerView messageLogView;
    private List<String> messageLog = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private Button backToGameButton;

    private EditText messageTosend;
    private Button sendMessageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messageLogView = (RecyclerView) findViewById(R.id.messageLog);
        messageLogView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter(messageLog);
        messageLogView.setAdapter(adapter);

        backToGameButton = (Button) findViewById(R.id.Back_to_Game);
        backToGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we do not need a presenter function right? we do not have to report to the server
                finish();
            }
        });

        messageTosend = (EditText) findViewById(R.id.message);
        sendMessageButton  = (Button) findViewById(R.id.sendMessage);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!messageTosend.equals("")){
                    //send some command to server
                    msgPresenter.sendMessage(messageTosend.getText().toString());
                    messageTosend.setText("");
                }
            }
        });

        Poller.getInstance().startPolling();

        update(Atom.getInstance().getModel());
    }

    public void update(Model model){
        if (model.getCurrentGame() == null) {
            return;
        }
        List<String> newLog = model.getCurrentGame().getMessages();
        messageLog.clear();

        if(newLog == null || newLog.size() == 0){
            messageLog.add("no message");
        }
        if(newLog != null){
            messageLog.addAll(newLog);
        }
        adapter.notifyDataSetChanged();
    }

    public class ListAdapter extends RecyclerView.Adapter<MessageActivity.ListAdapter.ViewHolder>{

        private List<String> listItems;

        ListAdapter(List<String> listItems) {
            this.listItems = listItems;
        }

        @Override
        public MessageActivity.ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_view, parent, false);
            return new MessageActivity.ListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MessageActivity.ListAdapter.ViewHolder holder, int position) {
            holder.textDescription.setText(listItems.get(position));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Poller.getInstance().stopPolling();
    }
}
