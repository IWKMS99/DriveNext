package com.iwkms.drivenext.presentation.common.util

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission

object NetworkUtils {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}
