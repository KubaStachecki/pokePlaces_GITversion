package com.example.kuba10.mypokemonplaces.Adapters;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuba10.mypokemonplaces.Model.PokePlace;
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

    private DatabaseReference mRef;
    private ChildEventListener mChildEventListener;
    private ArrayList<PokePlace> places;
    Context context;

    private OnStartDragListener mOnStartDragListener;

    public AdapterForTouchAndFirebase(Class<PokePlace> modelClass, int modelLayout,
                                      Class<CardViewHolder> viewHolderClass,
                                      Query ref, OnStartDragListener onStartDragListener, Context context, DatabaseReference datRef) {


        super(modelClass, modelLayout, viewHolderClass, ref);
        mRef = datRef;
        mOnStartDragListener = onStartDragListener;
        this.context = context;
        places = new ArrayList<>();

        mChildEventListener = mRef.addChildEventListener(new ChildEventListener() {

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

        setIndexInFirebase();

        return false;
    }


    @Override
    public void onItemDismiss(int position) {

        places.remove(position);
        getRef(position).removeValue();

    }

    @Override
    protected void populateViewHolder(final CardViewHolder viewHolder, PokePlace place, int position) {

        viewHolder.bindPokePlace(place);


        viewHolder.image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(viewHolder);
                } else if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_UP)
                {
                    setIndexInFirebase();
                    Log.d("touh", "onTouch: up - setindex");

                }

                return false;
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
//        setIndexInFirebase();

        mRef.removeEventListener(mChildEventListener);
    }


    private void setIndexInFirebase() {

        Log.d("touh", "setindex - !!!!");

        if (places.size() != 0) {
            for (PokePlace place : places) {
                int index = places.indexOf(place);
                Log.d("KEY : ", "" + getRef(index).getKey().toString());
                DatabaseReference ref = getRef(index);
                place.setListPosition(Integer.toString(index));
                ref.setValue(place);

            }

        }


    }
}
