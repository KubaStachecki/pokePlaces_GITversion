package com.example.kuba10.mypokemonplaces;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Kuba10 on 14.08.2017.
 */

public class MyPokemonPlaces extends Application {

    private static MyPokemonPlaces mainApplication;


    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mainApplication = this;



    }
}
