package com.iwkms.drivenext.presentation.main.settings.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.iwkms.drivenext.R
import com.iwkms.drivenext.data.repository.SessionRepositoryProvider
import com.iwkms.drivenext.domain.model.AppTheme
import com.iwkms.drivenext.domain.model.User
import com.iwkms.drivenext.domain.repository.SessionRepository
import com.iwkms.drivenext.presentation.main.settings.model.SettingsItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _settingsItems = MutableLiveData<List<SettingsItem>>()
    val settingsItems: LiveData<List<SettingsItem>> get() = _settingsItems

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _theme = MutableLiveData<AppTheme>()
    val theme: LiveData<AppTheme> get() = _theme

    private val _notificationsState = MutableLiveData<Boolean>()
    val notificationsState: LiveData<Boolean> get() = _notificationsState

    private var currentTheme: AppTheme = AppTheme.SYSTEM
    private var notificationsEnabled: Boolean = true

    init {
        observeUser()
        observePreferences()
        buildSettingsItems()
    }

    private fun observeUser() {
        viewModelScope.launch {
            sessionRepository.userFlow.collectLatest { storedUser ->
                storedUser?.let { _user.postValue(it) }
            }
        }
    }

    private fun observePreferences() {
        viewModelScope.launch {
            sessionRepository.themeFlow.collectLatest { theme ->
                currentTheme = theme
                _theme.postValue(theme)
                buildSettingsItems()
            }
        }
        viewModelScope.launch {
            sessionRepository.notificationsEnabledFlow.collectLatest { enabled ->
                notificationsEnabled = enabled
                _notificationsState.postValue(enabled)
                buildSettingsItems()
            }
        }
    }

    private fun buildSettingsItems() {
        val items = listOf(
            SettingsItem(R.drawable.ic_bookings, R.string.settings_my_bookings),
            SettingsItem(R.drawable.ic_theme, R.string.settings_theme, valueResId = currentTheme.asLabelRes()),
            SettingsItem(
                R.drawable.ic_notifications,
                R.string.settings_notifications,
                valueResId = if (notificationsEnabled) R.string.settings_notifications_on else R.string.settings_notifications_off
            ),
            SettingsItem(R.drawable.ic_add_car, R.string.settings_connect_car),
            SettingsItem(R.drawable.ic_help, R.string.settings_help),
            SettingsItem(R.drawable.ic_invite_friend, R.string.settings_invite_friend)
        )
        _settingsItems.postValue(items)
    }

    fun updateTheme(theme: AppTheme) {
        viewModelScope.launch {
            sessionRepository.updateTheme(theme)
        }
    }

    fun updateNotifications(enabled: Boolean) {
        viewModelScope.launch {
            sessionRepository.updateNotifications(enabled)
        }
    }

    private fun AppTheme.asLabelRes(): Int = when (this) {
        AppTheme.SYSTEM -> R.string.theme_system
        AppTheme.LIGHT -> R.string.theme_light
        AppTheme.DARK -> R.string.theme_dark
    }
}

class SettingsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(
                SessionRepositoryProvider.get(context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
