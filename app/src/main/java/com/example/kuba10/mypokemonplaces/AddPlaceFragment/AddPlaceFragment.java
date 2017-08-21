package com.example.kuba10.mypokemonplaces.AddPlaceFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kuba10.mypokemonplaces.FragmentListener;
import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.R;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPlaceFragment extends Fragment {

    @BindView(R.id.description_input)
    TextView descriptionField;

    @BindView(R.id.title_input)
    TextView titleField;

    @BindView(R.id.add_button)
    Button addButt;

    @BindView(R.id.cancel_button)
    Button cancelButt;

    private FragmentListener fragmentListener;


    public static AddPlaceFragment newInstance(LatLng position) {
        AddPlaceFragment fragment = new AddPlaceFragment();
        Bundle args = new Bundle();
        args.putParcelable("position", position);
        fragment.setArguments(args);
        return fragment;


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        fragmentListener = (FragmentListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_place, container, false);

        ButterKnife.bind(this, view);

        addButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LatLng position = getArguments().getParcelable("position");
                double lat = position.latitude;
                double lng = position.longitude;


                PokePlace place = new PokePlace();

                if (titleField.getText().toString().equals("")){
                    fragmentListener.showSnackbar("Place must have a title");
                }else {

                    place.setTitle(titleField.getText().toString());
                    place.setTitle(titleField.getText().toString());
                    place.setDesctription(descriptionField.getText().toString());

                    place.setLong(lng);
                    place.setLat(lat);

                    place.setListPosition("list_index_not_set");
                    place.setTest2("test2");

                }


//                fragmentListener.savePlace(
//                        new PokePlace(descriptionField.getText().toString(), lat, lng, 2147483647, "test2", titleField.getText().toString()));


                    fragmentListener.savePlace(place);

                place = null;




            }
    });

        cancelButt.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View view){


    }
    });


        return view;
}



}
