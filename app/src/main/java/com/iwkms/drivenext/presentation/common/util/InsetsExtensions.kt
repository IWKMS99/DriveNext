package com.iwkms.drivenext.presentation.common.util

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

data class InitialPadding(val left: Int, val top: Int, val right: Int, val bottom: Int)

fun View.applyStatusBarPadding() {
    val initialPadding = InitialPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val statusBars = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
        view.setPadding(
            initialPadding.left,
            initialPadding.top + statusBars,
            initialPadding.right,
            initialPadding.bottom
        )
        insets
    }
    ViewCompat.requestApplyInsets(this)
}
