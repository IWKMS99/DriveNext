package com.iwkms.drivenext.presentation.main.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.iwkms.drivenext.R
import com.iwkms.drivenext.data.repository.FakeCarRepository
import com.iwkms.drivenext.databinding.FragmentCarDetailsBinding
import com.iwkms.drivenext.domain.model.Car
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.applyStatusBarPadding()

        val car = carRepository.getCarById(args.carId)
        if (car == null) {
            findNavController().navigateUp()
            return
        }

        setupUI(car)
        setupListeners(car)
    }

    private fun setupUI(car: Car) {
        binding.tvCarName.text = "${car.brand} ${car.model}"
        binding.tvAddress.text = car.address
        binding.tvDescription.text = car.description

        binding.tvPrice.text = getString(R.string.car_card_price_per_day, car.pricePerDay)

        binding.ivCarImage.load(car.mainImage.ifEmpty { R.drawable.ic_car_loader }) {
            crossfade(true)
            error(R.drawable.ic_car_loader)
        }

        updateFavoriteIcon(car.isFavorite)
    }

    private fun setupListeners(car: Car) {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnFavorite.setOnClickListener {
            val newState = carRepository.toggleFavorite(car.id)
            updateFavoriteIcon(newState)
        }

        binding.btnBook.setOnClickListener {
            val action = CarDetailsFragmentDirections.actionCarDetailsFragmentToBookingFragment(car.id)
            findNavController().navigate(action)
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconRes = if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_border
        binding.btnFavorite.setImageResource(iconRes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}