package com.iwkms.drivenext.presentation.auth.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentRegistrationStep3Binding
import com.iwkms.drivenext.presentation.auth.viewmodel.RegistrationViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegistrationStep3Fragment : Fragment() {

    private var _binding: FragmentRegistrationStep3Binding? = null
    private val binding get() = _binding!!

    private val viewModel: RegistrationViewModel by navGraphViewModels(R.id.nav_graph)
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        binding.etLicenseNumber.doOnTextChanged { text, _, _, _ ->
            viewModel.licenseNumber.value = text.toString()
        }

        binding.etIssueDate.setOnClickListener { showDatePickerDialog() }

        binding.ivAddPhoto.setOnClickListener {
            // TODO: Реализовать выбор фото из галереи/камеры
            Toast.makeText(requireContext(), "Выбор фото профиля...", Toast.LENGTH_SHORT).show()
            viewModel.profilePhotoUri.value = "simulated_uri_profile.jpg"
        }

        binding.btnUploadLicense.setOnClickListener {
            // TODO: Реализовать выбор фото из галереи/камеры
            Toast.makeText(requireContext(), "Выбор фото ВУ...", Toast.LENGTH_SHORT).show()
            viewModel.licensePhotoUri.value = "simulated_uri_license.jpg"
        }

        binding.btnUploadPassport.setOnClickListener {
            // TODO: Реализовать выбор фото из галереи/камеры
            Toast.makeText(requireContext(), "Выбор фото паспорта...", Toast.LENGTH_SHORT).show()
            viewModel.passportPhotoUri.value = "simulated_uri_passport.jpg"
        }

        binding.btnNext.setOnClickListener {
            // TODO: Вызвать метод для отправки данных на сервер
            findNavController().navigate(R.id.action_registrationStep3Fragment_to_registrationSuccessFragment)
        }
    }

    private fun setupObservers() {
        viewModel.step3ValidationState.observe(viewLifecycleOwner) { state ->
            binding.btnNext.isEnabled = state.isNextButtonEnabled
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