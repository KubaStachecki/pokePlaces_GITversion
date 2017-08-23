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

import com.example.kuba10.mypokemonplaces.ListFragment.FirebaseListFragment;
import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import static com.example.kuba10.mypokemonplaces.Main.MapsActivity.LIST_FRAGMENT_TAG;

public class PlaceDetailsFragment extends DialogFragment {


    public static final String DETAIL_POKEMON = "DetailPokemon";
    private TextView title, description;
    public ImageView image, showLocation;
    public ImageButton favouriteBtn;
    private PokePlace place;
    private FirebaseListFragment parentFragment ;
    Query query;

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
            parentFragment = (FirebaseListFragment) getFragmentManager().findFragmentByTag(LIST_FRAGMENT_TAG);
            query = parentFragment.getQuery();
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

        favouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                switch (place.getFavourite()) {

                    case 0:

                        place.setFavourite(1);
                        DatabaseReference child = query.getRef().child(String.valueOf(place.getGlobalID()));
                        child.setValue(place, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                favouriteBtn.setImageResource(R.drawable.ic_032_star);

                            }
                        });
                        break;

                    case 1:

                        place.setFavourite(0);
                        DatabaseReference child1 = query.getRef().child(String.valueOf(place.getGlobalID()));
                        child1.setValue(place, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                favouriteBtn.setImageResource(R.drawable.ic_032_star_empty);
                            }
                        });
                        break;
                }

            }
        });



        showLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
                parentFragment.dismiss();
                parentFragment.findPosition(place);

            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parentFragment = null;
        query = null;
    }
}
