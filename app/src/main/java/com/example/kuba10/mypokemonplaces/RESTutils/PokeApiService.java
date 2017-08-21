package com.example.kuba10.mypokemonplaces.RESTutils;

import com.example.kuba10.mypokemonplaces.Model.PokePlace;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Kuba10 on 21.08.2017.
 */


public interface PokeApiService {
    @GET("users/{user}/repos")
    Call<List<PokePlace>> listRepos(@Path("user") String user);
}