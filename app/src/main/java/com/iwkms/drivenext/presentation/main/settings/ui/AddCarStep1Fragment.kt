package com.iwkms.drivenext.presentation.main.settings.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentAddCarStep1Binding
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding
import com.iwkms.drivenext.presentation.main.settings.viewmodel.AddCarViewModel

class AddCarStep1Fragment : Fragment(R.layout.fragment_add_car_step1) {

    private val viewModel: AddCarViewModel by navGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddCarStep1Binding.bind(view)
        binding.root.applyStatusBarPadding()

        binding.etAddress.setText(viewModel.address.value)

        binding.etAddress.doOnTextChanged { text, _, _, _ ->
            viewModel.address.value = text.toString()
            binding.btnNext.isEnabled = text.isNullOrBlank().not()
        }

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_addCarStep1Fragment_to_addCarStep2Fragment)
        }
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }
}