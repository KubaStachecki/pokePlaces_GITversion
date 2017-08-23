package com.example.kuba10.mypokemonplaces.RESTutils;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.kuba10.mypokemonplaces.Model.Pokemon;
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
    List<PokemonGo> pokemonList;


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
                        Log.e("NEXT RETROFIT", "" + pokemonList.size());

//                        Log.e("Pokemon", "" +pokemon.getName());
//                        Log.e("Pokemon", "" +pokemon.getWeight());
//                        Log.e("Pokemon", "" +pokemon.getId());
//                        Log.e("Pokemon", "" +pokemon.getImg());
//                        Log.e("Pokemon", "" +pokemon.getAvgSpawns());


                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.e("ERROR RETROFIT", "" + e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        Log.d("COMPLETE RETROFIT", "lista z retro sciagneita : " + pokemonList.size());

                        for (PokemonGo pokemon : pokemonList) {

                            Log.e("Pokemon", "" + pokemon.getName());
                            Log.e("Pokemon", "" + pokemon.getWeight());
                            Log.e("Pokemon", "" + pokemon.getId());
                            Log.e("Pokemon", "" + pokemon.getImg());
                            Log.e("Pokemon", "" + pokemon.getAvgSpawns());}

                    }
                });




        }





    public List<PokemonGo> getPokemonList() {

        return pokemonList;
    }


}




