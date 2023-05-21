package com.example.osproject.ParseC;

import android.app.Application;

import com.example.osproject.GnS.FriendsList;
import com.example.osproject.GnS.GnSGames;
import com.example.osproject.GnS.Requests;
import com.example.osproject.R;
import com.parse.Parse;
import com.parse.ParseObject;

public class parse extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.applicationKey))
                .clientKey(getString(R.string.clientKey))
                .server("https://parseapi.back4app.com")
                .build()
        );
        ParseObject.registerSubclass(FriendsList.class);
        ParseObject.registerSubclass(GnSGames.class);
        ParseObject.registerSubclass(Requests.class);

    }
}
