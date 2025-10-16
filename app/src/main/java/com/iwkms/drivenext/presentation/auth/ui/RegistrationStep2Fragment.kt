package com.iwkms.drivenext.presentation.auth.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentRegistrationStep2Binding
import com.iwkms.drivenext.presentation.auth.viewmodel.RegistrationViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegistrationStep2Fragment : Fragment() {

    private var _binding: FragmentRegistrationStep2Binding? = null
    private val binding get() = _binding!!

    private val viewModel: RegistrationViewModel by navGraphViewModels(R.id.nav_graph)

    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restoreState()
        setupListeners()
        setupObservers()
    }

    private fun restoreState() {
        binding.etLastname.setText(viewModel.lastName.value)
        binding.etFirstname.setText(viewModel.firstName.value)
        binding.etMiddlename.setText(viewModel.middleName.value)
        binding.etBirthdate.setText(viewModel.birthDate.value)
        viewModel.selectedGenderId.value?.let { binding.rgGender.check(it) }
    }

    private fun setupListeners() {
        binding.etLastname.doOnTextChanged { text, _, _, _ -> viewModel.lastName.value = text.toString() }
        binding.etFirstname.doOnTextChanged { text, _, _, _ -> viewModel.firstName.value = text.toString() }
        binding.etMiddlename.doOnTextChanged { text, _, _, _ -> viewModel.middleName.value = text.toString() }

        binding.rgGender.setOnCheckedChangeListener { _, checkedId ->
            viewModel.selectedGenderId.value = checkedId
        }

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_registrationStep2Fragment_to_registrationStep3Fragment)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.etBirthdate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun setupObservers() {
        viewModel.step2ValidationState.observe(viewLifecycleOwner) { state ->
            binding.btnNext.isEnabled = state.isNextButtonEnabled
        }
    }

    private fun showDatePickerDialog() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)

            binding.etBirthdate.setText(formattedDate)
            viewModel.birthDate.value = formattedDate
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