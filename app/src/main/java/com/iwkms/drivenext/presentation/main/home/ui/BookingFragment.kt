package com.iwkms.drivenext.presentation.main.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.iwkms.drivenext.R
import com.iwkms.drivenext.data.repository.FakeCarRepository
import com.iwkms.drivenext.databinding.FragmentBookingBinding
import com.iwkms.drivenext.domain.repository.CarRepository
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding

class BookingFragment : Fragment() {

    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    private val args: BookingFragmentArgs by navArgs()
    private val carRepository: CarRepository = FakeCarRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val car = carRepository.getCarById(args.carId)
        if (car == null) {
            findNavController().navigateUp()
            return
        }

        binding.root.applyStatusBarPadding()

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvBookingTitle.text = getString(
            R.string.booking_title,
            car.brand,
            car.model
        )
        binding.tvBookingSummary.text = getString(
            R.string.booking_summary,
            car.brand,
            car.model,
            car.pricePerDay.toString()
        )

        binding.btnConfirm.setOnClickListener {
            Snackbar.make(
                binding.root,
                R.string.booking_coming_soon,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
