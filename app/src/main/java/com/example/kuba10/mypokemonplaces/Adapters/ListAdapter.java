package com.example.kuba10.mypokemonplaces.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kuba10.mypokemonplaces.Model.PokePlace;


import java.util.ArrayList;
import java.util.List;
import com.example.kuba10.mypokemonplaces.R;


/**
 * Created by cordy on 2017-08-16.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    private List<PokePlace> placeList;
    private Context context;

    public ListAdapter(ArrayList<PokePlace> placeList) {

        this.placeList = placeList;


    }


    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.place_card_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder viewHolder, final  int i) {


        viewHolder.title.setText(placeList.get(i).getTitle());
        viewHolder.description.setText(placeList.get(i).getDesctription());

        viewHolder.image.setImageResource(R.drawable.ic_android_black_24dp);


//
//        Picasso.with(context)
//                .load(staffList.get(i).getObrazek())
//                .resize(500, 500)
//                .centerCrop()
//                .into(viewHolder.portret);


    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title, description;
        private ImageView image;


        public ViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title_text);
            description = (TextView) view.findViewById(R.id.description_text);
            image = (ImageView) view.findViewById(R.id.card_image);


        }

    }}







