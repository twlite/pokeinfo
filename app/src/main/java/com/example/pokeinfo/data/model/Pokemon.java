package com.example.pokeinfo.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.squareup.moshi.Json;

@Entity(tableName = "pokemons")
public class Pokemon {
    @PrimaryKey
    private int id;
    private String name;
    @Json(name = "sprites")
    private PokemonSprites imageUrl;
    private java.util.List<PokemonType> types;
    private boolean isFavorite;

    public Pokemon() {
        this.isFavorite = false;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PokemonSprites getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(PokemonSprites imageUrl) {
        this.imageUrl = imageUrl;
    }

    public java.util.List<PokemonType> getTypes() {
        return types;
    }

    public void setTypes(java.util.List<PokemonType> types) {
        this.types = types;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return id == pokemon.id;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }
}
