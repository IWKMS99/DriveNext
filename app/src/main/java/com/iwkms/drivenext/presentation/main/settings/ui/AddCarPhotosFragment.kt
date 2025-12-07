package com.iwkms.drivenext.presentation.main.settings.ui

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import coil.load
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.FragmentAddCarPhotosBinding
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding
import com.iwkms.drivenext.presentation.main.settings.viewmodel.AddCarViewModel
import androidx.core.net.toUri

class AddCarPhotosFragment : Fragment(R.layout.fragment_add_car_photos) {

    private val viewModel: AddCarViewModel by navGraphViewModels(R.id.nav_graph)
    private var _binding: FragmentAddCarPhotosBinding? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.addPhoto(it.toString()) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddCarPhotosBinding.bind(view)
        _binding!!.root.applyStatusBarPadding()

        _binding!!.cvMainPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        viewModel.photos.observe(viewLifecycleOwner) { photos ->
            val count = photos.size
            _binding!!.tvLimit.text = getString(R.string.add_car_photos_limit, count)
            _binding!!.btnNext.isEnabled = count > 0

            if (count > 0) {
                // Main Photo
                _binding!!.ivMainPhoto.load(photos[0].toUri()) {
                    crossfade(true)
                }
                _binding!!.ivMainPhoto.setPadding(0, 0, 0, 0)
            }

            _binding!!.ivThumb1.isVisible = count > 1
            if (count > 1) _binding!!.ivThumb1.load(photos[1].toUri())

            _binding!!.ivThumb2.isVisible = count > 2
            if (count > 2) _binding!!.ivThumb2.load(photos[2].toUri())

            _binding!!.ivThumb3.isVisible = count > 3
            if (count > 3) _binding!!.ivThumb3.load(photos[3].toUri())
        }

        _binding!!.btnNext.setOnClickListener {
            viewModel.saveCar()
            findNavController().navigate(R.id.action_addCarPhotosFragment_to_addCarSuccessFragment)
        }
        _binding!!.btnBack.setOnClickListener { findNavController().navigateUp() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}