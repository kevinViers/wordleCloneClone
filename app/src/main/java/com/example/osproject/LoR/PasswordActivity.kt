package com.example.osproject.LoR

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.osproject.Fragment.SettingsFragment
import com.example.osproject.R
import com.parse.LogInCallback
import com.parse.ParseUser

class passwordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        //Declarations
        var oldPass : EditText = findViewById(R.id.oldPass)
        var newPass : EditText = findViewById(R.id.newPass)
        var simPass : EditText = findViewById(R.id.simcheck)
        var confirm : Button = findViewById(R.id.confirmbutton)

        //Save Listener
        confirm.setOnClickListener {
            ParseUser.logInInBackground(ParseUser.getCurrentUser().username, oldPass.text.toString(), LogInCallback { user, e ->
                //Confirms User
                if(e != null){
                    Toast.makeText(this, "Old password incorrect", Toast.LENGTH_SHORT).show()
                    return@LogInCallback
                }
                //Confirms password match
                if(!newPass.equals(simPass)){
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@LogInCallback
                }
                user.setPassword(newPass.text.toString())
                user.saveInBackground {
                    if(it != null){
                        Log.e("passwordActivity, SaveCallback", "Exception: " + it)
                        return@saveInBackground
                    }
                    //Successful case
                    Toast.makeText(this, "Successfully Changed", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this, SettingsFragment::class.java)
                    startActivity(intent)
                }
            })
        }

    }
}