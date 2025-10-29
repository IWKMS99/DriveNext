package com.iwkms.drivenext.presentation.auth.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iwkms.drivenext.data.supabase.SupabaseClient
import io.github.jan.supabase.auth.providers.Google
import kotlinx.coroutines.launch

sealed class SignInState {
    object Idle : SignInState()
    object Loading : SignInState()
    object Success : SignInState()
    data class Error(val message: String) : SignInState()
}

class LoginViewModel : ViewModel() {

    val isLoginButtonEnabled = MutableLiveData(false)
    val email = MutableLiveData("")
    val password = MutableLiveData("")

    private val _signInState = MutableLiveData<SignInState>(SignInState.Idle)
    val signInState: LiveData<SignInState> get() = _signInState

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _signInState.value = SignInState.Loading
            try {
                SupabaseClient.auth.signInWith(Google, idToken)

                Log.d("SupabaseAuth", "Sign-in with Google successful.")
                _signInState.value = SignInState.Success
            } catch (e: Exception) {
                Log.e("SupabaseAuth", "Sign-in error", e)
                _signInState.value = SignInState.Error("Ошибка входа: ${e.message}")
            }
        }
    }
}