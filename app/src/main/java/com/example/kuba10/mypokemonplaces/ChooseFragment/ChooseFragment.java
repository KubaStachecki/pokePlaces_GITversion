package com.example.kuba10.mypokemonplaces.ChooseFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.kuba10.mypokemonplaces.FragmentListener;
import com.example.kuba10.mypokemonplaces.Model.PokemonGo;
import com.example.kuba10.mypokemonplaces.R;

import java.util.ArrayList;

import butterknife.BindView;


public class ChooseFragment extends Fragment {


    public static final String LIST = "list";
    private PokemonImageAdapter pokeAdapter;
    private GridLayoutManager gridManager;
    private FragmentListener fragmentListener;
    private ArrayList<PokemonGo> pokemonGoDataList;

    @BindView(R.id.error_image_gallery)
    LinearLayout errorImage;

    @BindView(R.id.choose_recycler_view)
    RecyclerView recyclerView;

    public static ChooseFragment newInstance(ArrayList<PokemonGo> pokemonGo_data_list) {
        ChooseFragment fragment = new ChooseFragment();
        Bundle args = new Bundle();
        args.putSerializable(LIST, pokemonGo_data_list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (pokemonGoDataList.isEmpty()) {
            errorImage.setVisibility(View.VISIBLE);
        }
        if (getContext() instanceof FragmentListener){fragmentListener = (FragmentListener) getContext();}
        pokemonGoDataList = (ArrayList<PokemonGo>) getArguments().getSerializable(LIST);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose, container, false);

        makeRecycleViewAdapter();
        errorImage.setVisibility(View.GONE);

        return view;

    }

    private void makeRecycleViewAdapter() {
        gridManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(gridManager);
        pokeAdapter = new PokemonImageAdapter(getActivity(), fragmentListener, this);
        recyclerView.setAdapter(pokeAdapter);
        pokeAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    public void closeFragment() {
        fragmentListener.closeFragment(this);
    }

}
