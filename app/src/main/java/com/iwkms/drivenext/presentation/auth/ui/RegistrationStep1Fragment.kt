package com.iwkms.drivenext.presentation.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentRegistrationStep1Binding
import com.iwkms.drivenext.presentation.auth.viewmodel.RegistrationViewModel

class RegistrationStep1Fragment : Fragment() {

    private var _binding: FragmentRegistrationStep1Binding? = null
    private val binding get() = _binding!!

    private val viewModel: RegistrationViewModel by navGraphViewModels(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.email.value = text.toString()
        }
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.password.value = text.toString()
        }
        binding.etRepeatPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.repeatPassword.value = text.toString()
        }
        binding.cbTerms.setOnCheckedChangeListener { _, isChecked ->
            viewModel.termsAccepted.value = isChecked
        }

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_registrationStep1Fragment_to_registrationStep2Fragment)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupObservers() {
        viewModel.step1ValidationState.observe(viewLifecycleOwner) { state ->
            binding.btnNext.isEnabled = state.isNextButtonEnabled

            if (binding.etEmail.text.toString().isNotEmpty()) {
                binding.tilEmail.error = if (state.isEmailValid) null else getString(R.string.error_invalid_email)
            }
            if (binding.etPassword.text.toString().isNotEmpty()) {
                binding.tilPassword.error = if (state.isPasswordValid) null else getString(R.string.error_password_length)
            }
            if (binding.etRepeatPassword.text.toString().isNotEmpty()) {
                binding.tilRepeatPassword.error = if (state.doPasswordsMatch) null else getString(R.string.error_passwords_do_not_match)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}