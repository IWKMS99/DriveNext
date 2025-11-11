package com.iwkms.drivenext.presentation.main.home.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.iwkms.drivenext.R
import com.iwkms.drivenext.data.repository.FakeCarRepository
import com.iwkms.drivenext.databinding.FragmentCarDetailsBinding
import com.iwkms.drivenext.domain.repository.CarRepository
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding

class CarDetailsFragment : Fragment() {

    private var _binding: FragmentCarDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: CarDetailsFragmentArgs by navArgs()
    private val carRepository: CarRepository = FakeCarRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
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

        binding.btnBook.setOnClickListener {
            val action = CarDetailsFragmentDirections.actionCarDetailsFragmentToBookingFragment(car.id)
            findNavController().navigate(action)
        }

        binding.tvCarName.text = "${car.brand} ${car.model}"
        binding.tvCarPrice.text = getString(R.string.car_card_price_per_day, car.pricePerDay)
        binding.tvCarSpecs.text = getString(
            R.string.car_details_specs,
            car.transmission,
            car.fuelType
        )
        binding.tvDescription.text = getString(
            R.string.car_details_placeholder_description,
            car.model
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
