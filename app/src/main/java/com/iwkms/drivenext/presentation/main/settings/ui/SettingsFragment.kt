package com.iwkms.drivenext.presentation.main.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentSettingsBinding
import com.iwkms.drivenext.domain.model.AppTheme
import com.iwkms.drivenext.presentation.main.settings.adapter.SettingsAdapter
import com.iwkms.drivenext.presentation.main.settings.model.SettingsItem
import com.iwkms.drivenext.presentation.main.settings.viewmodel.SettingsViewModel
import com.iwkms.drivenext.presentation.main.settings.viewmodel.SettingsViewModelFactory

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(requireContext().applicationContext)
    }
    private lateinit var settingsAdapter: SettingsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        setupObservers()
    }

    private fun setupRecyclerView() {
        settingsAdapter = SettingsAdapter { item -> handleSettingClick(item) }
        binding.rvSettings.apply {
            adapter = settingsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun handleSettingClick(item: SettingsItem) {
        when (item.titleResId) {
            R.string.settings_my_bookings -> findNavController().navigate(R.id.action_settingsFragment_to_myBookingsFragment)
            R.string.settings_connect_car -> findNavController().navigate(R.id.action_settingsFragment_to_connectCarFragment)
            R.string.settings_theme -> showThemeDialog()
            R.string.settings_notifications -> showNotificationsDialog()
            else -> Unit
        }
    }

    private fun showThemeDialog() {
        val options = AppTheme.entries.toTypedArray()
        val labels = options.map { getString(it.toLabelRes()) }.toTypedArray()
        val selectedIndex = options.indexOf(viewModel.theme.value ?: AppTheme.SYSTEM)
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.settings_theme)
            .setSingleChoiceItems(labels, selectedIndex) { dialog, which ->
                viewModel.updateTheme(options[which])
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun showNotificationsDialog() {
        val enabled = viewModel.notificationsState.value ?: true
        val items = arrayOf(
            getString(R.string.settings_notifications_on),
            getString(R.string.settings_notifications_off)
        )
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.settings_notifications)
            .setSingleChoiceItems(items, if (enabled) 0 else 1) { dialog, which ->
                viewModel.updateNotifications(which == 0)
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun setupListeners() {
        binding.profileHeader.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_profileFragment)
        }
    }

    private fun setupObservers() {
        viewModel.settingsItems.observe(viewLifecycleOwner) { items ->
            settingsAdapter.submitList(items)
        }
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.tvUserName.text = user.name
            binding.tvUserEmail.text = user.email
            binding.ivAvatar.load(user.avatarUrl ?: R.drawable.ic_profile_placeholder) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun AppTheme.toLabelRes(): Int = when (this) {
        AppTheme.SYSTEM -> R.string.theme_system
        AppTheme.LIGHT -> R.string.theme_light
        AppTheme.DARK -> R.string.theme_dark
    }
}
