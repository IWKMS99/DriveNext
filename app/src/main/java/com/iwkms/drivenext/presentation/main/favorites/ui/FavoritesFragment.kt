package com.iwkms.drivenext.presentation.main.favorites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.iwkms.drivenext.databinding.FragmentFavoritesBinding
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding

class FavoritesFragment  : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.applyStatusBarPadding()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
