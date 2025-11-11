package com.iwkms.drivenext.presentation.main.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.iwkms.drivenext.databinding.FragmentSearchResultBinding
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding
import com.iwkms.drivenext.presentation.main.home.adapter.CarAdapter
import com.iwkms.drivenext.presentation.main.home.viewmodel.SearchResultViewModel

class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchResultViewModel by viewModels()
    private val args: SearchResultFragmentArgs by navArgs()
    private lateinit var carAdapter: CarAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.applyStatusBarPadding()
        setupRecyclerView()
        setupListeners()
        setupObservers()
        viewModel.search(args.searchQuery)
    }

    private fun setupRecyclerView() {
        carAdapter = CarAdapter(
            onBookClick = { car ->
                val action = SearchResultFragmentDirections.actionSearchResultFragmentToBookingFragment(car.id)
                findNavController().navigate(action)
            },
            onDetailsClick = { car ->
                val action = SearchResultFragmentDirections.actionSearchResultFragmentToCarDetailsFragment(car.id)
                findNavController().navigate(action)
            }
        )
        binding.rvSearchResults.adapter = carAdapter
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnRetry.setOnClickListener {
            viewModel.search(args.searchQuery)
        }
    }

    private fun setupObservers() {
        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            carAdapter.submitList(results)
            val hasData = results.isNotEmpty()
            binding.rvSearchResults.isVisible = hasData && viewModel.errorResId.value == null
            binding.tvNoResults.isVisible = results.isEmpty() && viewModel.errorResId.value == null && viewModel.isLoading.value == false
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            if (isLoading) {
                binding.errorContainer.isVisible = false
                binding.tvNoResults.isVisible = false
                binding.rvSearchResults.isVisible = false
            } else if (viewModel.errorResId.value == null) {
                val hasData = viewModel.searchResults.value?.isNotEmpty() == true
                binding.rvSearchResults.isVisible = hasData
                binding.tvNoResults.isVisible = !hasData
            }
        }

        viewModel.errorResId.observe(viewLifecycleOwner) { errorResId ->
            val hasError = errorResId != null
            binding.errorContainer.isVisible = hasError
            binding.rvSearchResults.isVisible = !hasError && viewModel.searchResults.value?.isNotEmpty() == true
            binding.tvNoResults.isVisible = !hasError && viewModel.searchResults.value.isNullOrEmpty()
            if (hasError && errorResId != null) {
                binding.tvError.setText(errorResId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
