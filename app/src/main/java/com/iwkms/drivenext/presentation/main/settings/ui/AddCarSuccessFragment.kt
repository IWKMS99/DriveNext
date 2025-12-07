package com.iwkms.drivenext.presentation.main.settings.ui

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentAddCarSuccessBinding
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding

class AddCarSuccessFragment : Fragment(R.layout.fragment_add_car_success) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddCarSuccessBinding.bind(view)
        binding.root.applyStatusBarPadding()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateToHome()
        }

        binding.btnHome.setOnClickListener {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_addCarSuccessFragment_to_homeFragment)
    }
}