package com.iwkms.drivenext.presentation.main.favorites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iwkms.drivenext.R
import com.iwkms.drivenext.data.repository.FakeCarRepository
import com.iwkms.drivenext.databinding.FragmentFavoritesBinding
import com.iwkms.drivenext.domain.repository.CarRepository
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding
import com.iwkms.drivenext.presentation.main.home.adapter.CarAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val carRepository: CarRepository = FakeCarRepository()
    private lateinit var adapter: CarAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.applyStatusBarPadding()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        loadFavorites()
    }

    private fun setupRecyclerView() {
        adapter = CarAdapter(
            onBookClick = { car ->
                val action =
                    FavoritesFragmentDirections.actionFavoritesFragmentToCarDetailsFragment(car.id)
                findNavController().navigate(action)
            },
            onDetailsClick = { car ->
                val action =
                    FavoritesFragmentDirections.actionFavoritesFragmentToCarDetailsFragment(car.id)
                findNavController().navigate(action)
            }
        )
        binding.rvFavorites.adapter = adapter
    }

    private fun loadFavorites() {
        val favorites = carRepository.getFavorites()
        adapter.submitList(favorites)
        binding.rvFavorites.isVisible = favorites.isNotEmpty()
        binding.tvEmpty.isVisible = favorites.isEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}