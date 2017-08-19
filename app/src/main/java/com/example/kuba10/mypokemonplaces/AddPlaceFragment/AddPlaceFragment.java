package com.example.kuba10.mypokemonplaces.AddPlaceFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kuba10.mypokemonplaces.FragmentListener;
import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPlaceFragment extends DialogFragment {

    @BindView(R.id.description_input)
    TextView descriptionField;

    @BindView(R.id.title_input)
    TextView titleField;

    @BindView(R.id.add_button)
    Button addButt;

    @BindView(R.id.cancel_button)
    Button cancelButt;

private FragmentListener fragmentListener;


    public static AddPlaceFragment newInstance() {

        return new AddPlaceFragment();

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

                PokePlace place = new PokePlace();
                place.setTitle(titleField.getText().toString());
                place.setDesctription(descriptionField.getText().toString());

                place.setTest1("test1");
                place.setTest2("test2");

                fragmentListener.savePlace(place);

                dismissDialog();


            }
        });

        cancelButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismissDialog();

            }
        });


        return view;
    }

    private void dismissDialog(){

        this.dismiss();
    }
}
