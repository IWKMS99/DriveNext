package com.iwkms.drivenext.presentation.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentLoginBinding
import com.iwkms.drivenext.presentation.auth.viewmodel.LoginViewModel
import com.iwkms.drivenext.presentation.auth.viewmodel.LoginViewModelFactory
import com.iwkms.drivenext.presentation.auth.viewmodel.SignInError
import com.iwkms.drivenext.presentation.auth.viewmodel.SignInState

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.onEmailChanged(text?.toString().orEmpty())
            binding.tilEmail.error = null
        }
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordChanged(text?.toString().orEmpty())
            binding.tilPassword.error = null
        }

        binding.btnLogin.setOnClickListener {
            viewModel.onLoginClicked()
        }

        binding.btnGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationStep1Fragment)
        }

        binding.btnLoginGoogle.setOnClickListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.google_sign_in_placeholder),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupObservers() {
        viewModel.isLoginButtonEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.btnLogin.isEnabled = enabled && binding.progressBar.isVisible.not()
        }

        viewModel.signInState.observe(viewLifecycleOwner, ::handleSignInState)
    }

    private fun handleSignInState(state: SignInState) {
        if (state !is SignInState.Error) {
            binding.tilPassword.error = null
        }
        val isLoading = state is SignInState.Loading
        binding.progressBar.isVisible = isLoading
        binding.btnLogin.isEnabled = !isLoading && viewModel.isLoginButtonEnabled.value == true
        binding.btnLoginGoogle.isEnabled = !isLoading
        binding.btnGoToRegister.isEnabled = !isLoading

        when (state) {
            is SignInState.Success -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.login_success_toast),
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(
                    R.id.action_loginFragment_to_homeFragment,
                    null
                )
            }

            is SignInState.Error -> {
                val messageRes = when (state.reason) {
                    SignInError.InvalidCredentials -> R.string.login_error_invalid_credentials
                    SignInError.Unknown -> R.string.login_error_unknown
                }
                binding.tilPassword.error = getString(messageRes)
            }

            else -> Unit
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
