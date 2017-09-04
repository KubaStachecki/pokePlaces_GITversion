package com.example.kuba10.mypokemonplaces.ListFragmentAdapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.R;

public class CardViewHolder extends RecyclerView.ViewHolder {
    public TextView title, description;
    public ImageView image, dragHandle, showLocation;
    public CardView placeCardView;
    public ImageButton favouriteBtn;
    private View view;

    public CardViewHolder(View view) {
        super(view);
        this.view = view;
    }

    public void bindPokePlace(PokePlace place) {
        title = view.findViewById(R.id.title_text);
        description = view.findViewById(R.id.description_text);
        image = view.findViewById(R.id.card_image);
        dragHandle = view.findViewById(R.id.drag_handle);
        showLocation = view.findViewById(R.id.show_on_map_button);
        favouriteBtn = view.findViewById(R.id.favourite_button);
        placeCardView = view.findViewById(R.id.placeCardView);
        dragHandle.setImageResource(R.drawable.ic_drag_handle_black_24dp);
        showLocation.setImageResource(R.drawable.ic_021_pointer);
        title.setText(place.getTitle());
        description.setText(place.getDesctription());
        favouriteBtn.setImageResource(place.isFavourite() ? R.drawable.ic_032_star : R.drawable.ic_032_star_empty);
        }
    }


