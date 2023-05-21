package com.example.osproject.LoR

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.osproject.MainActivity
import com.example.osproject.R
import com.parse.LogInCallback
import com.parse.ParseUser


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Persistence Check
        if(ParseUser.getCurrentUser() !== null)goMainActivity()

        //Declarations
        var username : EditText = findViewById(R.id.username)
        var password : EditText = findViewById(R.id.password)
        var log : Button = findViewById(R.id.login)
        var reg : Button = findViewById(R.id.register)

        //Logins User
        log.setOnClickListener {
            ParseUser.logInInBackground(username.text.toString(), password.text.toString(), LogInCallback { _, e ->
                if(e!=null){
                    Log.e("logInInBackground", "Exception = " + e)
                    return@LogInCallback
                }
                goMainActivity()
            })
        }

        //Switch from Login to Register
        reg.setOnClickListener {
            goRegistryActivity()
        }

    }

    private fun goRegistryActivity() {
        val intent = Intent(this, Registery::class.java)
        startActivity(intent)
        finish()
    }

    private fun goMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}