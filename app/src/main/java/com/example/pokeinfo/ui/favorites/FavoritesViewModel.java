package com.example.pokeinfo.ui.favorites;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.pokeinfo.data.model.Pokemon;
import com.example.pokeinfo.data.db.AppDatabase;
import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {
    private final AppDatabase database;

    public FavoritesViewModel(Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
    }

    public LiveData<List<Pokemon>> getFavoritePokemons() {
        return database.pokemonDao().getFavoritePokemons();
    }
}
