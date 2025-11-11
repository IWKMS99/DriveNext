package com.iwkms.drivenext.presentation.main.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.iwkms.drivenext.databinding.FragmentHomeBinding
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding
import com.iwkms.drivenext.presentation.main.home.adapter.CarAdapter
import com.iwkms.drivenext.presentation.main.home.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var carAdapter: CarAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.applyStatusBarPadding()
        setupRecyclerView()
        setupListeners()
        setupObservers()
    }

    private fun setupRecyclerView() {
        carAdapter = CarAdapter(
            onBookClick = { car ->
                val action = HomeFragmentDirections.actionHomeFragmentToBookingFragment(car.id)
                findNavController().navigate(action)
            },
            onDetailsClick = { car ->
                val action = HomeFragmentDirections.actionHomeFragmentToCarDetailsFragment(car.id)
                findNavController().navigate(action)
            }
        )
        binding.rvCars.adapter = carAdapter
    }

    private fun setupListeners() {
        binding.etSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchQuery = textView.text.toString().trim()
                if (searchQuery.isNotEmpty()) {
                    val action = HomeFragmentDirections.actionHomeFragmentToLoaderFragment(searchQuery)
                    findNavController().navigate(action)
                }
                return@setOnEditorActionListener true
            }
            false
        }

        binding.btnRetry.setOnClickListener {
            viewModel.reload()
        }
    }

    private fun setupObservers() {
        viewModel.cars.observe(viewLifecycleOwner) { cars ->
            if (cars.isNotEmpty()) {
                binding.rvCars.isVisible = true
                carAdapter.submitList(cars)
            } else {
                binding.rvCars.isVisible = false
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            if (isLoading) {
                binding.errorContainer.isVisible = false
                binding.rvCars.isVisible = false
            } else if (viewModel.errorResId.value == null) {
                binding.rvCars.isVisible = viewModel.cars.value?.isNotEmpty() == true
            }
        }

        viewModel.errorResId.observe(viewLifecycleOwner) { errorResId ->
            val hasError = errorResId != null
            binding.errorContainer.isVisible = hasError
            binding.rvCars.isVisible = !hasError && (viewModel.cars.value?.isNotEmpty() == true)
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
