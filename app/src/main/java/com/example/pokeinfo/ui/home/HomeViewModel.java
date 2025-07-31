package com.example.pokeinfo.ui.home;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.pokeinfo.data.model.Pokemon;
import com.example.pokeinfo.data.db.AppDatabase;
import com.example.pokeinfo.data.api.PokemonApiService;
import com.example.pokeinfo.data.api.PokemonListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class HomeViewModel extends AndroidViewModel {
    private static final String TAG = "HomeViewModel";
    private final AppDatabase database;
    private final PokemonApiService apiService;
    private final MutableLiveData<List<Pokemon>> pokemons = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public HomeViewModel(Application application) {
        super(application);
        database = AppDatabase.getInstance(application);

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build();

        apiService = retrofit.create(PokemonApiService.class);
        loadInitialPokemonList();
    }

    private void loadInitialPokemonList() {
        isLoading.setValue(true);
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Pokemon> cachedPokemons = database.pokemonDao().getAllPokemonsSync();
            if (cachedPokemons != null && !cachedPokemons.isEmpty()) {
                pokemons.postValue(cachedPokemons);
                isLoading.postValue(false);
            } else {
                fetchFromApi(20, 0);
            }
        });
    }

    public void searchPokemons(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadInitialPokemonList();
            return;
        }

        isLoading.setValue(true);

        // First search in local database
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Pokemon> localResults = database.pokemonDao().searchPokemonsSync("%" + query + "%");
            pokemons.postValue(localResults);

            // If no local results, search in API
            if (localResults == null || localResults.isEmpty()) {
                searchInApi(query);
            } else {
                isLoading.postValue(false);
            }
        });
    }

    private void searchInApi(String query) {
        apiService.getPokemonDetails(query.toLowerCase()).enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Pokemon pokemon = response.body();
                    Executors.newSingleThreadExecutor().execute(() -> {
                        database.pokemonDao().insertAll(java.util.Collections.singletonList(pokemon));
                        pokemons.postValue(java.util.Collections.singletonList(pokemon));
                    });
                }
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Log.e(TAG, "Error searching pokemon in API: " + t.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    private void fetchFromApi(int limit, int offset) {
        apiService.getPokemonList(limit, offset).enqueue(new Callback<PokemonListResponse>() {
            @Override
            public void onResponse(Call<PokemonListResponse> call, Response<PokemonListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (PokemonListResponse.PokemonListItem item : response.body().getResults()) {
                        fetchPokemonDetails(item.getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<PokemonListResponse> call, Throwable t) {
                Log.e(TAG, "Error fetching pokemon list: " + t.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    private void fetchPokemonDetails(String name) {
        apiService.getPokemonDetails(name).enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Pokemon pokemon = response.body();
                    Executors.newSingleThreadExecutor().execute(() -> {
                        database.pokemonDao().insertAll(java.util.Collections.singletonList(pokemon));
                        // Update the LiveData with all cached pokemons
                        pokemons.postValue(database.pokemonDao().getAllPokemonsSync());
                    });
                }
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Log.e(TAG, "Error fetching pokemon details: " + t.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<List<Pokemon>> getPokemons() {
        return pokemons;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
