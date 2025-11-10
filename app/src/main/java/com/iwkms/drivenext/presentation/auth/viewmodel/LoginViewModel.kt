package com.iwkms.drivenext.presentation.auth.viewmodel

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.iwkms.drivenext.data.repository.SessionRepositoryProvider
import com.iwkms.drivenext.domain.repository.SessionRepository
import kotlinx.coroutines.launch

sealed class SignInState {
    object Idle : SignInState()
    object Loading : SignInState()
    object Success : SignInState()
    data class Error(val reason: SignInError) : SignInState()
}

enum class SignInError {
    InvalidCredentials,
    Unknown
}

class LoginViewModel(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _email = MutableLiveData("")
    private val _password = MutableLiveData("")

    private val _isLoginButtonEnabled = MutableLiveData(false)
    val isLoginButtonEnabled: LiveData<Boolean> get() = _isLoginButtonEnabled

    private val _signInState = MutableLiveData<SignInState>(SignInState.Idle)
    val signInState: LiveData<SignInState> get() = _signInState

    fun onEmailChanged(value: String) {
        _email.value = value
        validateInputs()
    }

    fun onPasswordChanged(value: String) {
        _password.value = value
        validateInputs()
    }

    fun onLoginClicked() {
        if (_signInState.value is SignInState.Loading) return
        val emailValue = _email.value.orEmpty()
        val passwordValue = _password.value.orEmpty()

        viewModelScope.launch {
            _signInState.value = SignInState.Loading
            runCatching {
                sessionRepository.authenticate(emailValue, passwordValue).getOrThrow()
            }.onSuccess {
                _signInState.value = SignInState.Success
            }.onFailure { error ->
                val reason = if (error.message == "invalid_credentials") {
                    SignInError.InvalidCredentials
                } else {
                    SignInError.Unknown
                }
                _signInState.value = SignInState.Error(reason)
            }
        }
    }

    private fun validateInputs() {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(_email.value.orEmpty()).matches()
        val isPasswordValid = _password.value.orEmpty().length >= MIN_PASSWORD_LENGTH
        _isLoginButtonEnabled.value = isEmailValid && isPasswordValid
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
    }
}

class LoginViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(
                SessionRepositoryProvider.get(context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
