package com.iwkms.drivenext.presentation.common.theme

import androidx.appcompat.app.AppCompatDelegate
import com.iwkms.drivenext.domain.model.AppTheme

object ThemeManager {

    fun apply(theme: AppTheme) {
        val mode = when (theme) {
            AppTheme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            AppTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            AppTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        }
        if (AppCompatDelegate.getDefaultNightMode() != mode) {
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }
}
