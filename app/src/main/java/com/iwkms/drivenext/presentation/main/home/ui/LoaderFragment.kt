package com.iwkms.drivenext.presentation.main.home.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.iwkms.drivenext.databinding.FragmentLoaderBinding
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding

class LoaderFragment : Fragment() {
    private var _binding: FragmentLoaderBinding? = null
    private val binding get() = _binding!!
    private val args: LoaderFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.applyStatusBarPadding()
        Handler(Looper.getMainLooper()).postDelayed({
            val action = LoaderFragmentDirections.actionLoaderFragmentToSearchResultFragment(args.searchQuery)
            findNavController().navigate(action)
        }, 1500)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
