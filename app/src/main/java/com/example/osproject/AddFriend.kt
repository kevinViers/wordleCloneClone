package com.example.osproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.osproject.Adapters.addFriendsAdapter
import com.example.osproject.GnS.Requests
import com.parse.Parse
import com.parse.ParseQuery
import com.parse.ParseUser
import org.w3c.dom.Text
import kotlin.collections.Map as Map

class AddFriend : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        //Declarations
        var fullUser = HashMap<String, ParseUser>()
        var users = HashMap<String, ParseUser>()
        var input : EditText = findViewById(R.id.addfriendsET)
        var rv : RecyclerView = findViewById(R.id.addFriendsRv)
        var removeList = ArrayList<String>()
        var adapter : addFriendsAdapter
        var onClickListener : addFriendsAdapter.OnClickListener
        var usernames = ArrayList<ParseUser>()

        //Creates Friend request
        onClickListener = addFriendsAdapter.OnClickListener {
            var request = Requests()
            request.setRequester(ParseUser.getCurrentUser())
            request.setRequested(it)

            request.saveInBackground {
                if(it != null){
                    Log.e("AddFriends", "Create Request Exception: " + it)
                    return@saveInBackground
                }
                Toast.makeText(this, "Successfully sent", Toast.LENGTH_SHORT).show()
            }
        }

        //RecyclerView bindings and adapter initialization
        adapter = addFriendsAdapter(usernames, onClickListener)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

        //Sends Query to backend for friends list
        var parseQuery : ParseQuery<ParseUser> = ParseUser.getQuery()
        parseQuery.findInBackground { objects, e ->
            if(e != null){
                Log.e("FriendsFragment", "Query Exception: " + e)
                return@findInBackground
            }
            for(i in objects){
                //Skips current user to not display
                if(i.objectId == ParseUser.getCurrentUser().objectId)continue
                //Full user list
                fullUser.put(i.objectId, i)
                //Adapter List
                users.put(i.objectId, i)
            }
            mapChanged(users, usernames)
            adapter.notifyDataSetChanged()
        }

        //TextListener based on change
        input.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            //Changes display list of users based on input of string
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().isEmpty()){
                    users.putAll(fullUser)
                    mapChanged(users, usernames)
                    adapter.notifyDataSetChanged()
                }
                for(i in fullUser){
                    //Iterates through users, matching string
                    if(!i.value.username.contains(s.toString()))removeList.add(i.value.objectId.toString())
                }
                for(i in removeList){
                    if(users.containsKey(i)){
                        users.remove(i)
                        mapChanged(users, usernames)
                        adapter.notifyDataSetChanged()
                    }
                }
                removeList.clear()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
    //Shifts map values to list
    fun mapChanged(x : Map<String, ParseUser>, y : ArrayList<ParseUser>){
        y.clear()
        for(i in x){
            y.add(i.value)
        }
    }
}