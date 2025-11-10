package com.iwkms.drivenext.presentation.main.settings.ui

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentProfileBinding
import com.iwkms.drivenext.presentation.main.settings.viewmodel.ProfileViewModel
import com.iwkms.drivenext.presentation.main.settings.viewmodel.ProfileViewModelFactory
import java.io.File

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(requireContext().applicationContext)
    }

    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestGalleryPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>

    private var tempImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeLaunchers()
    }

    private fun initializeLaunchers() {
        requestCameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    takePicture()
                } else {
                    showPermissionError(R.string.profile_camera_permission_denied)
                }
            }

        requestGalleryPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    selectImageFromGallery()
                } else {
                    showPermissionError(R.string.profile_gallery_permission_denied)
                }
            }

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { onImagePicked(it) }
        }

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                tempImageUri?.let { onImagePicked(it) }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnLogout.setOnClickListener {
            viewModel.onLogOutClicked()
        }

        binding.ivAvatar.setOnClickListener {
            showImageSourceDialog()
        }
    }

    private fun setupObservers() {
        viewModel.navigateToAuth.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                findNavController().navigate(R.id.action_global_authFragment)
                viewModel.onNavigationComplete()
            }
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.tvUserName.text = user.name
            binding.tvJoinedDate.text = getString(R.string.profile_joined_date, user.joinedDate)
            binding.tvUserEmail.text = user.email
            binding.ivAvatar.load(user.avatarUrl ?: R.drawable.ic_profile_placeholder) {
                crossfade(true)
                transformations(CircleCropTransformation())
                error(R.drawable.ic_profile_placeholder)
            }
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf(
            getString(R.string.profile_photo_camera),
            getString(R.string.profile_photo_gallery)
        )
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.profile_photo_dialog_title)
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> requestCameraPermission()
                    1 -> requestGalleryPermission()
                }
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun requestGalleryPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        requestGalleryPermissionLauncher.launch(permission)
    }

    private fun requestCameraPermission() {
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun selectImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun takePicture() {
        createTempImageUri().let { uri ->
            tempImageUri = uri
            takePictureLauncher.launch(uri)
        }
    }

    private fun createTempImageUri(): Uri {
        val tempFile = File.createTempFile("temp_avatar", ".jpg", requireContext().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            tempFile
        )
    }

    private fun onImagePicked(uri: Uri) {
        viewModel.updateAvatar(uri)
    }

    private fun showPermissionError(messageResId: Int) {
        AlertDialog.Builder(requireContext())
            .setMessage(messageResId)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
