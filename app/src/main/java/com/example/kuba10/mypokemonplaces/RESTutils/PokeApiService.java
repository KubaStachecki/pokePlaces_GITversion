package com.example.kuba10.mypokemonplaces.RESTutils;

import com.example.kuba10.mypokemonplaces.Model.PokemonGo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;



public interface PokeApiService {
    @GET("pokedex2.json")
    Observable <List<PokemonGo>> listPokemons();
}