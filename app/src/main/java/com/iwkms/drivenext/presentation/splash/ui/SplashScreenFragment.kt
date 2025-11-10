package com.iwkms.drivenext.presentation.splash.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.iwkms.drivenext.R
import com.iwkms.drivenext.data.repository.SessionRepositoryProvider
import com.iwkms.drivenext.databinding.FragmentSplashScreenBinding
import com.iwkms.drivenext.presentation.common.util.NetworkUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    private val sessionRepository by lazy {
        SessionRepositoryProvider.get(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            delay(SPLASH_DELAY)
            if (!NetworkUtils.isOnline(requireContext())) {
                findNavController().navigate(R.id.action_splashScreenFragment_to_noConnectionFragment)
                return@launch
            }

            val destination = when {
                sessionRepository.isOnboardingCompleted().not() -> {
                    R.id.action_splashScreenFragment_to_onboardingFragment
                }

                sessionRepository.isLoggedIn().not() -> {
                    R.id.action_splashScreenFragment_to_authFragment
                }

                else -> R.id.action_splashScreenFragment_to_homeFragment
            }
            findNavController().navigate(destination)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val SPLASH_DELAY = 2_000L
    }
}
