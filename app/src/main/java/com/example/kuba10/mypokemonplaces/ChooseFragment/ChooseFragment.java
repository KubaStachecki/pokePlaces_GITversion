package com.example.kuba10.mypokemonplaces.ChooseFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.kuba10.mypokemonplaces.AddPlaceFragment.AddPlaceFragment;
import com.example.kuba10.mypokemonplaces.FragmentListener;
import com.example.kuba10.mypokemonplaces.Model.Pokemon;
import com.example.kuba10.mypokemonplaces.Model.PokemonGo;
import com.example.kuba10.mypokemonplaces.R;
import com.example.kuba10.mypokemonplaces.RESTutils.RetrofitConnection;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class ChooseFragment extends Fragment {


    private PokemonImageAdapter pokeAdapter;
    private GridLayoutManager gridManager;
    private RecyclerView recyclerView;
    private LinearLayout errorImage;
    private FragmentListener fragmentListener;

   private  ArrayList<PokemonGo> pokemonGo_data_list;


    public ChooseFragment() {
        // Required empty public constructor
    }

    public static ChooseFragment newInstance(ArrayList<PokemonGo> pokemonGo_data_list) {
        ChooseFragment fragment = new ChooseFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(AddPlaceFragment.LIST, pokemonGo_data_list);
        fragment.setArguments(args);
        return fragment;



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        fragmentListener = (FragmentListener) context;
        pokemonGo_data_list = getArguments().getParcelableArrayList(AddPlaceFragment.LIST);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (pokemonGo_data_list.size() == 0) {
            errorImage.setVisibility(View.VISIBLE);


        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choose, container, false);
        errorImage = (LinearLayout) view.findViewById(R.id.error_image_gallery);


        recyclerView = (RecyclerView) view.findViewById(R.id.choose_recycler_view);
        gridManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(gridManager);


        pokeAdapter = new PokemonImageAdapter(getActivity(), pokemonGo_data_list, fragmentListener, this);
        recyclerView.setAdapter(pokeAdapter);

        pokeAdapter.notifyDataSetChanged();
        errorImage.setVisibility(View.GONE);

        return view;

    }


    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    public void dismiss() {
        fragmentListener.dismiss(this);
    }

}
