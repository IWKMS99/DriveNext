package com.iwkms.drivenext.presentation.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentAuthBinding
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.applyStatusBarPadding()

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_authFragment_to_loginFragment)
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_authFragment_to_registrationStep1Fragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
