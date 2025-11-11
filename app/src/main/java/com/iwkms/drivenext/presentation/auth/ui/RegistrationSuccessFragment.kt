package com.iwkms.drivenext.presentation.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.iwkms.drivenext.R
import com.iwkms.drivenext.data.repository.SessionRepositoryProvider
import com.iwkms.drivenext.databinding.FragmentRegistrationSuccessBinding
import com.iwkms.drivenext.presentation.auth.viewmodel.RegistrationViewModel
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding
import kotlinx.coroutines.launch

class RegistrationSuccessFragment : Fragment() {

    private var _binding: FragmentRegistrationSuccessBinding? = null
    private val binding get() = _binding!!

    private val registrationViewModel: RegistrationViewModel by navGraphViewModels(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationSuccessBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Disable going back to avoid duplicate submissions
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.applyStatusBarPadding()

        binding.btnNext.setOnClickListener {
            completeRegistration()
        }
    }

    private fun completeRegistration() {
        val summary = registrationViewModel.buildRegistrationSummary() ?: run {
            findNavController().navigate(R.id.action_global_authFragment)
            return
        }
        viewLifecycleOwner.lifecycleScope.launch {
            val repository = SessionRepositoryProvider.get(requireContext().applicationContext)
            repository.registerUser(summary.user, summary.password)
            repository.setOnboardingCompleted(true)
            findNavController().navigate(
                R.id.action_registrationSuccessFragment_to_homeFragment,
                null
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
