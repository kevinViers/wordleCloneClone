package com.example.osproject.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

public class friendsAdapter extends RecyclerView.Adapter<friendsAdapter.Viewholder>{

    public interface OnLongClickListener{
        void onItemLongClick(int position);
    }

    //Declaration
    List<ParseUser> friends;
    OnLongClickListener longClickListener;
    //Constructor
    public friendsAdapter(List<ParseUser> friends, OnLongClickListener longClickListener){
        this.friends = friends;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new Viewholder(todoView);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ParseUser user = friends.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        //Declarations
        TextView username;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(android.R.id.text1);
        }

        public void bind(ParseUser user) {
            try {
                username.setText(user.fetch().getUsername());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            username.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onItemLongClick(getAdapterPosition());
                    return true;
                }
            });

        }
    }
}
