package com.example.osproject.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.osproject.Adapters.gamesListAdapter
import com.example.osproject.GnS.GnSGames
import com.example.osproject.R
import com.example.osproject.gameaspects.creationActivity
import com.example.osproject.gameaspects.gameScreen
import com.parse.ParseQuery
import com.parse.ParseUser
import org.parceler.Parcels

class HomeFragment : Fragment() {

    companion object{
        fun newInstance(): HomeFragment{
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post, container, false)
        var create : TextView = view.findViewById(R.id.createTV)
        var recyclerView : RecyclerView = view.findViewById(R.id.homeRv)
        var gameList = ArrayList<GnSGames>()
        lateinit var gamesListadapter : gamesListAdapter

        //Click listener, Game variables sent
        var onClickListener : gamesListAdapter.OnClickListener = gamesListAdapter.OnClickListener {
            var intent = Intent(context, gameScreen::class.java)
            intent.putExtra("curr", it.curr)
            var temp  = ArrayList<String>()
            if(it.attempted.get(ParseUser.getCurrentUser().objectId) != null){
                temp.addAll(it.attempted.get(ParseUser.getCurrentUser().objectId)!!)
            }
            intent.putStringArrayListExtra("attempts", temp)
            intent.putExtra("game", it)
            startActivity(intent)

        }

        //initialization of adapter and binding of recyclerview variables
        gamesListadapter = gamesListAdapter(gameList, onClickListener)
        recyclerView.adapter = gamesListadapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        //Queries for games
        var query : ParseQuery<GnSGames> = ParseQuery.getQuery("Game")
        query.findInBackground { objects, e ->
            if(e != null){
                Log.e("HomeFragmentQueryException: ", e.toString())
                return@findInBackground
            }
            for(i in objects){
                for(x in i.players){
                    if(x.fetchIfNeeded().objectId == ParseUser.getCurrentUser().objectId) {
                        gameList.add(i)
                        break
                    }
                }
            }
            gamesListadapter.notifyDataSetChanged()

        }

        //Create games function
        create.setOnClickListener {
            val intent = Intent(context, creationActivity::class.java)
            startActivity(intent)
        }

        return view
    }

}