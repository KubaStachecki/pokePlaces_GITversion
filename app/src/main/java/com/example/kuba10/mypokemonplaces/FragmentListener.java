package com.example.kuba10.mypokemonplaces;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.Model.PokemonGo;

import java.util.ArrayList;

/**
 * Created by Kuba10 on 17.08.2017.
 */

public interface FragmentListener {

    void savePlace(PokePlace place);

    void showSnackbar(String text);

    void findPlaceFromList(PokePlace place);

    void openFragment(Fragment fragment);

    void openTaggedFragment(Fragment fragment, String tag);

    void dismiss(Fragment fragment);

    void sendDataToAddFragment(PokemonGo pokemon);

    ArrayList<PokemonGo> getPokemonList();
}
