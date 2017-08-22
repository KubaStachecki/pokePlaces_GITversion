package com.example.kuba10.mypokemonplaces;

import android.support.v4.app.Fragment;

import com.example.kuba10.mypokemonplaces.Model.PokePlace;

/**
 * Created by Kuba10 on 17.08.2017.
 */

public interface FragmentListener {

    void savePlace(PokePlace place);

    void showSnackbar(String text);

    void findPlaceFromList(PokePlace place);

    void openFragment(Fragment fragment);

}
