package com.example.osproject.GnS;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.Map;

@ParseClassName("FriendsRequest")
public class Requests extends ParseObject {
    //Getters
    public ParseUser getRequester(){
        return getParseUser("requester");
    }
    public ParseUser getRequested(){
        return getParseUser("requested");
    }

    //Setters
    public void setRequester(ParseUser x){
        put("requester", x);
    }
    public void setRequested(ParseUser x){
        put("requested", x);
    }

}
