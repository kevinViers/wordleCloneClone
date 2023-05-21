package com.example.osproject.gameaspects

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.osproject.Adapters.addPlayersAdapter
import com.example.osproject.R
import com.parse.ParseUser
import org.parceler.Parcels

class addPlayers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_players)

        //Declarations
        var recyclerview : RecyclerView = findViewById(R.id.apRv)
        var finish : Button = findViewById(R.id.apfinish)
        var playerList = ArrayList<String>()
        var posPlayer = ArrayList<ParseUser>()
        lateinit var adapter : addPlayersAdapter
        var onClickListener = addPlayersAdapter.OnClickListener {
            playerList.add(posPlayer.get(it).username)
            posPlayer.removeAt(it)
            adapter.notifyDataSetChanged()
        }

        if(ParseUser.getCurrentUser().get("friendsList") != null){
            posPlayer.addAll(ParseUser.getCurrentUser().getList("friendsList")!!)
        }

        finish.setOnClickListener {
            var intent = Intent()
            intent.putStringArrayListExtra("players", playerList)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        adapter = addPlayersAdapter(posPlayer, onClickListener)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)
    }
}