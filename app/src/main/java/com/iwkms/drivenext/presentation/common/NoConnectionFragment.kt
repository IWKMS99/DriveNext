package com.iwkms.drivenext.presentation.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentNoConnectionBinding
import com.iwkms.drivenext.presentation.common.util.NetworkUtils
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding

class NoConnectionFragment : Fragment() {

    private var _binding: FragmentNoConnectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.applyStatusBarPadding()

        binding.btnRetry.setOnClickListener {
            if (NetworkUtils.isOnline(requireContext())) {
                findNavController().navigateUp()
            } else {
                Snackbar.make(
                    binding.root,
                    getString(R.string.no_connection_retry_error),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
