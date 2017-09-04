package com.example.kuba10.mypokemonplaces.AddPlaceFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kuba10.mypokemonplaces.ChooseFragment.ChooseFragment;
import com.example.kuba10.mypokemonplaces.Utils.Constants;
import com.example.kuba10.mypokemonplaces.Utils.FragmentListener;
import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.Model.PokemonGo;
import com.example.kuba10.mypokemonplaces.R;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPlaceFragment extends Fragment {

    private FragmentListener fragmentListener;
    private LatLng position;
    private ArrayList<PokemonGo> pokemonGoDataList;
    public static final String POSITION = "position";
    public static final String INDEX_NOT_SET = "list_index_not_set";
    private PokemonGo selectedPokemon;

    @BindView(R.id.pokemon_image_view)
    ImageView pokemonImageView;
    @BindView(R.id.description_input)
    TextView descriptionField;
    @BindView(R.id.title_input)
    TextView titleField;


    public static AddPlaceFragment newInstance(LatLng position) {
        AddPlaceFragment fragment = new AddPlaceFragment();
        Bundle args = new Bundle();
        args.putParcelable(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof FragmentListener) {
            fragmentListener = (FragmentListener) getActivity();
        }
        fillPokemonList();
        extractSelectedPosition();
    }


    private void fillPokemonList() {
        pokemonGoDataList = fragmentListener.getPokemonList();
    }

    private void extractSelectedPosition() {
        position = getArguments().getParcelable(POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_place, container, false);
        ButterKnife.bind(this, view);
        return view;
    }



    private int setSelectedPokemonId( ) {
        return (selectedPokemon == null? Constants.EMPTY_POKEMON_ID : decreaseListPositionForId());
    }

    private int decreaseListPositionForId() {
        return selectedPokemon.getId() - 1;
    }

    private void setPokemonImage() {
        Picasso.with(getContext())
                .load(selectedPokemon.getImg())
                .into(pokemonImageView);
    }

    private void closeFragment() {
        fragmentListener.closeFragment(this);
    }


    public void setCurrentPokemonData(PokemonGo pokemon) {
        this.selectedPokemon = pokemon;
        setPokemonImage();
        titleField.setText(pokemon.getName());
        descriptionField.setText(String.format("%s%s",getResources().getString(R.string.AverageSpawnText),  pokemon.getAvgSpawns().toString()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    @OnClick(R.id.add_button)
    public void addButtonClick(View view) {

        if (titleField.getText().toString().isEmpty()) {
            fragmentListener.showSnackbar(getString(R.string.placeMustHaveTitle));
        } else {
            fragmentListener.savePlace(inflateCurrentPlace());
            closeFragment();
        }
    }

    @OnClick(R.id.cancel_button)
    public void cancelButtonClick(View view) {
        closeFragment();
    }

    @OnClick(R.id.pokemon_image_view)
    public void iconSelectionClick(View view) {
        fragmentListener.openFragment(ChooseFragment.newInstance(pokemonGoDataList));
    }

    private PokePlace inflateCurrentPlace(){

        Date date = new Date();
        Long id = date.getTime();

        return new PokePlace.PlaceBuilder()
                .setTitle(titleField.getText().toString())
                .setDesctription(descriptionField.getText().toString())
                .setLat(position.latitude)
                .setLong(position.longitude)
                .setListPosition(INDEX_NOT_SET)
                .setGlobalID(id)
                .setPokemonId(setSelectedPokemonId())
                .setFavourite(0)
                .build();
    }

}
