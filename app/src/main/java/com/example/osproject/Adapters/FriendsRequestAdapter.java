package com.example.osproject.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.osproject.GnS.Requests;
import com.example.osproject.R;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

public class FriendsRequestAdapter extends RecyclerView.Adapter<FriendsRequestAdapter.Viewholder>{
    //ClickListener
    public interface OnClickListener{
        void onItemClicked(Requests requests, boolean x, int position);
    }
    //Declarations
    List<Requests> requests;
    OnClickListener onClickListener;

    //Constructor
    public FriendsRequestAdapter(List<Requests> requests, OnClickListener onClickListener){
        this.requests = requests;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requestsitem, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Requests req = requests.get(position);
        holder.bind(req, position);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        //Declarations
        TextView requestor;
        Button accept, reject;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            requestor = itemView.findViewById(R.id.requestUser);
            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);
        }

        public void bind(Requests req, int position) {
            requestor.setText(req.getRequester().getUsername());
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClicked(req, true, position);
                }
            });
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClicked(req, false, position);
                }
            });

        }
    }
}
