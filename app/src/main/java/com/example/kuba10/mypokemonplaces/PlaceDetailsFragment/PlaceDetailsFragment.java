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

import com.example.kuba10.mypokemonplaces.Utils.Constants;
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
    private ImageView image, showLocation;
    private ImageButton favouriteBtn;
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
            query = parentFragment.getOrderByChildQuery();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_details, container, false);
        inflateViewElementsWithoutButterknifeBecauseImLazy(view);
        title.setText(userSelectedPlace.getTitle());
        description.setText(userSelectedPlace.getDesctription());

                favouriteBtn.setImageResource(userSelectedPlace.isFavourite()? R.drawable.ic_032_star: R.drawable.ic_032_star_empty);

        setPokemonImage();
        favouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean i = userSelectedPlace.isFavourite();
                if (!i) {
                    setFavouriteImageAndDatabase(1,
                            String.valueOf(userSelectedPlace.getGlobalID()), userSelectedPlace, R.drawable.ic_032_star);
                } else if (i) {
                    setFavouriteImageAndDatabase(0,
                            String.valueOf(userSelectedPlace.getGlobalID()), userSelectedPlace, R.drawable.ic_032_star_empty);
                }
            }
        });
        showLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                parentFragment.dismiss();
                parentFragment.moveCameraToPlacePosition(userSelectedPlace);
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
        if (!pokemonGo_data_list.isEmpty()) {
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

    private void setFullScreenStyle() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parentFragment = null;
        query = null;
    }

    private void inflateViewElementsWithoutButterknifeBecauseImLazy(View view) {
        pokemonGo_data_list = parentFragment.sendPokemonListToAdapter();
        title = view.findViewById(R.id.details_title);
        description = view.findViewById(R.id.details_description);
        image = view.findViewById(R.id.details_image);
        showLocation = view.findViewById(R.id.details_show_on_map_button);
        favouriteBtn = view.findViewById(R.id.details_favourite_button);
        image.setImageResource(R.drawable.ic_034_pikachu_1);
        showLocation.setImageResource(R.drawable.ic_021_pointer);
    }
}
