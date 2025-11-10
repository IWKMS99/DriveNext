package com.iwkms.drivenext.presentation.main.settings.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iwkms.drivenext.data.repository.AuthRepositoryImpl
import com.iwkms.drivenext.domain.model.User
import com.iwkms.drivenext.domain.usecase.LogOutUseCase
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val authRepository = AuthRepositoryImpl()
    private val logOutUseCase = LogOutUseCase(authRepository)

    private val _navigateToAuth = MutableLiveData<Boolean>()
    val navigateToAuth: LiveData<Boolean> get() = _navigateToAuth

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    init {
        loadUserData()
    }

    private fun loadUserData() {
        _user.value = User(
            name = "Иван Иванов",
            email = "ivan@mtuci.ru",
            avatarUrl = null,
            joinedDate = "Присоединился в июле 2024"
        )
    }

    fun updateAvatar(uri: Uri) {
        _user.value = _user.value?.copy(avatarUrl = uri.toString())
    }

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