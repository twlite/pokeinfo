package com.example.pokeinfo.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pokeinfo.data.model.Pokemon;
import com.example.pokeinfo.data.model.PokemonType;
import com.example.pokeinfo.databinding.ItemPokemonBinding;
import com.google.android.material.chip.Chip;
import com.bumptech.glide.Glide;

public class PokemonAdapter extends ListAdapter<Pokemon, PokemonAdapter.PokemonViewHolder> {
    private OnPokemonClickListener clickListener;

    public interface OnPokemonClickListener {
        void onPokemonClick(Pokemon pokemon);
    }

    public PokemonAdapter(OnPokemonClickListener listener) {
        super(new PokemonDiffCallback());
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPokemonBinding binding = ItemPokemonBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new PokemonViewHolder(binding, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class PokemonViewHolder extends RecyclerView.ViewHolder {
        private final ItemPokemonBinding binding;
        private final OnPokemonClickListener listener;

        PokemonViewHolder(ItemPokemonBinding binding, OnPokemonClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
        }

        void bind(Pokemon pokemon) {
            binding.pokemonName.setText(pokemon.getName());

            // Load image using Glide
            Glide.with(binding.pokemonImage)
                .load(pokemon.getImageUrl().getFrontDefault())
                .into(binding.pokemonImage);

            // Set up type chips
            binding.typeChipGroup.removeAllViews();
            for (PokemonType type : pokemon.getTypes()) {
                Chip chip = new Chip(binding.getRoot().getContext());
                chip.setText(type.getType().getName());
                binding.typeChipGroup.addView(chip);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPokemonClick(pokemon);
                }
            });
        }
    }

    static class PokemonDiffCallback extends DiffUtil.ItemCallback<Pokemon> {
        @Override
        public boolean areItemsTheSame(@NonNull Pokemon oldItem, @NonNull Pokemon newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Pokemon oldItem, @NonNull Pokemon newItem) {
            return oldItem.equals(newItem);
        }
    }
}
