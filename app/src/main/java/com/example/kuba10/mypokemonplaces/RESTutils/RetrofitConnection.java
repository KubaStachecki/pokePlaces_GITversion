package com.example.kuba10.mypokemonplaces.RESTutils;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.kuba10.mypokemonplaces.Model.Pokemon;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitConnection {

    private Retrofit retrofit;
    private PokeApiService pokeRetrofit;
    List<Pokemon> pokemonList;



    public void downloadPokemonList() {


     pokemonList = new ArrayList<>();


        retrofit = new Retrofit.Builder()
                .baseUrl("ownstar.pl/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        pokeRetrofit = retrofit.create(PokeApiService.class);

        pokeRetrofit.listPokemons().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PokemonGo>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Pokemon pokemon) {
                        pokemonList.add(pokemon);
                        Log.e("NEXT RETROFIT", "" + pokemonList.size());

                        Log.e("Pokemon", "" +pokemon.getName());
                        Log.e("Pokemon", "" +pokemon.getImageUrl());
                        Log.e("Pokemon", "" +pokemon.getWeight());
                        Log.e("Pokemon", "" +pokemon.getId());




                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.e("ERROR RETROFIT", "" + e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        Log.d("COMPLETE RETROFIT", "lista z retro sciagneita : " + pokemonList.size());

                    }
                });
    }


    public List<Pokemon> getPokemonList() {

        return pokemonList;
    }



}




