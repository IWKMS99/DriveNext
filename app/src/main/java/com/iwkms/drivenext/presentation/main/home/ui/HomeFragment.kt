package com.iwkms.drivenext.presentation.main.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.iwkms.drivenext.databinding.FragmentHomeBinding
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
        setupRecyclerView()
        setupListeners()
        setupObservers()
    }

    private fun setupRecyclerView() {
        carAdapter = CarAdapter(
            onBookClick = { car ->
                Toast.makeText(requireContext(), "Бронирование ${car.model}", Toast.LENGTH_SHORT)
                    .show()
            },
            onDetailsClick = { car ->
                Toast.makeText(requireContext(), "Детали ${car.model}", Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvCars.adapter = carAdapter
    }

    private fun setupObservers() {
        viewModel.cars.observe(viewLifecycleOwner) { cars ->
            carAdapter.submitList(cars)
        }
    }

    private fun setupListeners() {
        binding.etSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchQuery = textView.text.toString().trim()
                if (searchQuery.isNotEmpty()) {
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToLoaderFragment(searchQuery)
                    findNavController().navigate(action)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}