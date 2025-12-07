package com.iwkms.drivenext.presentation.main.home.ui

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.iwkms.drivenext.R
import com.iwkms.drivenext.data.repository.FakeCarRepository
import com.iwkms.drivenext.data.repository.SessionRepositoryProvider
import com.iwkms.drivenext.databinding.FragmentBookingBinding
import com.iwkms.drivenext.domain.model.Booking
import com.iwkms.drivenext.domain.model.BookingStatus
import com.iwkms.drivenext.domain.model.Car
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding
import java.util.Date
import java.util.UUID
import java.util.concurrent.TimeUnit

class BookingFragment : Fragment() {

    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    private val args: BookingFragmentArgs by navArgs()
    private val carRepository = FakeCarRepository()
    private val bookingRepository by lazy { SessionRepositoryProvider.getBookingRepository() }

    private var selectedStartDate: Long? = null
    private var selectedEndDate: Long? = null
    private var currentCar: Car? = null

    private val INSURANCE_PER_DAY = 300
    private val DEPOSIT = 15000

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
        binding.root.applyStatusBarPadding()

        val car = carRepository.getCarById(args.carId)
        if (car == null) {
            findNavController().navigateUp()
            return
        }
        currentCar = car

        setupUI(car)
        setupListeners()
    }

    private fun setupUI(car: Car) {
        binding.tvCarBrand.text = car.brand
        binding.tvCarModel.text = car.model
        binding.tvPricePerDay.text = getString(R.string.car_card_price_per_day, car.pricePerDay)
        binding.tvAddress.text = car.address
        binding.tvDeposit.text = getString(R.string.price_format, DEPOSIT)

        binding.ivCarThumb.load(car.mainImage.ifEmpty { R.drawable.ic_car_loader }) {
            crossfade(true)
        }

        binding.tvRentPrice.text = "-"
        binding.tvInsurancePrice.text = "-"
        binding.tvTotalPrice.text = "-"
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.etDates.setOnClickListener { showRangePicker() }

        binding.btnContinue.setOnClickListener { createBooking() }
    }

    private fun showRangePicker() {
        val constraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
            .build()

        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText(getString(R.string.booking_dates_placeholder))
            .setCalendarConstraints(constraints)
            .build()

        picker.addOnPositiveButtonClickListener { selection ->
            selectedStartDate = selection.first
            selectedEndDate = selection.second
            updateDatesAndPrice()
        }

        picker.show(parentFragmentManager, "date_picker")
    }

    private fun updateDatesAndPrice() {
        if (selectedStartDate == null || selectedEndDate == null) return

        val startStr = DateFormat.format("dd.MM", Date(selectedStartDate!!))
        val endStr = DateFormat.format("dd.MM.yyyy", Date(selectedEndDate!!))
        binding.etDates.setText("$startStr - $endStr")

        val diffMs = selectedEndDate!! - selectedStartDate!!
        val days = TimeUnit.MILLISECONDS.toDays(diffMs).toInt() + 1

        val carPrice = currentCar?.pricePerDay ?: 0
        val rentTotal = carPrice * days
        val insuranceTotal = INSURANCE_PER_DAY * days
        val total = rentTotal + insuranceTotal

        binding.tvRentLabel.text = getString(R.string.booking_price_rent, days)
        binding.tvRentPrice.text = getString(R.string.price_format, rentTotal)

        binding.tvInsuranceLabel.text = getString(R.string.booking_price_insurance, days)
        binding.tvInsurancePrice.text = getString(R.string.price_format, insuranceTotal)

        binding.tvTotalPrice.text = getString(R.string.price_format, total)

        binding.btnContinue.isEnabled = true
    }

    private fun createBooking() {
        val car = currentCar ?: return
        val start = selectedStartDate ?: return
        val end = selectedEndDate ?: return

        val days = TimeUnit.MILLISECONDS.toDays(end - start).toInt() + 1
        val total = (car.pricePerDay * days) + (INSURANCE_PER_DAY * days)

        val booking = Booking(
            id = UUID.randomUUID().toString().substring(0, 8).uppercase(),
            carId = car.id,
            carName = "${car.brand} ${car.model}",
            carImage = car.mainImage,
            startDate = start,
            endDate = end,
            totalPrice = total,
            status = BookingStatus.ACTIVE
        )

        bookingRepository.createBooking(booking)

        findNavController().navigate(R.id.action_bookingFragment_to_bookingSuccessFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}