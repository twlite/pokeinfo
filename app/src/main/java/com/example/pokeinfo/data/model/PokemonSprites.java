package com.example.pokeinfo.data.model;

import com.squareup.moshi.Json;

public class PokemonSprites {
    @Json(name = "front_default")
    private String frontDefault;

    public String getFrontDefault() {
        return frontDefault;
    }

    public void setFrontDefault(String frontDefault) {
        this.frontDefault = frontDefault;
    }
}
