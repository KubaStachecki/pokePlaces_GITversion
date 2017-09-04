package com.example.kuba10.mypokemonplaces.ChooseFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.kuba10.mypokemonplaces.Utils.FragmentListener;
import com.example.kuba10.mypokemonplaces.Model.PokemonGo;
import com.example.kuba10.mypokemonplaces.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseFragment extends Fragment {
    public static final String LIST = "list";
    private PokemonImageAdapter pokeAdapter;
    private GridLayoutManager gridManager;
    private FragmentListener fragmentListener;
    private ArrayList<PokemonGo> pokemonGoDataList;
    public View.OnClickListener imageClickListener;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pokemonGoDataList = (ArrayList<PokemonGo>) getArguments().getSerializable(LIST);
        if (getActivity() instanceof FragmentListener) {
            fragmentListener = (FragmentListener) getActivity();
        } else {
            throw new NullPointerException("Fragment Listener must be main activity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose, container, false);
        ButterKnife.bind(this, view);
        if (!pokemonGoDataList.isEmpty()) {
            errorImage.setVisibility(View.GONE);
        }
        makeRecycleViewAdapter();
        return view;
    }

    private void makeRecycleViewAdapter() {
        gridManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridManager);
        pokeAdapter = new PokemonImageAdapter(this);
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

    public void sendSelectedDataToAddFragment(int position) {
        fragmentListener.sendDataToAddFragment(pokemonGoDataList.get(position));
    }

    public ArrayList<PokemonGo> sendListToAdapter() {
        return pokemonGoDataList;
    }
}
