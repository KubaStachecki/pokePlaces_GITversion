package com.example.kuba10.mypokemonplaces.ChooseFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.kuba10.mypokemonplaces.Adapters.PokemonImageAdapter;
import com.example.kuba10.mypokemonplaces.AddPlaceFragment.AddPlaceFragment;
import com.example.kuba10.mypokemonplaces.Model.Pokemon;
import com.example.kuba10.mypokemonplaces.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class ChooseFragment extends Fragment {


    private PokemonImageAdapter pokeAdapter;
    private GridLayoutManager gridManager;
    private RecyclerView recyclerView;
    private FrameLayout frameLayout;
    private RelativeLayout imageContainer;
    private ImageView errorImage;


    ArrayList<Pokemon> imageList;


    public ChooseFragment() {
        // Required empty public constructor
    }

    public static ChooseFragment newInstance() {
        ChooseFragment fragment = new ChooseFragment();

        return fragment;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View view =   inflater.inflate(R.layout.fragment_choose, container, false);
        errorImage = (ImageView) view.findViewById(R.id.error_image_gallery);



        frameLayout = (FrameLayout) view.findViewById(R.id.container);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        gridManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridManager);


        imageList = getArguments().getParcelableArrayList("imagelist");

        wallAdapter = new WallAdapter(getActivity(), imageList);
        recyclerView.setAdapter(wallAdapter);

        wallAdapter.notifyDataSetChanged();
        errorImage.setVisibility(View.GONE);

        return view;

    }


    @Override
    public void onStart() {
        super.onStart();

        mainActivity.requestPermission();

        if (imageList.size() == 0){
            errorImage.setVisibility(View.VISIBLE);


        }
    }
}
