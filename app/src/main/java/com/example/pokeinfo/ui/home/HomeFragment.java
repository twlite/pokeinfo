package com.example.pokeinfo.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.pokeinfo.databinding.FragmentHomeBinding;
import com.example.pokeinfo.ui.adapters.PokemonAdapter;
import com.example.pokeinfo.data.model.Pokemon;

public class HomeFragment extends Fragment implements PokemonAdapter.OnPokemonClickListener {
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private PokemonAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            binding = FragmentHomeBinding.inflate(inflater, container, false);
            return binding.getRoot();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateView: " + e.getMessage());
            return new View(requireContext());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
            setupRecyclerView();
            setupSearch();
            observeViewModel();
        } catch (Exception e) {
            Log.e(TAG, "Error in onViewCreated: " + e.getMessage());
        }
    }

    private void setupRecyclerView() {
        try {
            adapter = new PokemonAdapter(this);
            binding.pokemonRecyclerView.setAdapter(adapter);
            binding.pokemonRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        } catch (Exception e) {
            Log.e(TAG, "Error setting up RecyclerView: " + e.getMessage());
        }
    }

    private void setupSearch() {
        binding.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.searchEditText.getText().toString();
                viewModel.searchPokemons(query);
                return true;
            }
            return false;
        });
    }

    private void observeViewModel() {
        viewModel.getPokemons().observe(getViewLifecycleOwner(), pokemons -> {
            adapter.submitList(pokemons);
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onPokemonClick(Pokemon pokemon) {
        HomeFragmentDirections.ActionHomeToDetails action =
            HomeFragmentDirections.actionHomeToDetails(pokemon.getName());
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
