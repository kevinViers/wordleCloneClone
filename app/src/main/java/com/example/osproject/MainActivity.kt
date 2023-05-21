package com.example.osproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.osproject.Fragment.FriendsFragment
import com.example.osproject.Fragment.HomeFragment
import com.example.osproject.Fragment.SettingsFragment
import com.example.osproject.GnS.FriendsList
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.ParseQuery
import com.parse.ParseUser

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Declaration
        var fragmentManager = supportFragmentManager
        var bottomnav : BottomNavigationView = findViewById(R.id.Botnav)
        var fragment = Fragment()
        val homeFragment = HomeFragment()
        val settingsFragment = SettingsFragment()
        val friendsFragment = FriendsFragment()

        //Switch case to move Fragments
        bottomnav.setOnItemSelectedListener {
            if(it.itemId == R.id.home){
                fragment = homeFragment
            }else if(it.itemId == R.id.friends){
                fragment = friendsFragment
            }else if(it.itemId == R.id.settings){
                fragment = settingsFragment
            }
            fragmentManager.beginTransaction().replace(R.id.Container, fragment).commit()
            return@setOnItemSelectedListener true
        }
        //Default Fragment Case
        bottomnav.selectedItemId = R.id.home

        //Updates FriendsList
        var query : ParseQuery<FriendsList> = ParseQuery.getQuery("Friends")
        query.whereEqualTo("requester", ParseUser.getCurrentUser())
        query.findInBackground { objects, e ->
            if(e != null){
                Log.e("MainActivity, QueryException: ", ""+e)
                return@findInBackground
            }
            var flist = ArrayList<ParseUser>()
            if(!(ParseUser.getCurrentUser().getList<ParseUser>("friendsList") == null)){
                flist.addAll(ParseUser.getCurrentUser().getList<ParseUser>("friendsList")!!)
            }
            for(i in objects){
                for(x in i.friends){
                    flist.add(x)
                    i.deleteInBackground()
                }
            }
            var user = ParseUser.getCurrentUser()
            user.put("friendsList", flist)
            user.saveInBackground()
        }
    }

}