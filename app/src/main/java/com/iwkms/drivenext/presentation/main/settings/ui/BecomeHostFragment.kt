package com.iwkms.drivenext.presentation.main.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentBecomeHostBinding
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding

class BecomeHostFragment : Fragment() {
    private var _binding: FragmentBecomeHostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBecomeHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.applyStatusBarPadding()

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnStart.setOnClickListener {
            findNavController().navigate(R.id.action_becomeHostFragment_to_addCarStep1Fragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}