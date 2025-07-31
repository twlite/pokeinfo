package com.example.pokeinfo.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.pokeinfo.databinding.FragmentFavoritesBinding;
import com.example.pokeinfo.ui.adapters.PokemonAdapter;
import com.example.pokeinfo.data.model.Pokemon;

public class FavoritesFragment extends Fragment implements PokemonAdapter.OnPokemonClickListener {
    private FragmentFavoritesBinding binding;
    private FavoritesViewModel viewModel;
    private PokemonAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
        setupRecyclerView();
        observeViewModel();
    }

    private void setupRecyclerView() {
        adapter = new PokemonAdapter(this);
        binding.favoriteRecyclerView.setAdapter(adapter);
        binding.favoriteRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
    }

    private void observeViewModel() {
        viewModel.getFavoritePokemons().observe(getViewLifecycleOwner(), pokemons -> {
            adapter.submitList(pokemons);
            binding.emptyView.setVisibility(pokemons.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onPokemonClick(Pokemon pokemon) {
        FavoritesFragmentDirections.ActionFavoritesToDetails action =
            FavoritesFragmentDirections.actionFavoritesToDetails(pokemon.getName());
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
