package com.example.kuba10.mypokemonplaces.Adapters;

import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.kuba10.mypokemonplaces.Constants;
import com.example.kuba10.mypokemonplaces.ListFragment.FirebaseListFragment;
import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Kuba10 on 20.08.2017.
 */

public class AdapterForTouchAndFirebase extends FirebaseRecyclerAdapter<PokePlace, CardViewHolder> implements ItemTouchHelperAdapter {

    private DatabaseReference placesReference;
    private ChildEventListener mChildEventListener;
    private ArrayList<PokePlace> places;
    private FirebaseListFragment parentFragment;
    private OnStartDragListener mOnStartDragListener;

    public AdapterForTouchAndFirebase(Class<PokePlace> modelClass, int modelLayout,
                                      Class<CardViewHolder> viewHolderClass,
                                      Query ref, OnStartDragListener onStartDragListener, FirebaseListFragment fragment) {


        super(modelClass, modelLayout, viewHolderClass, ref);
        mOnStartDragListener = onStartDragListener;
        places = new ArrayList<>();
        parentFragment = fragment;

        placesReference = FirebaseDatabase.getInstance().getReference().child(Constants.PLACES);

        mChildEventListener = placesReference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                places.add(dataSnapshot.getValue(PokePlace.class));
                Log.d("CHILD ADD LIST SIZE : ", "" + places.size());
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
    public boolean onItemMove(int fromPosition, int toPosition) {

        Collections.swap(places, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
//        setIndexInFirebase();

        return false;
    }


    @Override
    public void onItemDismiss(int position) {

        places.remove(position);
        getRef(position).removeValue();

    }

    @Override
    protected void populateViewHolder(final CardViewHolder viewHolder, final PokePlace place, final int position) {

        viewHolder.bindPokePlace(place);

        viewHolder.favouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (place.getFavourite()) {

                    case 0:

                        place.setFavourite(1);
                        getRef(position).setValue(place);
                        viewHolder.favouriteBtn.setImageResource(R.drawable.ic_032_star);
                        break;


                    case 1:

                        place.setFavourite(0);
                        getRef(position).setValue(place);
                        viewHolder.favouriteBtn.setImageResource(R.drawable.ic_032_star_empty);
                        break;


                }


            }
        });


        viewHolder.image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(viewHolder);
                } else if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_UP) {
                    Log.d("touh", "onTouch: up - setindex");

                }

                return false;
            }
        });


        viewHolder.placeCardView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {

                parentFragment.dismiss();
                parentFragment.findPosition(place);


            }
        });


    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void cleanup() {
        super.cleanup();

        setIndexInFirebase();

        placesReference.removeEventListener(mChildEventListener);
    }


    private void setIndexInFirebase() {

        Log.d("touh", "setindex - !!!!");

        for (int i = 0; i < places.size(); i++) {

            PokePlace place = places.get(i);

            place.setListPosition(Integer.toString(i));

            DatabaseReference child = placesReference.child(String.valueOf(place.getGlobalID()));

            child.setValue(place);

//            DatabaseReference ref = getRef(i);
//
//            ref.setValue(place);

        }


//        if (places.size() != 0) {
//            for (PokePlace place : places) {
//                int index = places.indexOf(place);
//                Log.d("KEY : ", "" + getRef(index).getKey().toString());
//                DatabaseReference ref = getRef(index);
//                place.setListPosition(Integer.toString(index));
//                ref.setValue(place);
//
//            }

    }


}

