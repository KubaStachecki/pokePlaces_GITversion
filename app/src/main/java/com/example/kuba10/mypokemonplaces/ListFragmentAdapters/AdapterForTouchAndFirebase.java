package com.example.kuba10.mypokemonplaces.ListFragmentAdapters;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.kuba10.mypokemonplaces.Utils.Constants;
import com.example.kuba10.mypokemonplaces.ListFragment.FirebaseListFragment;
import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.Model.PokemonGo;
import com.example.kuba10.mypokemonplaces.PlaceDetailsFragment.PlaceDetailsFragment;
import com.example.kuba10.mypokemonplaces.R;
import com.example.kuba10.mypokemonplaces.Utils.OnStartDragListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class AdapterForTouchAndFirebase extends FirebaseRecyclerAdapter<PokePlace, CardViewHolder> implements ItemTouchHelperAdapter {
    private final Query ref;
    private ChildEventListener mChildEventListener;
    private ArrayList<PokePlace> userPlacesDataList;
    private ArrayList<PokemonGo> pokemonGoDataList;
    private FirebaseListFragment parentFragment;
    private final OnStartDragListener onStartDragListener;

    public AdapterForTouchAndFirebase(Class<PokePlace> modelClass, int modelLayout,
                                      Class<CardViewHolder> viewHolderClass,
                                      Query ref, OnStartDragListener onStartDragListener, FirebaseListFragment parentFragment) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.ref = ref;
        this.onStartDragListener = onStartDragListener;
        this.parentFragment = parentFragment;
        userPlacesDataList = new ArrayList<>();
        pokemonGoDataList = parentFragment.sendPokemonListToAdapter();
        setDatabaseListener(ref);
    }

    private void setDatabaseListener(Query ref) {
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
        notifyDataSetChanged();
    }

    @Override
    protected void populateViewHolder(final CardViewHolder viewHolder, final PokePlace place, int position) {
        viewHolder.bindPokePlace(place);
        viewHolder.dragHandle.setLongClickable(true);
        viewHolder.dragHandle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        onStartDragListener.onStartDrag(viewHolder);
                        return false;
                    }
                }
                return false;
            }
        });
        viewHolder.showLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentFragment.dismiss();
                parentFragment.moveCameraToPlacePosition(place);
            }
        });
        viewHolder.placeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetails(place);
                setIndexInFirebase();
            }
        });
        setCardImage(viewHolder, place);
    }

    private void setCardImage(CardViewHolder viewHolder, PokePlace place) {
        if (pokemonGoDataList.size() >= 0) {
            if (place.getPokemonId() == Constants.EMPTY_POKEMON_ID) {
                viewHolder.image.setImageResource(R.drawable.ic_034_pikachu_1);
            } else {
                setPokemonImage(place, viewHolder.image);
            }
        } else {
            viewHolder.image.setImageResource(R.drawable.sad_pika);
        }
    }

    private void setPokemonImage(PokePlace place, final ImageView image) {
        Picasso.with(parentFragment.getContext())
                .load(pokemonGoDataList.get(150).getImg())
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        image.setImageResource(R.drawable.sad_pika);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public long getItemId(int position) {
        return userPlacesDataList.get(position).getGlobalID();
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

    private void showDetails(PokePlace place) {
        PlaceDetailsFragment details = PlaceDetailsFragment.newInstance(place);
        details.show(parentFragment.getFragmentManager(), "");
    }

    @Override
    public void clearView() {
        setIndexInFirebase();
    }
}

