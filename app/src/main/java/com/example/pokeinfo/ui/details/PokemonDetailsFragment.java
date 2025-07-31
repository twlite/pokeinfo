package com.example.pokeinfo.ui.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.pokeinfo.databinding.FragmentPokemonDetailsBinding;
import com.example.pokeinfo.data.model.PokemonType;
import com.google.android.material.chip.Chip;

public class PokemonDetailsFragment extends Fragment {
    private FragmentPokemonDetailsBinding binding;
    private PokemonDetailsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPokemonDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String pokemonName = PokemonDetailsFragmentArgs.fromBundle(getArguments()).getPokemonName();
        viewModel = new ViewModelProvider(this).get(PokemonDetailsViewModel.class);
        viewModel.loadPokemon(pokemonName);

        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        binding.favoriteButton.setOnClickListener(v -> viewModel.toggleFavorite());

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getPokemon().observe(getViewLifecycleOwner(), pokemon -> {
            if (pokemon != null) {
                binding.pokemonName.setText(pokemon.getName());
                Glide.with(this)
                    .load(pokemon.getImageUrl().getFrontDefault())
                    .into(binding.pokemonImage);

                binding.typeChipGroup.removeAllViews();
                for (PokemonType type : pokemon.getTypes()) {
                    Chip chip = new Chip(requireContext());
                    chip.setText(type.getType().getName());
                    binding.typeChipGroup.addView(chip);
                }

                // Update favorite button text based on state
                binding.favoriteButton.setText(pokemon.isFavorite()
                    ? "Remove from Favorites"
                    : "Add to Favorites");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
