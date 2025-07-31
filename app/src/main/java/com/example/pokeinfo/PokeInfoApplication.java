package com.example.pokeinfo;

import android.app.Application;
import com.example.pokeinfo.data.db.AppDatabase;

public class PokeInfoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the database
        AppDatabase.getInstance(this);
    }
}
