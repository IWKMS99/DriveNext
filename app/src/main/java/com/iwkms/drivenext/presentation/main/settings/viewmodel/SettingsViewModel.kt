package com.iwkms.drivenext.presentation.main.settings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwkms.drivenext.R
import com.iwkms.drivenext.domain.model.User
import com.iwkms.drivenext.presentation.main.settings.model.SettingsItem

class SettingsViewModel : ViewModel() {

    private val _settingsItems = MutableLiveData<List<SettingsItem>>()
    val settingsItems: LiveData<List<SettingsItem>> get() = _settingsItems

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    init {
        loadSettingsItems()
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

    private fun loadSettingsItems() {
        val items = listOf(
            SettingsItem(R.drawable.ic_bookings, R.string.settings_my_bookings),
            SettingsItem(R.drawable.ic_theme, R.string.settings_theme),
            SettingsItem(R.drawable.ic_notifications, R.string.settings_notifications),
            SettingsItem(R.drawable.ic_add_car, R.string.settings_connect_car),
            SettingsItem(R.drawable.ic_help, R.string.settings_help),
            SettingsItem(R.drawable.ic_invite_friend, R.string.settings_invite_friend)
        )
        _settingsItems.value = items
    }
}