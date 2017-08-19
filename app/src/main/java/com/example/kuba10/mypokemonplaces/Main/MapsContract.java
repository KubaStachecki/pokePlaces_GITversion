package com.example.kuba10.mypokemonplaces.Main;

/**
 * Created by Kuba10 on 14.08.2017.
 */

public interface MapsContract {

    interface View {

        void showSnackbar(String text);
    }



    interface Presenter {


        void getPlacesList();


    }
}
