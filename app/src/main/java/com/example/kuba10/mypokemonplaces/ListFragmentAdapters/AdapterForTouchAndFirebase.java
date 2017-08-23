package com.example.kuba10.mypokemonplaces.ListFragmentAdapters;

import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;

import com.example.kuba10.mypokemonplaces.ListFragment.FirebaseListFragment;
import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.PlaceDetailsFragment.PlaceDetailsFragment;
import com.example.kuba10.mypokemonplaces.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Kuba10 on 20.08.2017.
 */

public class AdapterForTouchAndFirebase extends FirebaseRecyclerAdapter<PokePlace, CardViewHolder> implements ItemTouchHelperAdapter {

    private final Query ref;
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


        this.ref = ref;
        mChildEventListener = this.ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                places.add(dataSnapshot.getValue(PokePlace.class));
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
        return false;
    }


    @Override
    public void onItemDismiss(final int position) {

        PokePlace place = places.get(position);
        DatabaseReference child = ref.getRef().child(String.valueOf(place.getGlobalID()));
        child.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                places.remove(position);

            }
        });

    }

    @Override
    protected void populateViewHolder(final CardViewHolder viewHolder, final PokePlace place, final int position) {

        viewHolder.bindPokePlace(place);
        viewHolder.favouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (place.getFavourite()) {

                    case 0:

                        PokePlace place0 = places.get(position);
                        place0.setFavourite(1);
//                        place0.setListPosition(Integer.toString(position));
                        DatabaseReference child = ref.getRef().child(String.valueOf(place0.getGlobalID()));

                        child.setValue(place0, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                viewHolder.favouriteBtn.setImageResource(R.drawable.ic_032_star);

                            }
                        });
                        break;

                    case 1:

                        PokePlace place1 = places.get(position);
                        place1.setFavourite(0);
//                        place2.setListPosition(Integer.toString(position));
                        DatabaseReference child1 = ref.getRef().child(String.valueOf(place1.getGlobalID()));

                        child1.setValue(place1, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                viewHolder.favouriteBtn.setImageResource(R.drawable.ic_032_star_empty);

                            }
                        });
                        break;
                }

            }
        });


        viewHolder.dragHandle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(viewHolder);
                }
                return false;
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
                showDetails(place);

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
        ref.removeEventListener(mChildEventListener);
    }


    private void setIndexInFirebase() {
        for (int i = 0; i < places.size(); i++) {
            PokePlace place = places.get(i);
            place.setListPosition(Integer.toString(i));
            DatabaseReference child = ref.getRef().child(String.valueOf(place.getGlobalID()));
            child.setValue(place);
        }
    }

    public void showDetails(PokePlace place) {


        PlaceDetailsFragment details = PlaceDetailsFragment.newInstance(place);
        details.show(parentFragment.getFragmentManager(), "");


    }

    public void changeFavourite(PokePlace place, int position, CardViewHolder viewHolder) {


        switch (place.getFavourite()) {

            case 0:

                PokePlace place0 = places.get(position);
                place0.setFavourite(1);
                place0.setListPosition(Integer.toString(position));
                DatabaseReference child = ref.getRef().child(String.valueOf(place0.getGlobalID()));
                child.setValue(place0);
                viewHolder.favouriteBtn.setImageResource(R.drawable.ic_032_star);
                break;

            case 1:

                PokePlace place2 = places.get(position);
                place2.setFavourite(0);
                place.setListPosition(Integer.toString(position));
                DatabaseReference child2 = ref.getRef().child(String.valueOf(place2.getGlobalID()));
                child2.setValue(place2);
                viewHolder.favouriteBtn.setImageResource(R.drawable.ic_032_star_empty);
                break;
        }

    }

}

