package com.example.kuba10.mypokemonplaces.AddPlaceFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kuba10.mypokemonplaces.ChooseFragment.ChooseFragment;
import com.example.kuba10.mypokemonplaces.FragmentListener;
import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.Model.PokemonGo;
import com.example.kuba10.mypokemonplaces.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;



public class AddPlaceFragment extends Fragment {

    public static final String LIST = "list";
    public static final String POSITION = "position";
    @BindView(R.id.pokemon_image_view)
    ImageView pokemonImageView;

    @BindView(R.id.description_input)
    TextView descriptionField;

    @BindView(R.id.title_input)
    TextView titleField;

    @BindView(R.id.add_button)
    Button addButt;

    @BindView(R.id.cancel_button)
    Button cancelButt;

    private FragmentListener fragmentListener;
    private ArrayList<PokemonGo> pokemonGo_data_list;


    public static AddPlaceFragment newInstance(LatLng position, ArrayList<PokemonGo> pokemonGo_data_list) {
        AddPlaceFragment fragment = new AddPlaceFragment();
        Bundle args = new Bundle();
        args.putParcelable(POSITION, position);
        args.putParcelableArrayList(LIST, pokemonGo_data_list);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        fragmentListener = (FragmentListener) context;
        pokemonGo_data_list = getArguments().getParcelableArrayList(LIST);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_place, container, false);

        ButterKnife.bind(this, view);

        addButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LatLng position = getArguments().getParcelable(POSITION);
                double lat = position.latitude;
                double lng = position.longitude;

                PokePlace place = new PokePlace();

                if (titleField.getText().toString().equals("")) {
                    fragmentListener.showSnackbar("Place must have a title");
                } else {

                    place.setTitle(titleField.getText().toString());
                    place.setTitle(titleField.getText().toString());
                    place.setDesctription(descriptionField.getText().toString());
                    place.setLong(lng);
                    place.setLat(lat);
                    place.setListPosition("list_index_not_set");
                    place.setFavourite(0);
                    place.setPokemonId(-777);

                    Date date = new Date();
                    Long id = date.getTime();

                    place.setGlobalID(id);

                    fragmentListener.savePlace(place);
                    place = null;

                    dismiss();

                }


            }
        });
        cancelButt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        pokemonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentListener.openFragment(ChooseFragment.newInstance(pokemonGo_data_list));

            }
        });
        return view;
    }

    public void dismiss() {
        fragmentListener.dismiss(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("ADD FRAGMENT", "WYWOLALLO SIE ON RESUME");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d("ADD FRAGMENT", "WYWOLALLO SIE ON PAUSE");

    }


}
