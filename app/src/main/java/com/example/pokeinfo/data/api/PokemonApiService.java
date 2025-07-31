package com.example.pokeinfo.data.api;

import com.example.pokeinfo.data.model.Pokemon;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokemonApiService {
    @GET("pokemon")
    Call<PokemonListResponse> getPokemonList(@Query("limit") int limit, @Query("offset") int offset);

    @GET("pokemon/{name}")
    Call<Pokemon> getPokemonDetails(@Path("name") String name);
}
