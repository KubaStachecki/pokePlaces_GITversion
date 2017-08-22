package com.example.kuba10.mypokemonplaces.ListFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kuba10.mypokemonplaces.Adapters.AdapterForTouchAndFirebase;
import com.example.kuba10.mypokemonplaces.Adapters.CardViewHolder;
import com.example.kuba10.mypokemonplaces.Adapters.OnStartDragListener;
import com.example.kuba10.mypokemonplaces.Adapters.SimpleItemTouchHelperCallback;
import com.example.kuba10.mypokemonplaces.Constants;
import com.example.kuba10.mypokemonplaces.FragmentListener;
import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FirebaseListFragment extends Fragment implements OnStartDragListener {

    private static final String TAG = FirebaseListFragment.class.getSimpleName();

    DatabaseReference databaseReference;
    private AdapterForTouchAndFirebase mFirebaseAdapter;
    private ItemTouchHelper mItemTouchHelper;

    Context context;
    private FragmentListener fragmentListener;


    @BindView(R.id.listRecycler)
    RecyclerView recyclerView;

    private Query orderByChild;


    public static FirebaseListFragment newInstance() {
        return new FirebaseListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        fragmentListener = (FragmentListener) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);


        //  Use if un-sorted list is required, and change "query" in mFirebaseAdapter
//        prepareDatabaseInstance();

        prepareQuery();
        setUpFirebaseAdapter();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void setUpFirebaseAdapter() {

        mFirebaseAdapter = new AdapterForTouchAndFirebase(PokePlace.class,
                R.layout.place_card_layout, CardViewHolder.class,
                orderByChild, this, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mFirebaseAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);


    }

    private void prepareQuery() {
        orderByChild = FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.PLACES)
                .orderByChild(Constants.FIREBASE_QUERY_INDEX);
    }

    private void prepareDatabaseInstance() {
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.PLACES);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


    @Override
    public void onDetach() {
        super.onDetach();

        context = null;

    }

    @Override
    public void onPause() {

        super.onPause();
        cleanup();


    }

    public void cleanup() {

        mFirebaseAdapter.cleanup();

    }

    public void dismiss() {

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)

                .remove(this).commit();
    }

    public void findPosition(PokePlace place) {

        fragmentListener.findPlaceFromList(place);


    }


}
