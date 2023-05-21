package com.example.osproject.LoR

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.osproject.MainActivity
import com.example.osproject.R
import com.parse.LogInCallback
import com.parse.Parse
import com.parse.ParseUser
import com.parse.SignUpCallback

class Registery : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registery)

        //Declarations
        var username : EditText = findViewById(R.id.Rusername)
        var password : EditText = findViewById(R.id.Rpassword)
        var reg : Button = findViewById(R.id.Rregister)
        var back : Button = findViewById(R.id.Rback)

        //Registers User
        reg.setOnClickListener {

            val user  = ParseUser()
            user.username = username.text.toString()
            user.setPassword(password.text.toString())
            user.put("friendsList", ArrayList<ParseUser>())

            var userQuery = ParseUser.getQuery()
            userQuery.findInBackground { objects, e ->
                if(e != null){
                    Log.e("Registery", "Query Exception: " + e)
                    return@findInBackground
                }
                for(i in objects){
                    if(i.username.equals(username.text.toString())){
                        Toast.makeText(this, "Username already exist, Try again", Toast.LENGTH_SHORT).show()
                        return@findInBackground
                    }
                }
                //Creates new User in Database
                user.signUpInBackground(SignUpCallback() {
                    if(it != null){
                        Log.e("Registery", "SignUpException: " + it)
                    }
                    ParseUser.logInInBackground(username.text.toString(), password.text.toString(), LogInCallback { _, e ->
                        if(e!=null){
                            Log.e("logInInBackground", "Exception = " + e)
                            return@LogInCallback
                        }
                        goMainActivityRegister()
                    })
                })
            }
        }

        //Returns to Login
        back.setOnClickListener {
            var i = Intent(this, Login::class.java)
            startActivity(i)
        }
    }
    private fun goMainActivityRegister(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}