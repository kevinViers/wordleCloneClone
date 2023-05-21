package com.example.osproject.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;
import java.util.zip.DeflaterInputStream;

public class addPlayersAdapter extends RecyclerView.Adapter<addPlayersAdapter.Viewholder>{
    //ClickListener
    public interface OnClickListener{
        void onItemClicked(int pos);
    }
    //Declarations
    List<ParseUser> players;
    OnClickListener onClickListener;
    public addPlayersAdapter(List<ParseUser> players, OnClickListener onClickListener){
        this.players = players;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ParseUser user = players.get(position);
        holder.bind(user, position);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView temp;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            temp = itemView.findViewById(android.R.id.text1);
        }

        public void bind(ParseUser user, int position) {
            try {
                temp.setText(user.fetchIfNeeded().getUsername());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClicked(position);
                }
            });
        }
    }
}
