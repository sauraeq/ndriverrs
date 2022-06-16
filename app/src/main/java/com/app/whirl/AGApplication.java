package com.app.whirl;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class AGApplication extends Application {
    private static AGApplication sInstance;
    public static AGApplication the() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}

