package com.iwkms.drivenext.presentation.main.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentProfileBinding
import com.iwkms.drivenext.presentation.main.settings.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnLogout.setOnClickListener {
            viewModel.onLogOutClicked()
        }
    }

    private fun setupObservers() {
        viewModel.navigateToAuth.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                findNavController().navigate(R.id.action_global_authFragment)
                viewModel.onNavigationComplete()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}