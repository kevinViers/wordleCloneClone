package com.example.osproject.Adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.osproject.GnS.GnSGames;
import com.example.osproject.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class gameScreenAdapter extends RecyclerView.Adapter<gameScreenAdapter.Viewholder>{
    //ClickListener
    //Onclick Override
    public interface OnClickListener{
        void onItemClicked(GnSGames gam, int pos, boolean c, List<String> x);
    }
    OnClickListener onClickListener;
    List<String> attempts;
    String curr;
    GnSGames game;
    int pos = 1;
    public gameScreenAdapter(List<String> attempts, String curr, GnSGames game, OnClickListener onClickListener){
        this.attempts=attempts;
        this.curr=curr;
        this.game=game;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gamescreenitem, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return pos;
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        //Declarations
        EditText l1,l2,l3,l4,l5;
        Button submit;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            //Attach
            l1 = itemView.findViewById(R.id.letter1);
            l2 = itemView.findViewById(R.id.letter2);
            l3 = itemView.findViewById(R.id.letter3);
            l4 = itemView.findViewById(R.id.letter4);
            l5 = itemView.findViewById(R.id.letter5);
            submit = itemView.findViewById(R.id.submit);
            //Initializes previous attempts
            if(pos - 1 < attempts.size()  && pos < 6){
                //Sets previous attempts
                StringBuilder temp = new StringBuilder(attempts.get(pos - 1));
                l1.setText(String.valueOf(temp.charAt(0)));
                l2.setText(String.valueOf(temp.charAt(1)));
                l3.setText(String.valueOf(temp.charAt(2)));
                l4.setText(String.valueOf(temp.charAt(3)));
                l5.setText(String.valueOf(temp.charAt(4)));
                //Disables change
                l1.setEnabled(false);
                l2.setEnabled(false);
                l3.setEnabled(false);
                l4.setEnabled(false);
                l5.setEnabled(false);
                submit.setVisibility(View.INVISIBLE);
                pos++;
                return;
            }
            //Check if maximum attempt has been made
            if(pos == 6){
                Toast.makeText(itemView.getContext(), "All possible attempts have been made", Toast.LENGTH_SHORT).show();
                return;
            }
            //Checks new attempts;
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Char to String
                    StringBuilder builder = new StringBuilder(curr);
                    StringBuilder coll = new StringBuilder();
                    coll.append(l1.getText().toString());
                    coll.append(l2.getText().toString());
                    coll.append(l3.getText().toString());
                    coll.append(l4.getText().toString());
                    coll.append(l5.getText().toString());
                    if (pos <= 6) {
                        //Word match
                        if (coll.toString().equals(curr)) {
                            attempts.add(builder.toString());
                            //Check if first solved
                            Boolean c = true;
                            for (Map.Entry<String, Boolean> i : game.getSolved().entrySet()) {
                                if (c == i.getValue()) {
                                    c = false;
                                }
                            }
                            if (c) {
                                onClickListener.onItemClicked(game, pos, c, attempts);
                                submit.setVisibility(View.INVISIBLE);
                                return;
                            } else {
                                //Checks if all have finish
                                boolean all = true;
                                for (Map.Entry<String, Boolean> i : game.getSolved().entrySet()) {
                                    if (all != i.getValue()) {
                                        all = false;
                                    }
                                }
                                //Conditions
                                if (all) {
                                    //Resets game into new game
                                    Map<String, Integer> score = game.getScore();
                                    score.put(ParseUser.getCurrentUser().getUsername(), score.get(ParseUser.getCurrentUser().getUsername()) + (6 - attempts.size()));
                                    HashMap<String, Integer> numattempted = new HashMap<>();
                                    HashMap<String, List<String>> attempted = new HashMap<>();
                                    HashMap<String, Boolean> solved = new HashMap<>();
                                    for (ParseUser x : game.getPlayers()) {
                                        numattempted.put(x.getUsername(), 0);
                                        attempted.put(x.getUsername(), new ArrayList<String>());
                                        solved.put(x.getUsername(), false);
                                    }
                                    game.setScore(score);
                                    game.setnumAttempt(numattempted);
                                    game.setAttempted(attempted);
                                    game.setCurr(game.getNext());
                                    game.setSolved(solved);
                                    game.setNext("");

                                    game.saveInBackground();
                                } else {
                                    Toast.makeText(itemView.getContext(), "Solved!", Toast.LENGTH_SHORT).show();
                                    HashMap<String, Integer> temp = new HashMap<>();
                                    temp.put(ParseUser.getCurrentUser().getObjectId(), pos);
                                    game.setnumAttempt(temp);
                                    HashMap<String, List<String>> atemp = new HashMap<>();
                                    atemp.put(ParseUser.getCurrentUser().getObjectId(), attempts);
                                    return;
                                }
                            }
                        } else {

                            //Non-match
                            int green = Color.rgb(0, 255, 0);
                            int red = Color.rgb(255, 0, 0);
                            if (l1.getText().toString().equals(String.valueOf(builder.charAt(0)))) {
                                l1.setBackgroundColor(green);
                                l1.setEnabled(false);
                            } else {
                                l1.setBackgroundColor(red);
                                l1.setEnabled(false);
                            }
                            if (l2.getText().toString().equals(String.valueOf(builder.charAt(1)))) {
                                l2.setBackgroundColor(green);
                                l2.setEnabled(false);
                                Log.e("here", l2.getText().toString() + " " + String.valueOf(builder.charAt(2)));
                            } else {
                                l2.setBackgroundColor(red);
                                l2.setEnabled(false);
                            }
                            if (l3.getText().toString().equals(String.valueOf(builder.charAt(2)))) {
                                l3.setBackgroundColor(green);
                                l3.setEnabled(false);
                            } else {
                                l3.setBackgroundColor(red);
                                l3.setEnabled(false);
                            }
                            if (l4.getText().toString().equals(String.valueOf(builder.charAt(3)))) {
                                l4.setBackgroundColor(green);
                                l4.setEnabled(false);
                            } else {
                                l4.setBackgroundColor(red);
                                l4.setEnabled(false);
                            }
                            if (l5.getText().toString().equals(String.valueOf(builder.charAt(4)))) {
                                l5.setBackgroundColor(green);
                                l5.setEnabled(false);
                            } else {
                                l5.setBackgroundColor(red);
                                l5.setEnabled(false);
                            }
                            pos++;
                            attempts.add(coll.toString());
                            submit.setVisibility(View.INVISIBLE);
                            notifyItemInserted(pos);
                            HashMap<String, List<String>> temp = new HashMap<>();
                            temp.putAll(game.getAttempted());
                            temp.put(ParseUser.getCurrentUser().getUsername(), attempts);
                            game.setAttempted(temp);
                            game.saveInBackground();
                        }
                    }
                }
            });

        }
    }
}
