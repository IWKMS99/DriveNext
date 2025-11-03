package com.iwkms.drivenext.presentation.main.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.iwkms.drivenext.databinding.FragmentSearchResultBinding
import com.iwkms.drivenext.presentation.main.home.adapter.CarAdapter
import com.iwkms.drivenext.presentation.main.home.viewmodel.SearchResultViewModel

class SearchResultFragment : Fragment() {
    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchResultViewModel by viewModels()
    private val args: SearchResultFragmentArgs by navArgs()
    private lateinit var carAdapter: CarAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        setupObservers()

        viewModel.search(args.searchQuery)
    }

    private fun setupRecyclerView() {
        carAdapter = CarAdapter(
            onBookClick = { car -> Toast.makeText(requireContext(), "Бронирование ${car.model}", Toast.LENGTH_SHORT).show() },
            onDetailsClick = { car -> Toast.makeText(requireContext(), "Детали ${car.model}", Toast.LENGTH_SHORT).show() }
        )
        binding.rvSearchResults.adapter = carAdapter
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupObservers() {
        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            carAdapter.submitList(results)
            binding.tvNoResults.isVisible = results.isEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}