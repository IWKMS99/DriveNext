package com.iwkms.drivenext.presentation.main.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentSettingsBinding
import com.iwkms.drivenext.presentation.main.settings.adapter.SettingsAdapter
import com.iwkms.drivenext.presentation.main.settings.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var settingsAdapter: SettingsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        setupObservers()
    }

    private fun setupRecyclerView() {
        settingsAdapter = SettingsAdapter { item ->
            // TODO: Handle clicks on settings items
            Toast.makeText(requireContext(), getString(item.titleResId), Toast.LENGTH_SHORT).show()
        }
        binding.rvSettings.apply {
            adapter = settingsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupListeners() {
        binding.profileHeader.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_profileFragment)
        }
    }

    private fun setupObservers() {
        viewModel.settingsItems.observe(viewLifecycleOwner) { items ->
            settingsAdapter.submitList(items)
        }
        // TODO: Observe user data to update header
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}