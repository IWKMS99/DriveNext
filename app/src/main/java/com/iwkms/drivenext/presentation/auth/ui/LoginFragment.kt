package com.iwkms.drivenext.presentation.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentLoginBinding
import com.iwkms.drivenext.presentation.auth.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
            viewModel.email.value = text.toString()
        }
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.password.value = text.toString()
        }

        binding.btnLogin.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.login_success_toast), Toast.LENGTH_SHORT).show()
        }

        binding.btnGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationStep1Fragment)
        }
    }

    private fun setupObservers() {
        viewModel.isLoginButtonEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.btnLogin.isEnabled = isEnabled
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}