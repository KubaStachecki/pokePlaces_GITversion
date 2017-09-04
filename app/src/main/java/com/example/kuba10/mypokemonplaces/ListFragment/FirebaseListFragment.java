package com.example.kuba10.mypokemonplaces.ListFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kuba10.mypokemonplaces.ListFragmentAdapters.AdapterForTouchAndFirebase;
import com.example.kuba10.mypokemonplaces.ListFragmentAdapters.CardViewHolder;
import com.example.kuba10.mypokemonplaces.Utils.OnStartDragListener;
import com.example.kuba10.mypokemonplaces.Utils.SimpleItemTouchHelperCallback;
import com.example.kuba10.mypokemonplaces.Utils.Constants;
import com.example.kuba10.mypokemonplaces.Utils.FragmentListener;
import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.Model.PokemonGo;
import com.example.kuba10.mypokemonplaces.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirebaseListFragment extends Fragment implements OnStartDragListener {
    private AdapterForTouchAndFirebase firebaseAdapter;
    private ItemTouchHelper itemTouchHelper;
    private FragmentListener fragmentListener;
    private Query orderByChild;
    @BindView(R.id.listRecycler)
    RecyclerView recyclerView;

    public static FirebaseListFragment newInstance() {
        return new FirebaseListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof FragmentListener) {
            fragmentListener = (FragmentListener) getActivity();
        } else {
            throw new NullPointerException("Fragment Listener must be main activity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        generateQuery();
        setUpFirebaseAdapter();
        return view;
    }

    private void setUpFirebaseAdapter() {
        firebaseAdapter = new AdapterForTouchAndFirebase(PokePlace.class,
                R.layout.place_card_layout, CardViewHolder.class,
                orderByChild, this, this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(firebaseAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(firebaseAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void generateQuery() {
        orderByChild = FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.PLACES)
                .orderByChild(Constants.FIREBASE_QUERY_INDEX);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onPause() {
        super.onPause();
        firebaseAdapter.cleanup();
    }

    public void dismiss() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .remove(this).commit();
    }

    public void moveCameraToPlacePosition(PokePlace place) {
        fragmentListener.findPlaceFromList(place);
    }

    public Query getOrderByChildQuery() {
        return orderByChild;
    }

    public ArrayList<PokemonGo> sendPokemonListToAdapter() {
        return fragmentListener.getPokemonList();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
        orderByChild = null;
        itemTouchHelper = null;
        firebaseAdapter = null;
    }
}

