package com.example.pokeinfo.data.api;

import com.squareup.moshi.Json;
import java.util.List;

public class PokemonListResponse {
    private int count;
    private List<PokemonListItem> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PokemonListItem> getResults() {
        return results;
    }

    public void setResults(List<PokemonListItem> results) {
        this.results = results;
    }

    public static class PokemonListItem {
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
