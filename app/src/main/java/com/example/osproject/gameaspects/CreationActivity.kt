package com.example.osproject.gameaspects

import android.app.Activity
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.osproject.Fragment.HomeFragment
import com.example.osproject.GnS.GnSGames
import com.example.osproject.MainActivity
import com.example.osproject.R
import com.example.osproject.gameaspects.addPlayers
import com.parse.ParseUser
import org.parceler.Parcels
import java.lang.reflect.TypeVariable
import kotlin.math.log

class creationActivity : AppCompatActivity() {

    var playerList = ArrayList<ParseUser>()

    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation)
        //Declarations
        var word : EditText = findViewById(R.id.currWord)
        var title : EditText = findViewById(R.id.createTitle)
        var add : TextView = findViewById(R.id.addPlayers)
        var confirm : Button = findViewById(R.id.createGame)


        //Add Players function
        add.setOnClickListener {
            var intent = Intent(applicationContext, addPlayers::class.java)
            resultLauncher.launch(intent)
        }
        //Click Listener
        confirm.setOnClickListener {
            if(playerList.isEmpty() || playerList.size <= 1){
                Toast.makeText(this, "Players must be greater than one", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if(word.text.isEmpty()){
                Toast.makeText(this, "Game must have a beginning word", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if(word.text.length != 5){
                Toast.makeText(this, "Word must be five letters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var newGame = GnSGames()
            newGame.players = playerList
            var score = HashMap<String, Int>()
            var numattempt = HashMap<String, Int>()
            var attempt = HashMap<String, List<String>>()
            var solved = HashMap<String, Boolean>()
            for(i in playerList){
                score.put(i.username, 0)
                numattempt.put(i.username, 0)
                attempt.put(i.username, ArrayList<String>())
                solved.put(i.username, false)
            }
            newGame.score = score
            newGame.setnumAttempt(numattempt)
            newGame.attempted = attempt
            newGame.title = title.text.toString()
            newGame.curr = word.text.toString()
            newGame.setcurrSet(ParseUser.getCurrentUser())
            newGame.solved = solved
            newGame.saveInBackground {
                if(it != null){
                    Log.e("CreationActivitySaveException:" , it.toString())
                    Toast.makeText(this, "Unable to create game", Toast.LENGTH_SHORT).show()
                    return@saveInBackground
                }
                Toast.makeText(this, "Successfully created", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }
    @RequiresApi(33)
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data

            if (data != null) {
                var ret  = ArrayList<String>()
                ret.addAll(data.getStringArrayListExtra("players")!!)
                val temp = ParseUser.getQuery()
                temp.findInBackground { objects, e ->
                    if(e != null){
                        Log.e("CreationUserQueryException: ", e.toString())
                        return@findInBackground
                    }
                    for(i in objects){
                        if(ret.contains(i.username)){
                            playerList.add(i)
                        }
                    }
                }
                playerList.add(ParseUser.getCurrentUser())
            }
        }else{

        }
    }
}