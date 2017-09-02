package com.example.kuba10.mypokemonplaces.PlaceDetailsFragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kuba10.mypokemonplaces.Constants;
import com.example.kuba10.mypokemonplaces.ListFragment.FirebaseListFragment;
import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.Model.PokemonGo;
import com.example.kuba10.mypokemonplaces.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlaceDetailsFragment extends DialogFragment {


    private TextView title, description;
    public ImageView image, showLocation;
    public ImageButton favouriteBtn;

    private PokePlace userSelectedPlace;
    private ArrayList<PokemonGo> pokemonGo_data_list;
    private FirebaseListFragment parentFragment;
    private Query query;

    public static PlaceDetailsFragment newInstance(PokePlace place) {

        PlaceDetailsFragment fragment = new PlaceDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DETAIL_POKEMON, place);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        setFullScreenStyle();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            userSelectedPlace = (PokePlace) getArguments().getSerializable(Constants.DETAIL_POKEMON);
            parentFragment = (FirebaseListFragment) getFragmentManager().findFragmentByTag(Constants.LIST_FRAGMENT_TAG);
            query = parentFragment.getQuery();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_place_details, container, false);

        pokemonGo_data_list = parentFragment.sendPokemonListToAdapter();

        title = (TextView) view.findViewById(R.id.details_title);
        description = (TextView) view.findViewById(R.id.details_description);
        image = (ImageView) view.findViewById(R.id.details_image);
        showLocation = (ImageView) view.findViewById(R.id.details_show_on_map_button);
        favouriteBtn = (ImageButton) view.findViewById(R.id.details_favourite_button);

        image.setImageResource(R.drawable.ic_034_pikachu_1);
        showLocation.setImageResource(R.drawable.ic_021_pointer);


        title.setText(userSelectedPlace.getTitle());
        description.setText(userSelectedPlace.getDesctription());

        switch (userSelectedPlace.getFavourite()) {
            case 0:
                favouriteBtn.setImageResource(R.drawable.ic_032_star_empty);
                break;
            case 1:
                favouriteBtn.setImageResource(R.drawable.ic_032_star);
                break;

        }

        setPokemonImage();


        favouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (userSelectedPlace.getFavourite()) {

                    case 0:
                        setFavouriteImageAndDatabase(1,
                                String.valueOf(userSelectedPlace.getGlobalID()), userSelectedPlace, R.drawable.ic_032_star);
                        break;

                    case 1:
                        setFavouriteImageAndDatabase(0,
                                String.valueOf(userSelectedPlace.getGlobalID()), userSelectedPlace, R.drawable.ic_032_star_empty);
                        break;
                }


            }
        });


        showLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                parentFragment.dismiss();
                parentFragment.findPosition(userSelectedPlace);

            }
        });

        return view;
    }

    private void setFavouriteImageAndDatabase(int favourite, String s, PokePlace userSelectedPlace, int ic_032_star) {
        userSelectedPlace.setFavourite(favourite);
        DatabaseReference child = query.getRef().child(s);
        child.setValue(userSelectedPlace);
        favouriteBtn.setImageResource(ic_032_star);
    }

    private void setPokemonImage() {
        if (pokemonGo_data_list.size() > 0) {

            if (userSelectedPlace.getPokemonId() == -Constants.EMPTY_POKEMON_ID) {

                image.setImageResource(R.drawable.ic_034_pikachu_1);

            } else {

                setPokemonImage(userSelectedPlace, image);
            }


        } else {

            image.setImageResource(R.drawable.sad_pika);
        }
    }

    private void setPokemonImage(PokePlace place, ImageView image) {
        Picasso.with(parentFragment.getContext())
                .load(pokemonGo_data_list.get(place.getPokemonId()).getImg())
                .into(image);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parentFragment = null;
        query = null;
    }

    private void setFullScreenStyle() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
