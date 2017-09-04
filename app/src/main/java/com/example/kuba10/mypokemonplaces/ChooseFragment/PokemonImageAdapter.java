package com.example.kuba10.mypokemonplaces.ChooseFragment;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.kuba10.mypokemonplaces.Model.PokemonGo;
import com.example.kuba10.mypokemonplaces.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PokemonImageAdapter extends RecyclerView.Adapter<PokemonImageAdapter.ViewHolder> {
    private ChooseFragment fragment;
    private List<PokemonGo> pokemonGoDataList;

    public PokemonImageAdapter(ChooseFragment fragment) {
        this.fragment = fragment;
        pokemonGoDataList = fragment.sendListToAdapter();
    }

    @Override
    public PokemonImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_image_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PokemonImageAdapter.ViewHolder holder, int position) {
        ViewGroup.LayoutParams params = holder.imageContainer.getLayoutParams();
        params.height = getScreenWidth() / 4;
        params.width = getScreenWidth() / 4;
        holder.imageContainer.setLayoutParams(params);
        holder.loader.setVisibility(View.VISIBLE);
        holder.imageView.setVisibility(View.VISIBLE);
        final ProgressBar progressView = holder.loader;
        PokemonGo pokemon = pokemonGoDataList.get(position);
        inflatePokemonImage(holder, progressView, pokemon);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.sendSelectedDataToAddFragment(holder.getAdapterPosition());
                fragment.closeFragment();
            }
        });
    }

    private void inflatePokemonImage(final ViewHolder holder, final ProgressBar progressView, PokemonGo pokemon) {
        Picasso.with(holder.imageView.getContext()).load(pokemon.getImg())
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.imageView.setImageResource(R.drawable.sad_pika);
                    }
                });
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) fragment.getContext()
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
        return pokemonGoDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ProgressBar loader;
        public RelativeLayout imageContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.choose_fragment_image);
            loader = itemView.findViewById(R.id.loader);
            imageContainer = itemView.findViewById(R.id.image_container);
        }
    }
}