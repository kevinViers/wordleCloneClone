package com.example.osproject.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.osproject.Adapters.FriendsRequestAdapter
import com.example.osproject.Adapters.friendsAdapter
import com.example.osproject.AddFriend
import com.example.osproject.GnS.FriendsList
import com.example.osproject.GnS.Requests
import com.example.osproject.R
import com.parse.Parse
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser


class FriendsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_friends, container, false)

        //Declarations
        var recyclerView : RecyclerView = view.findViewById(R.id.friendsRv)
        var add : Button = view.findViewById(R.id.FriendsAdd)
        var friends = ArrayList<ParseUser>()
        lateinit var requestAdapter: FriendsRequestAdapter
        lateinit var adapter : friendsAdapter
        var friendsRequest : RecyclerView = view.findViewById(R.id.friendsRequestRv)
        var requestsList = ArrayList<Requests>()


        var onClickListener : FriendsRequestAdapter.OnClickListener = FriendsRequestAdapter.OnClickListener { requests, x, position ->
            if (x) {
                //Adds to friendsList
                var temp = ParseUser.getCurrentUser()
                var newFriend = ArrayList<ParseUser>()
                if (ParseUser.getCurrentUser().getList<ParseUser>("friendsList") != null) {
                    newFriend.addAll(temp.getList("friendsList")!!)
                }
                newFriend.add(requests.requester)
                temp.put("friendsList", newFriend)
                temp.saveInBackground()
                var friendsList : FriendsList
                var list = ArrayList<ParseUser>()
                adapter.notifyDataSetChanged()

                //Creates new FriendsList
                var tempFriends = FriendsList()
                var tempReq = ArrayList<ParseUser>()
                tempReq.add(requests.requested)
                tempFriends.setfriendsList(tempReq)
                tempFriends.curr = requests.requester
                tempFriends.saveInBackground {
                    if (it != null) {
                        Log.e("FriendsListException: ", "" + it)
                        return@saveInBackground
                    }
                }

                //Deletes Request
                requestsList.removeAt(position)
                requestAdapter.notifyDataSetChanged()
                requests.deleteInBackground()
            }else{

                //Deletes Request
                requestsList.removeAt(position)
                requestAdapter.notifyDataSetChanged()
                requests.deleteInBackground()
            }
        }


        //Removes from FriendsList
        var longclick =  friendsAdapter.OnLongClickListener {
            friends.removeAt(it)
            adapter.notifyDataSetChanged()
            Toast.makeText(context, "Successfully Removed", Toast.LENGTH_SHORT).show()
        }

        //ClickListener to add Friends
        add.setOnClickListener {
            var intent = Intent(context, AddFriend::class.java)
            startActivity(intent)
        }

        //Creates instance of adapter and binds adapter to recyclerView(Friends)
        adapter = friendsAdapter(friends, longclick)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        //Creates instance of adapter and binds adapter to recyclerView(Requests)
        requestAdapter = FriendsRequestAdapter(requestsList, onClickListener)
        friendsRequest.adapter = requestAdapter
        friendsRequest.layoutManager = LinearLayoutManager(context)

        //Sends Query to backend for Friends Request
        var requestQuery : ParseQuery<Requests> = ParseQuery.getQuery("FriendsRequest")
        requestQuery.whereEqualTo("requested", ParseUser.getCurrentUser())
        requestQuery.findInBackground { objects, e ->
            if (e != null) {
                Log.e("FriendsFragment, requestQuery", "Query Exception: " + e)
                return@findInBackground
            }
            Log.i("here", objects.size.toString())
            for (i in objects) {
                requestsList.add(i)
                Log.e("here", i.requester.fetch().username)
            }
            requestAdapter.notifyDataSetChanged()
        }

        if(ParseUser.getCurrentUser().getList<ParseUser>("friendsList") != null){
            friends.addAll(ParseUser.getCurrentUser().getList("friendsList")!!)
        }
        adapter.notifyDataSetChanged()

        return view

    }


}