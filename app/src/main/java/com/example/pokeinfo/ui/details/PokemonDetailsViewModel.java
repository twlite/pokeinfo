package com.example.pokeinfo.ui.details;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.pokeinfo.data.model.Pokemon;
import com.example.pokeinfo.data.db.AppDatabase;
import java.util.concurrent.Executors;

public class PokemonDetailsViewModel extends AndroidViewModel {
    private final AppDatabase database;
    private final MutableLiveData<Pokemon> pokemon = new MutableLiveData<>();
    private String currentPokemonName;

    public PokemonDetailsViewModel(Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
    }

    public void loadPokemon(String name) {
        currentPokemonName = name;
        Executors.newSingleThreadExecutor().execute(() -> {
            Pokemon pokemonData = database.pokemonDao().getPokemonByName(name);
            pokemon.postValue(pokemonData);
        });
    }

    public void toggleFavorite() {
        Pokemon currentPokemon = pokemon.getValue();
        if (currentPokemon != null) {
            currentPokemon.setFavorite(!currentPokemon.isFavorite());
            pokemon.setValue(currentPokemon);

            Executors.newSingleThreadExecutor().execute(() -> {
                database.pokemonDao().update(currentPokemon);
            });
        }
    }

    public LiveData<Pokemon> getPokemon() {
        return pokemon;
    }
}
