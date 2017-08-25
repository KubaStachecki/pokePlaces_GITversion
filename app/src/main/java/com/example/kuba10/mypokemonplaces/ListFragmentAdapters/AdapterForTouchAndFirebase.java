package com.example.kuba10.mypokemonplaces.ListFragmentAdapters;

import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.kuba10.mypokemonplaces.Constants;
import com.example.kuba10.mypokemonplaces.ListFragment.FirebaseListFragment;
import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.Model.PokemonGo;
import com.example.kuba10.mypokemonplaces.PlaceDetailsFragment.PlaceDetailsFragment;
import com.example.kuba10.mypokemonplaces.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Kuba10 on 20.08.2017.
 */

public class AdapterForTouchAndFirebase extends FirebaseRecyclerAdapter<PokePlace, CardViewHolder> implements ItemTouchHelperAdapter {

    private final Query ref;
    private ChildEventListener mChildEventListener;
    private ArrayList<PokePlace> userPlacesDataList;
    private ArrayList<PokemonGo> pokemonGo_data_list;
    private FirebaseListFragment parentFragment;
    private OnStartDragListener mOnStartDragListener;

    public AdapterForTouchAndFirebase(Class<PokePlace> modelClass, int modelLayout,
                                      Class<CardViewHolder> viewHolderClass,
                                      Query ref, OnStartDragListener onStartDragListener, FirebaseListFragment fragment) {


        super(modelClass, modelLayout, viewHolderClass, ref);
        this.ref = ref;

        mOnStartDragListener = onStartDragListener;
        userPlacesDataList = new ArrayList<>();
        parentFragment = fragment;
        pokemonGo_data_list = parentFragment.sendPokemonListToAdapter();

        mChildEventListener = ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userPlacesDataList.add(dataSnapshot.getValue(PokePlace.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(userPlacesDataList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }


    @Override
    public void onItemDismiss(int position) {
        PokePlace place = userPlacesDataList.get(position);
        DatabaseReference databasePlace = ref.getRef().child(String.valueOf(place.getGlobalID()));
        databasePlace.removeValue();
        userPlacesDataList.remove(position);
    }

    @Override
    protected void populateViewHolder(final CardViewHolder viewHolder, final PokePlace place, final int position) {

        viewHolder.bindPokePlace(place);
        viewHolder.dragHandle.setLongClickable(true);



        viewHolder.dragHandle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN: {

//                        mOnStartDragListener.onStartDrag(viewHolder);

                        Log.d("TAG", "onTouch: FINGER DOWN");
                        return true;

                    }

                    case MotionEvent.ACTION_UP: {

                        Log.d("TAG", "onTouch: FINGER UP");

                        return false;
                    }

                    case MotionEvent.ACTION_CANCEL:
                    {

                        Log.d("TAG", "onTouch: CANCEL ");

                        return true;
                    }

                }
return true;
            }

        });


        viewHolder.showLocation.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                parentFragment.dismiss();
                parentFragment.findPosition(place);
            }
        });

        viewHolder.placeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIndexInFirebase();
                showDetails(place);

            }
        });

        if (pokemonGo_data_list.size() > 0) {

            if (place.getPokemonId() == -777) {
                viewHolder.image.setImageResource(R.drawable.ic_034_pikachu_1);
            } else {
                setPokemonImage(place, viewHolder.image);
            }
        } else {
            viewHolder.image.setImageResource(R.drawable.sad_pika);
        }
    }

    private void setPokemonImage(PokePlace place, ImageView image) {
        Picasso.with(parentFragment.getContext())
                .load(pokemonGo_data_list.get(place.getPokemonId()).getImg())
                .into(image);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        setIndexInFirebase();
        ref.removeEventListener(mChildEventListener);
    }


    private void setIndexInFirebase() {
        for (int i = 0; i < userPlacesDataList.size(); i++) {
            PokePlace place = userPlacesDataList.get(i);
            DatabaseReference child = ref.getRef().child(String.valueOf(place.getGlobalID()));
            child.child(Constants.FIREBASE_QUERY_INDEX).setValue(Integer.toString(i));
        }
    }

    public void showDetails(PokePlace place) {


        PlaceDetailsFragment details = PlaceDetailsFragment.newInstance(place);
        details.show(parentFragment.getFragmentManager(), "");


    }


}

