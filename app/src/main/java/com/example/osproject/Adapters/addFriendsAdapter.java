package com.example.osproject.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.osproject.R;
import com.parse.ParseUser;

import java.util.List;
import java.util.Map;

public class addFriendsAdapter extends RecyclerView.Adapter<addFriendsAdapter.Viewholder>{

    //Onclick Override
    public interface OnClickListener{
        void onItemClicked(ParseUser user);
    }

    //Declaration
    List<ParseUser> userDisplay;
    OnClickListener onClickListener;

    //Constructor
    public addFriendsAdapter(List<ParseUser> userDisplay, OnClickListener onClickListener){
        this.userDisplay = userDisplay;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ParseUser user = userDisplay.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userDisplay.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        //Declaration
        TextView display;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            display = itemView.findViewById(android.R.id.text1);
        }

        public void bind(ParseUser user) {
            display.setText(user.getUsername());
            display.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClicked(user);
                }
            });
        }
    }
}
