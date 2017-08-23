package com.example.kuba10.mypokemonplaces.PlaceDetailsFragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.Model.PokemonGo;
import com.example.kuba10.mypokemonplaces.R;

public class PlaceDetailsFragment extends DialogFragment {


    public static final String DETAIL_POKEMON = "DetailPokemon";
    private TextView title, description;
    public ImageView image, showLocation;
    public ImageButton favouriteBtn;
    private PokePlace place;


    public static PlaceDetailsFragment newInstance(PokePlace place) {
        PlaceDetailsFragment fragment = new PlaceDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(DETAIL_POKEMON, place);
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

            place = getArguments().getParcelable(DETAIL_POKEMON);


        }
    }

    private void setFullScreenStyle() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_place_details, container, false);

        title = (TextView) view.findViewById(R.id.details_title);
        description = (TextView) view.findViewById(R.id.details_description);
        image = (ImageView) view.findViewById(R.id.details_image);
        showLocation = (ImageView) view.findViewById(R.id.details_show_on_map_button);
        favouriteBtn = (ImageButton) view.findViewById(R.id.details_favourite_button);

        image.setImageResource(R.drawable.ic_034_pikachu_1);
        showLocation.setImageResource(R.drawable.ic_001_pointer);


        title.setText(place.getTitle());
        description.setText(place.getDesctription());

        switch (place.getFavourite()) {
            case 0:
                favouriteBtn.setImageResource(R.drawable.ic_032_star_empty);
                break;
            case 1:
                favouriteBtn.setImageResource(R.drawable.ic_032_star);
                break;

        }

            return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
