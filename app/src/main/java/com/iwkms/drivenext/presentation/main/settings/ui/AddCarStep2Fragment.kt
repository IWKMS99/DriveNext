package com.iwkms.drivenext.presentation.main.settings.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentAddCarStep2Binding
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding
import com.iwkms.drivenext.presentation.main.settings.viewmodel.AddCarViewModel

class AddCarStep2Fragment : Fragment(R.layout.fragment_add_car_step2) {

    private val viewModel: AddCarViewModel by navGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddCarStep2Binding.bind(view)
        binding.root.applyStatusBarPadding()

        val transmissions = listOf("А/Т", "М/Т", "Робот", "Вариатор")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, transmissions)
        binding.etTransmission.setAdapter(adapter)

        binding.etYear.setText(viewModel.year.value)
        binding.etBrand.setText(viewModel.brand.value)
        binding.etModel.setText(viewModel.model.value)
        binding.etTransmission.setText(viewModel.transmission.value, false)
        binding.etMileage.setText(viewModel.mileage.value)
        binding.etDescription.setText(viewModel.description.value)

        val textWatcher = {
            viewModel.year.value = binding.etYear.text.toString()
            viewModel.brand.value = binding.etBrand.text.toString()
            viewModel.model.value = binding.etModel.text.toString()
            viewModel.transmission.value = binding.etTransmission.text.toString()
            viewModel.mileage.value = binding.etMileage.text.toString()
            viewModel.description.value = binding.etDescription.text.toString()
            binding.btnSend.isEnabled = viewModel.isStep2Valid()
        }

        binding.etYear.doOnTextChanged { _, _, _, _ -> textWatcher() }
        binding.etBrand.doOnTextChanged { _, _, _, _ -> textWatcher() }
        binding.etModel.doOnTextChanged { _, _, _, _ -> textWatcher() }
        binding.etTransmission.doOnTextChanged { _, _, _, _ -> textWatcher() }
        binding.etMileage.doOnTextChanged { _, _, _, _ -> textWatcher() }
        binding.etDescription.doOnTextChanged { _, _, _, _ -> textWatcher() }

        binding.btnSend.setOnClickListener {
            findNavController().navigate(R.id.action_addCarStep2Fragment_to_addCarPhotosFragment)
        }
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }
}