package com.iwkms.drivenext.presentation.auth.ui

import android.Manifest
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentRegistrationStep3Binding
import com.iwkms.drivenext.presentation.auth.viewmodel.RegistrationViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.core.net.toUri

private enum class ImageType {
    PROFILE, LICENSE, PASSPORT
}

class RegistrationStep3Fragment : Fragment() {

    private var _binding: FragmentRegistrationStep3Binding? = null
    private val binding get() = _binding!!

    private val viewModel: RegistrationViewModel by navGraphViewModels(R.id.nav_graph)
    private val calendar: Calendar = Calendar.getInstance()

    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestGalleryPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>

    private var tempImageUri: Uri? = null
    private var currentImageType: ImageType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeLaunchers()
    }

    private fun initializeLaunchers() {
        requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                takePicture()
            } else {
                Toast.makeText(requireContext(), "Для использования камеры необходимо разрешение", Toast.LENGTH_LONG).show()
            }
        }

        requestGalleryPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                selectImageFromGallery()
            } else {
                Toast.makeText(requireContext(), "Для доступа к галерее необходимо разрешение", Toast.LENGTH_LONG).show()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegistrationStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restoreState()
        setupListeners()
        setupObservers()
    }

    private fun restoreState() {
        binding.etLicenseNumber.setText(viewModel.licenseNumber.value)
        binding.etIssueDate.setText(viewModel.issueDate.value)
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.etLicenseNumber.doOnTextChanged { text, _, _, _ -> viewModel.licenseNumber.value = text.toString() }
        binding.etIssueDate.setOnClickListener { showDatePickerDialog() }

        binding.ivAddPhoto.setOnClickListener {
            currentImageType = ImageType.PROFILE
            showImageSourceDialog()
        }
        binding.btnUploadLicense.setOnClickListener {
            currentImageType = ImageType.LICENSE
            showImageSourceDialog()
        }
        binding.btnUploadPassport.setOnClickListener {
            currentImageType = ImageType.PASSPORT
            showImageSourceDialog()
        }

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_registrationStep3Fragment_to_registrationSuccessFragment)
        }

    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Сделать фото", "Выбрать из галереи")
        AlertDialog.Builder(requireContext())
            .setTitle("Выберите источник")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> requestCameraPermission()
                    1 -> requestGalleryPermission()
                }
                dialog.dismiss()
            }
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
        val tempFile = File.createTempFile("temp_image", ".jpg", requireContext().cacheDir).apply {
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
        when (currentImageType) {
            ImageType.PROFILE -> viewModel.profilePhotoUri.value = uri.toString()
            ImageType.LICENSE -> viewModel.licensePhotoUri.value = uri.toString()
            ImageType.PASSPORT -> viewModel.passportPhotoUri.value = uri.toString()
            null -> {}
        }
    }

    private fun setupObservers() {
        viewModel.step3ValidationState.observe(viewLifecycleOwner) { state ->
            binding.btnNext.isEnabled = state.isNextButtonEnabled
        }

        viewModel.profilePhotoUri.observe(viewLifecycleOwner) { uriString ->
            if (uriString != null) {
                binding.ivAddPhoto.load(uriString.toUri()) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            } else {
                binding.ivAddPhoto.setImageResource(R.drawable.ic_add_a_photo)
            }
        }

        viewModel.licensePhotoUri.observe(viewLifecycleOwner) { uri ->
            updateUploadButtonState(binding.btnUploadLicense, uri != null)
        }

        viewModel.passportPhotoUri.observe(viewLifecycleOwner) { uri ->
            updateUploadButtonState(binding.btnUploadPassport, uri != null)
        }
    }

    private fun updateUploadButtonState(button: View, isUploaded: Boolean) {
        if (isUploaded) {
            button.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_upload_button_border_success)
        } else {
            button.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_upload_button_border)
        }
    }

    private fun showDatePickerDialog() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)
            binding.etIssueDate.setText(formattedDate)
            viewModel.issueDate.value = formattedDate
        }

        DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}