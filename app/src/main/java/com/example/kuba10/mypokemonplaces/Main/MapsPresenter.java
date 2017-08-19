package com.example.kuba10.mypokemonplaces.Main;

import android.util.Log;

import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Kuba10 on 14.08.2017.
 */

public class MapsPresenter implements MapsContract.Presenter {


    private final MapsContract.View view;

    public MapsPresenter(MapsContract.View view) {
        this.view = view;
    }


    @Override
    public void getPlacesList() {

    }
}




