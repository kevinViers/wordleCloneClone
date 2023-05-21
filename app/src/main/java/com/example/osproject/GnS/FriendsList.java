package com.example.osproject.GnS;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.List;


@ParseClassName("Friends")
public class FriendsList extends ParseObject {

    //Getters
    public ParseUser getCurr(){
        return ParseUser.getCurrentUser();
    }
    public List<ParseUser> getFriends(){
        return getList("friendsList");
    }

    //Setters
    public void setCurr(ParseUser x){
        put("requester", x);
    }
    public void setfriendsList(List<ParseUser> x){
        put("friendsList", x);
    }
}
