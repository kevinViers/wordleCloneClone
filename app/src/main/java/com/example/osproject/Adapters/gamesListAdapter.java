package com.example.osproject.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.osproject.GnS.GnSGames;
import com.example.osproject.R;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;
import java.util.Map;

public class gamesListAdapter extends RecyclerView.Adapter<gamesListAdapter.Viewholder>{
    //ClickOverrider
    public interface OnClickListener{
        void onItemClicked(GnSGames games);
    }

    //Declarations
    List<GnSGames> gnSGames;
    OnClickListener onClickListener;
    //Constructor
    public gamesListAdapter(List<GnSGames> gnSGames, OnClickListener onClickListener){
        this.gnSGames = gnSGames;
        this.onClickListener = onClickListener;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gameslistitem, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        GnSGames temp = gnSGames.get(position);
        holder.bind(temp);
    }

    @Override
    public int getItemCount() {
        return gnSGames.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        //Declarations
        TextView score, attempt, players, title;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            //Bind
            score = itemView.findViewById(R.id.gameScore);
            attempt = itemView.findViewById(R.id.playersAttempts);
            players = itemView.findViewById(R.id.players);
            title = itemView.findViewById(R.id.gameTitle);
        }

        public void bind(GnSGames temp) {
            score.setText(score.getText().toString() + temp.getScore());
            for(ParseUser i : temp.getPlayers()){
                try {
                    players.setText(players.getText() + i.fetchIfNeeded().getUsername()+", ");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            attempt.setText(attempt.getText().toString() + temp.getnumAttempts());
            title.setText(title.getText().toString() + temp.getTitle());
            score.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClicked(temp);
                }
            });
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClicked(temp);
                }
            });
            attempt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClicked(temp);
                }
            });
            players.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClicked(temp);
                }
            });
        }
    }
}
