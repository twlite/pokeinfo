package com.example.pokeinfo.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.pokeinfo.data.model.Pokemon;
import java.util.List;

@Dao
public interface PokemonDao {
    @Query("SELECT * FROM pokemons")
    LiveData<List<Pokemon>> getAllPokemons();

    @Query("SELECT * FROM pokemons")
    List<Pokemon> getAllPokemonsSync();

    @Query("SELECT * FROM pokemons WHERE isFavorite = 1")
    LiveData<List<Pokemon>> getFavoritePokemons();

    @Query("SELECT * FROM pokemons WHERE name LIKE :query")
    LiveData<List<Pokemon>> searchPokemons(String query);

    @Query("SELECT * FROM pokemons WHERE name LIKE :query")
    List<Pokemon> searchPokemonsSync(String query);

    @Query("SELECT * FROM pokemons WHERE name = :name")
    Pokemon getPokemonByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Pokemon> pokemons);

    @Update
    void update(Pokemon pokemon);
}
