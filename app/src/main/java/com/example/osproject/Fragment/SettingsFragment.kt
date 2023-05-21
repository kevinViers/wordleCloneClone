package com.example.osproject.Fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.osproject.LoR.Login
import com.example.osproject.R
import com.example.osproject.LoR.passwordActivity
import com.parse.ParseUser


class SettingsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        var user = view.findViewById<EditText>(R.id.settingsUser)
        var pass = view.findViewById<TextView>(R.id.settingsPassword)
        var logout : Button = view.findViewById(R.id.logout)

        user.addTextChangedListener(object : TextWatcher{
            lateinit var before : String
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                before = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                //Queries usernames
                var query = ParseUser.getQuery()
                query.findInBackground { objects, e ->
                    if(e != null){
                        Log.e("SettingsQueryException: ", e.toString())
                        return@findInBackground
                    }
                    for(i in objects){
                        if(user.equals(i.username)){
                            Toast.makeText(context, "Username already exists", Toast.LENGTH_SHORT).show()
                            return@findInBackground
                        }
                    }
                    //Changes username
                    var pUser = ParseUser.getCurrentUser()
                    pUser.username = user.text.toString()
                    pUser.saveInBackground {
                        if(it != null){
                            Log.e("SettingsSaveException: " , it.toString())
                            return@saveInBackground
                        }
                        Toast.makeText(context, "Successfully changed username", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
        //Logs user out
        logout.setOnClickListener {
            ParseUser.logOutInBackground {
                if(it != null){
                    Log.e("SettingFragmentsLogoutException: ", "" + it)
                    return@logOutInBackground
                }
                val intent = Intent(context, Login::class.java)
                startActivity(intent)
            }
        }

        //Change password activity
        pass.setOnClickListener {
            val intent = Intent(context, passwordActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}