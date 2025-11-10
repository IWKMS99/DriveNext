package com.iwkms.drivenext.presentation.common

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var connectivityManager: ConnectivityManager? = null
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var isShowingNoConnection = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashScreenFragment,
                R.id.onboardingFragment,
                R.id.authFragment,
                R.id.loginFragment,
                R.id.registrationStep1Fragment,
                R.id.registrationStep2Fragment,
                R.id.registrationStep3Fragment,
                R.id.registrationSuccessFragment,
                R.id.loaderFragment,
                R.id.carDetailsFragment,
                R.id.bookingFragment,
                R.id.noConnectionFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                }

                else -> binding.bottomNavigation.visibility = if (isShowingNoConnection) View.GONE else View.VISIBLE
            }
        }

        monitorConnectivity()
    }

    @SuppressLint("MissingPermission")
    private fun monitorConnectivity() {
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                runOnUiThread { handleConnectivityChange(true) }
            }

            override fun onLost(network: Network) {
                runOnUiThread { handleConnectivityChange(false) }
            }
        }
        connectivityManager?.registerDefaultNetworkCallback(networkCallback!!)
    }

    private fun handleConnectivityChange(isConnected: Boolean) {
        if (!isConnected && !isShowingNoConnection) {
            navController.navigate(R.id.action_global_noConnectionFragment)
            binding.bottomNavigation.visibility = View.GONE
            isShowingNoConnection = true
        } else if (isConnected && isShowingNoConnection) {
            if (navController.currentDestination?.id == R.id.noConnectionFragment) {
                navController.popBackStack()
            }
            isShowingNoConnection = false
            binding.bottomNavigation.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkCallback?.let { connectivityManager?.unregisterNetworkCallback(it) }
    }
}
