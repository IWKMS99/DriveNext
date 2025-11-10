package com.iwkms.drivenext.presentation.main.settings.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.iwkms.drivenext.data.repository.SessionRepositoryProvider
import com.iwkms.drivenext.domain.model.User
import com.iwkms.drivenext.domain.repository.SessionRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _navigateToAuth = MutableLiveData<Boolean>()
    val navigateToAuth: LiveData<Boolean> get() = _navigateToAuth

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    init {
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            sessionRepository.userFlow.collectLatest { storedUser ->
                storedUser?.let { _user.postValue(it) }
            }
        }
    }

    fun updateAvatar(uri: Uri) {
        viewModelScope.launch {
            sessionRepository.updateAvatar(uri.toString())
        }
    }

    fun onLogOutClicked() {
        viewModelScope.launch {
            sessionRepository.logOut()
            _navigateToAuth.postValue(true)
        }
    }

    fun onNavigationComplete() {
        _navigateToAuth.value = false
    }
}

class ProfileViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(
                SessionRepositoryProvider.get(context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
