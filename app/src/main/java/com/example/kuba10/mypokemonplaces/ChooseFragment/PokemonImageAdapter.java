package com.example.kuba10.mypokemonplaces.ChooseFragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kuba10.mypokemonplaces.FragmentListener;
import com.example.kuba10.mypokemonplaces.Model.Pokemon;
import com.example.kuba10.mypokemonplaces.Model.PokemonGo;
import com.example.kuba10.mypokemonplaces.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.util.List;


public class PokemonImageAdapter extends RecyclerView.Adapter<PokemonImageAdapter.ViewHolder> {


    private Context context;
    private List<PokemonGo> pokemonGo_data_list;
    private FragmentListener fragmentListener;
    ChooseFragment fragment;





    public PokemonImageAdapter(Context context, FragmentListener fragmentListener, ChooseFragment fragment) {
        this.context = context;
        this.fragment = (ChooseFragment) fragment;
        this.fragmentListener = fragmentListener;
        pokemonGo_data_list = fragmentListener.getPokemonList();

    }


    @Override
    public PokemonImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_image_card, parent, false);




        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(PokemonImageAdapter.ViewHolder holder, final int position) {


        ViewGroup.LayoutParams params = holder.imageContainer.getLayoutParams();

        params.height = getScreenWidth() / 4;
        params.width = getScreenWidth() / 4;
        holder.imageContainer.setLayoutParams(params);


        holder.loader.setVisibility(View.VISIBLE);
        holder.imageView.setVisibility(View.VISIBLE);
        final ProgressBar progressView = holder.loader;



            Picasso.with(context)
                    .load(pokemonGo_data_list.get(position).getImg())
                    .resize((getScreenWidth() / 4) - 20, (getScreenWidth() / 4) - 20)
                    .centerCrop()

                    .into(holder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });






        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentListener.sendDataToAddFragment(pokemonGo_data_list.get(position));
                fragment.dismiss();

            }
        });
    }




    public int getScreenWidth() {

        int columnWidth;

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) {

            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;

        return columnWidth;
    }

    @Override
    public int getItemCount() {
        return pokemonGo_data_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


//        public TextView name;
        public ImageView imageView;
        public ProgressBar loader;
        public RelativeLayout imageContainer;


        public ViewHolder(View itemView) {
            super(itemView);

//            name = (TextView) itemView.findViewById(R.id.choose_fragment_name);
            imageView = (ImageView) itemView.findViewById(R.id.choose_fragment_image);
            loader = (ProgressBar) itemView.findViewById(R.id.loader);
            imageContainer = (RelativeLayout) itemView.findViewById(R.id.image_container);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        context = null;
        fragmentListener = null;

    }
}