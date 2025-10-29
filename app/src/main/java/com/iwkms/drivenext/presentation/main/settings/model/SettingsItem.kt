package com.iwkms.drivenext.presentation.main.settings.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SettingsItem(
    @DrawableRes val iconResId: Int,
    @StringRes val titleResId: Int
)