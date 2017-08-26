package com.example.kuba10.mypokemonplaces.RESTutils;

import android.util.Log;

import com.example.kuba10.mypokemonplaces.FragmentListener;
import com.example.kuba10.mypokemonplaces.Model.PokemonGo;
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
    ArrayList<PokemonGo> pokemonList;



    public void downloadPokemonList() {

        pokemonList = new ArrayList<>();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://www.ownstar.pl/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        pokeRetrofit = retrofit.create(PokeApiService.class);

        pokeRetrofit.listPokemons().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<PokemonGo>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull List<PokemonGo> pokemon) {
                        pokemonList.addAll(pokemon);

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.e("ERROR RETROFIT", "" + e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                        for (PokemonGo pokemon : pokemonList) {
                            Log.d("Pokemon", "" + pokemon.getName());
                            Log.d("Pokemon", "" + pokemon.getWeight());
                            Log.d("Pokemon", "" + pokemon.getId());
                            Log.d("Pokemon", "" + pokemon.getImg());
                            Log.d("Pokemon", "" + pokemon.getAvgSpawns());
                        }
                        Log.d("POKEMON LIST FULL", "I just like to watch that log...");


                    }


                });


    }


    public ArrayList<PokemonGo> getPokemonList() {

        return pokemonList;
    }


}




