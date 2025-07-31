package com.example.pokeinfo.data.db;

import androidx.room.TypeConverter;
import com.example.pokeinfo.data.model.PokemonSprites;
import com.example.pokeinfo.data.model.PokemonType;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    private final Moshi moshi;
    private final JsonAdapter<PokemonSprites> spritesAdapter;
    private final JsonAdapter<List<PokemonType>> typesAdapter;

    public Converters() {
        moshi = new Moshi.Builder().build();
        spritesAdapter = moshi.adapter(PokemonSprites.class);
        Type listType = Types.newParameterizedType(List.class, PokemonType.class);
        typesAdapter = moshi.adapter(listType);
    }

    @TypeConverter
    public String fromPokemonSprites(PokemonSprites sprites) {
        return sprites != null ? spritesAdapter.toJson(sprites) : null;
    }

    @TypeConverter
    public PokemonSprites toPokemonSprites(String value) {
        try {
            return value != null ? spritesAdapter.fromJson(value) : null;
        } catch (Exception e) {
            return null;
        }
    }

    @TypeConverter
    public String fromTypeList(List<PokemonType> types) {
        return types != null ? typesAdapter.toJson(types) : null;
    }

    @TypeConverter
    public List<PokemonType> toTypeList(String value) {
        try {
            return value != null ? typesAdapter.fromJson(value) : null;
        } catch (Exception e) {
            return null;
        }
    }
}
