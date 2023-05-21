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

    private val homeFragment = HomeFragment()
    private val settingsFragment = SettingsFragment()
    private val friendsFragment = FriendsFragment()

    companion object {
        private const val FRAGMENT_HOME = R.id.home
        private const val FRAGMENT_FRIENDS = R.id.friends
        private const val FRAGMENT_SETTINGS = R.id.settings
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val bottomnav: BottomNavigationView = findViewById(R.id.Botnav)
        var selectedFragment: Fragment = homeFragment

        bottomnav.setOnItemSelectedListener { item ->
            selectedFragment = when (item.itemId) {
                FRAGMENT_HOME -> homeFragment
                FRAGMENT_FRIENDS -> friendsFragment
                FRAGMENT_SETTINGS -> settingsFragment
                else -> homeFragment // Set a default fragment here
            }
            fragmentManager.beginTransaction().replace(R.id.Container, selectedFragment).commit()
            true
        }

        bottomnav.selectedItemId = FRAGMENT_HOME

        updateFriendsList()
    }

    private fun updateFriendsList() {
        val query: ParseQuery<FriendsList> = ParseQuery.getQuery("Friends")
        query.whereEqualTo("requester", ParseUser.getCurrentUser())
        query.findInBackground { objects, e ->
            if (e != null) {
                Log.e("MainActivity", "QueryException: $e")
                return@findInBackground
            }
            
            val flist = ArrayList<ParseUser>()
            ParseUser.getCurrentUser().getList<ParseUser>("friendsList")?.let { flist.addAll(it) }

            objects.forEach { i ->
                i.friends.forEach { x ->
                    flist.add(x)
                    i.deleteInBackground()
                }
            }

            val user = ParseUser.getCurrentUser()
            user.put("friendsList", flist)
            user.saveInBackground()
        }
    }
}
