package com.iwkms.drivenext.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.iwkms.drivenext.data.storage.sessionDataStore
import com.iwkms.drivenext.domain.model.AppTheme
import com.iwkms.drivenext.domain.model.User
import com.iwkms.drivenext.domain.repository.SessionRepository
import java.security.MessageDigest
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.first

class SessionRepositoryImpl(context: Context) : SessionRepository {

    private val dataStore = context.applicationContext.sessionDataStore

    override val onboardingCompletedFlow: Flow<Boolean> =
        dataStore.data.map { preferences -> preferences[KEY_ONBOARDING_COMPLETED] ?: false }

    override val authTokenFlow: Flow<String?> =
        dataStore.data.map { preferences -> preferences[KEY_AUTH_TOKEN] }

    override val userFlow: Flow<User?> =
        dataStore.data.map { preferences -> preferences.toUser() }

    override val themeFlow: Flow<AppTheme> =
        dataStore.data.map { preferences ->
            preferences[KEY_THEME]?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() } ?: AppTheme.SYSTEM
        }

    override val notificationsEnabledFlow: Flow<Boolean> =
        dataStore.data.map { preferences -> preferences[KEY_NOTIFICATIONS_ENABLED] ?: true }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_ONBOARDING_COMPLETED] = completed
        }
    }

    override suspend fun registerUser(user: User, password: String) {
        withContext(Dispatchers.IO) {
            dataStore.edit { prefs ->
                prefs[KEY_REGISTERED_EMAIL] = user.email
                prefs[KEY_REGISTERED_PASSWORD] = hashPassword(password)
                prefs[KEY_REGISTERED_USER_NAME] = user.name
                user.avatarUrl?.let { prefs[KEY_REGISTERED_AVATAR] = it } ?: prefs.remove(KEY_REGISTERED_AVATAR)
                prefs[KEY_REGISTERED_JOINED_DATE] = user.joinedDate
            }
            persistSession(user)
        }
    }

    override suspend fun authenticate(email: String, password: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            val normalizedEmail = email.trim()
            val preferences = dataStore.data.first()

            val registeredEmail = preferences[KEY_REGISTERED_EMAIL]
            val registeredPassword = preferences[KEY_REGISTERED_PASSWORD]
            val hashedInput = hashPassword(password)

            val registeredMatch = registeredEmail != null &&
                registeredEmail.equals(normalizedEmail, ignoreCase = true) &&
                registeredPassword == hashedInput

            val defaultMatch = normalizedEmail.equals(DEFAULT_DEMO_USER.email, ignoreCase = true) &&
                password == DEFAULT_DEMO_PASSWORD

            when {
                registeredMatch -> {
                    val user = preferences.toRegisteredUser() ?: DEFAULT_DEMO_USER.copy(email = registeredEmail!!)
                    persistSession(user)
                    Result.success(Unit)
                }

                defaultMatch -> {
                    persistSession(DEFAULT_DEMO_USER)
                    Result.success(Unit)
                }

                else -> Result.failure(IllegalStateException("invalid_credentials"))
            }
        }

    override suspend fun logOut() {
        withContext(Dispatchers.IO) {
            dataStore.edit { prefs ->
                prefs.remove(KEY_AUTH_TOKEN)
            }
        }
    }

    override suspend fun isLoggedIn(): Boolean =
        authTokenFlow.first().isNullOrBlank().not()

    override suspend fun isOnboardingCompleted(): Boolean =
        onboardingCompletedFlow.first()

    override suspend fun updateTheme(theme: AppTheme) {
        dataStore.edit { prefs ->
            prefs[KEY_THEME] = theme.name
        }
    }

    override suspend fun updateNotifications(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_NOTIFICATIONS_ENABLED] = enabled
        }
    }

    override suspend fun updateAvatar(avatarUrl: String?) {
        withContext(Dispatchers.IO) {
            dataStore.edit { prefs ->
                if (avatarUrl == null) {
                    prefs.remove(KEY_REGISTERED_AVATAR)
                    prefs.remove(KEY_USER_AVATAR)
                } else {
                    prefs[KEY_REGISTERED_AVATAR] = avatarUrl
                    prefs[KEY_USER_AVATAR] = avatarUrl
                }
            }
            val currentUser = userFlow.first()
            if (currentUser != null) {
                persistSession(currentUser.copy(avatarUrl = avatarUrl))
            }
        }
    }

    private suspend fun persistSession(user: User) {
        dataStore.edit { prefs ->
            prefs[KEY_AUTH_TOKEN] = UUID.randomUUID().toString()
            prefs[KEY_USER_NAME] = user.name
            prefs[KEY_USER_EMAIL] = user.email
            prefs[KEY_USER_JOINED_DATE] = user.joinedDate
            if (user.avatarUrl.isNullOrBlank()) {
                prefs.remove(KEY_USER_AVATAR)
            } else {
                prefs[KEY_USER_AVATAR] = user.avatarUrl
            }
        }
    }

    private fun Preferences.toUser(): User? {
        val name = this[KEY_USER_NAME] ?: return null
        val email = this[KEY_USER_EMAIL] ?: return null
        val joinedDate = this[KEY_USER_JOINED_DATE] ?: DEFAULT_JOINED_DATE
        val avatar = this[KEY_USER_AVATAR]?.takeIf { it.isNotBlank() }
        return User(
            name = name,
            email = email,
            avatarUrl = avatar,
            joinedDate = joinedDate
        )
    }

    private fun Preferences.toRegisteredUser(): User? {
        val name = this[KEY_REGISTERED_USER_NAME] ?: return null
        val email = this[KEY_REGISTERED_EMAIL] ?: return null
        val joinedDate = this[KEY_REGISTERED_JOINED_DATE] ?: DEFAULT_JOINED_DATE
        val avatar = this[KEY_REGISTERED_AVATAR]
        return User(name = name, email = email, avatarUrl = avatar, joinedDate = joinedDate)
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return digest.joinToString("") { byte -> "%02x".format(byte) }
    }

    companion object {
        private const val DEFAULT_JOINED_DATE = "2024"
        private const val DEFAULT_DEMO_PASSWORD = "DriveNext123"
        private val DEFAULT_DEMO_USER = User(
            name = "Alexey Vlasov",
            email = "demo@drivenext.ru",
            avatarUrl = null,
            joinedDate = DEFAULT_JOINED_DATE
        )

        private val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")

        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        private val KEY_USER_JOINED_DATE = stringPreferencesKey("user_joined_date")
        private val KEY_USER_AVATAR = stringPreferencesKey("user_avatar")

        private val KEY_REGISTERED_EMAIL = stringPreferencesKey("registered_email")
        private val KEY_REGISTERED_PASSWORD = stringPreferencesKey("registered_password")
        private val KEY_REGISTERED_USER_NAME = stringPreferencesKey("registered_user_name")
        private val KEY_REGISTERED_JOINED_DATE = stringPreferencesKey("registered_joined_date")
        private val KEY_REGISTERED_AVATAR = stringPreferencesKey("registered_avatar")

        private val KEY_THEME = stringPreferencesKey("app_theme")
        private val KEY_NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    }
}
