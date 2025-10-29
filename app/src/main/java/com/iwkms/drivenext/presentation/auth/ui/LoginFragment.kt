package com.iwkms.drivenext.presentation.auth.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentLoginBinding
import com.iwkms.drivenext.presentation.auth.viewmodel.LoginViewModel
import com.iwkms.drivenext.presentation.auth.viewmodel.SignInState

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    val idToken = account.idToken
                    if (idToken != null) {
                        viewModel.signInWithGoogle(idToken)
                    } else {
                        showAuthError("Google ID Token is null")
                    }
                } catch (e: ApiException) {
                    showAuthError("signInResult:failed code=${e.statusCode}")
                }
            } else {
                showAuthError("Google Sign-In was cancelled or failed.")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGoogleSignIn()
        setupListeners()
        setupObservers()
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun setupListeners() {
        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.email.value = text.toString()
        }
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.password.value = text.toString()
        }

        binding.btnLogin.setOnClickListener {
            // TODO: Реализовать вход через email/пароль
            Toast.makeText(
                requireContext(),
                getString(R.string.login_success_toast),
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationStep1Fragment)
        }

        binding.btnLoginGoogle.setOnClickListener {
            if (viewModel.signInState.value !is SignInState.Loading) {
                val signInIntent = googleSignInClient.signInIntent
                googleSignInLauncher.launch(signInIntent)
            }
        }
    }

    private fun setupObservers() {
        viewModel.isLoginButtonEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.btnLogin.isEnabled = isEnabled
        }

        viewModel.signInState.observe(viewLifecycleOwner) { state ->
            handleSignInState(state)
        }
    }

    private fun handleSignInState(state: SignInState) {
        val isLoading = state is SignInState.Loading
        binding.progressBar.isVisible = isLoading
        binding.btnLoginGoogle.isEnabled = !isLoading
        binding.btnLogin.isEnabled = !isLoading
        binding.btnGoToRegister.isEnabled = !isLoading

        when (state) {
            is SignInState.Success -> {
                Toast.makeText(requireContext(), "Вход выполнен успешно!", Toast.LENGTH_LONG).show()
                findNavController().navigate(
                    R.id.action_loginFragment_to_homeFragment,
                    null,

                    )
            }

            is SignInState.Error -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
            }

            else -> {}
        }
    }

    private fun showAuthError(message: String) {
        Log.w("GoogleSignIn", message)
        Toast.makeText(requireContext(), "Ошибка аутентификации Google", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}