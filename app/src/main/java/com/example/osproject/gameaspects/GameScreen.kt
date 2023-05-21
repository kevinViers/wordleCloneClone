package com.example.osproject.gameaspects

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.osproject.Adapters.gameScreenAdapter
import com.example.osproject.Fragment.HomeFragment
import com.example.osproject.GnS.GnSGames
import com.example.osproject.MainActivity
import com.example.osproject.R
import com.parse.ParseUser
import org.parceler.Parcels

class gameScreen : AppCompatActivity() {
    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gamescreen)
        var data = intent.extras
        var attempts = ArrayList<String>()
        var curr : String
        var gnSGames : GnSGames = intent.getParcelableExtra("game", GnSGames::class.java)!!
        var gameScreenadapter : gameScreenAdapter
        var nextw : EditText = findViewById(R.id.nextW)
        var recyclerView : RecyclerView = findViewById(R.id.gsRv)

        nextw.visibility = View.INVISIBLE

        var onClickListener = gameScreenAdapter.OnClickListener { gam, pos, c, x ->
            Toast.makeText(applicationContext, "Solved!", Toast.LENGTH_SHORT).show()
            nextw.visibility = View.VISIBLE
            //Records number of attempts
            val temp : MutableMap<String, Int> = gam.getnumAttempts()
            temp[ParseUser.getCurrentUser().username] = 0
            gam.setnumAttempt(temp)
            //Records attempts
            val atemp : MutableMap<String, List<String>> = gam.attempted;
            atemp[ParseUser.getCurrentUser().username] = x
            gam.attempted = atemp
            //Records completion
            val solved : MutableMap<String, Boolean> = gam.solved
            solved[ParseUser.getCurrentUser().username] = true;
            gam.solved = solved
            //Records score
            val score : MutableMap<String, Int> = gam.score
            score[ParseUser.getCurrentUser().username] = score.get(ParseUser.getCurrentUser().username)!! + (6-x.size)
            gam.score = score
            //Sets word setter
            gam.setcurrSet(ParseUser.getCurrentUser())
            nextw.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if(s.toString().length != 4){
                        Toast.makeText(applicationContext, "Word must be 5 letters long", Toast.LENGTH_SHORT).show()
                    }else{
                        gam.next = nextw.text.toString()
                        gam.saveInBackground {
                            if(it != null){
                                Log.e( "GameScreenSaveException: ", it.toString());
                                return@saveInBackground
                            }
                            var intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            })
        }

        curr = gnSGames.curr

        gameScreenadapter = gameScreenAdapter(attempts, curr, gnSGames, onClickListener)
        recyclerView.adapter = gameScreenadapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        if(gnSGames.attempted.get(ParseUser.getCurrentUser().username) != null){
            attempts.addAll(gnSGames.attempted.get(ParseUser.getCurrentUser().username)!!)
            gameScreenadapter.notifyDataSetChanged()
        }

    }
}