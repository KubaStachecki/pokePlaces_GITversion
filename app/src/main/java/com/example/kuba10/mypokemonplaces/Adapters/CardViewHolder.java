package com.example.kuba10.mypokemonplaces.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.R;

/**
 * Created by Kuba10 on 20.08.2017.
 */

public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView title, description;
    public ImageView image;
    public CardView placeCardView;
    public ImageButton favouriteBtn;

    View view;
    Context mContext;




    public CardViewHolder(View view) {
        super(view);
        this.view = view;

        mContext = view.getContext();

    }

    public void bindPokePlace(PokePlace place){


        title = (TextView) view.findViewById(R.id.title_text);
        description = (TextView) view.findViewById(R.id.description_text);
        image = (ImageView) view.findViewById(R.id.card_image);
        favouriteBtn = (ImageButton) view.findViewById(R.id.favourite_button);
        placeCardView = (CardView) view.findViewById(R.id.placeCardView);

        title.setText(place.getTitle());
        description.setText(place.getDesctription());

        image.setImageResource(R.drawable.ic_034_pikachu_1);
    }

    @Override
    public void onClick(View view) {

    }


}

