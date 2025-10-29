package com.iwkms.drivenext.presentation.main.settings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iwkms.drivenext.data.repository.AuthRepositoryImpl
import com.iwkms.drivenext.domain.usecase.LogOutUseCase
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val authRepository = AuthRepositoryImpl()
    private val logOutUseCase = LogOutUseCase(authRepository)

    private val _navigateToAuth = MutableLiveData<Boolean>()
    val navigateToAuth: LiveData<Boolean> get() = _navigateToAuth

    fun onLogOutClicked() {
        viewModelScope.launch {
            logOutUseCase.execute()
            _navigateToAuth.value = true
        }
    }

    fun onNavigationComplete() {
        _navigateToAuth.value = false
    }
}