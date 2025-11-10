package com.iwkms.drivenext.domain.repository

import com.iwkms.drivenext.domain.model.AppTheme
import com.iwkms.drivenext.domain.model.User
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    val onboardingCompletedFlow: Flow<Boolean>
    val authTokenFlow: Flow<String?>
    val userFlow: Flow<User?>
    val themeFlow: Flow<AppTheme>
    val notificationsEnabledFlow: Flow<Boolean>

    suspend fun setOnboardingCompleted(completed: Boolean)
    suspend fun registerUser(user: User, password: String)
    suspend fun authenticate(email: String, password: String): Result<Unit>
    suspend fun logOut()
    suspend fun isLoggedIn(): Boolean
    suspend fun isOnboardingCompleted(): Boolean
    suspend fun updateTheme(theme: AppTheme)
    suspend fun updateNotifications(enabled: Boolean)
    suspend fun updateAvatar(avatarUrl: String?)
}
