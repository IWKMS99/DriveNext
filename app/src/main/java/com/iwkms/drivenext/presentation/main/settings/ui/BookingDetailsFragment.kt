package com.iwkms.drivenext.presentation.main.settings.ui

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.iwkms.drivenext.R
import com.iwkms.drivenext.data.repository.FakeCarRepository
import com.iwkms.drivenext.data.repository.SessionRepositoryProvider
import com.iwkms.drivenext.databinding.FragmentBookingDetailsBinding
import com.iwkms.drivenext.domain.model.Booking
import com.iwkms.drivenext.domain.model.BookingStatus
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

class BookingDetailsFragment : Fragment() {

    private var _binding: FragmentBookingDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: BookingDetailsFragmentArgs by navArgs()
    private val bookingRepository by lazy { SessionRepositoryProvider.getBookingRepository() }
    private val carRepository = FakeCarRepository()
    private val sessionRepository by lazy { SessionRepositoryProvider.get(requireContext().applicationContext) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.applyStatusBarPadding()

        val booking = bookingRepository.getBookingById(args.bookingId)
        if (booking == null) {
            findNavController().navigateUp()
            return
        }

        setupUI(booking)
        setupListeners(booking)
    }

    private fun setupUI(booking: Booking) {
        val car = carRepository.getCarById(booking.carId)

        binding.tvTitle.text = getString(R.string.booking_details_title, booking.id)

        binding.tvCarBrand.text = car?.brand ?: ""
        binding.tvCarModel.text = car?.model ?: booking.carName
        binding.tvPricePerDay.text = getString(R.string.car_card_price_per_day, car?.pricePerDay ?: 0)
        binding.ivCarThumb.load(booking.carImage.ifEmpty { R.drawable.ic_car_loader })

        binding.tvAddress.text = car?.address ?: "N/A"

        val startStr = DateFormat.format("dd.MM.yyyy", Date(booking.startDate))
        val endStr = DateFormat.format("dd.MM.yyyy", Date(booking.endDate))
        binding.tvDates.text = getString(R.string.booking_dates_format, startStr, endStr)

        viewLifecycleOwner.lifecycleScope.launch {
            val user = sessionRepository.userFlow.first()
            binding.tvDriverName.text = user?.name ?: "Unknown"
            binding.tvDriverLicense.text = "**********"
        }

        val statusRes = when(booking.status) {
            BookingStatus.ACTIVE -> R.string.booking_status_active
            BookingStatus.COMPLETED -> R.string.booking_status_completed
            BookingStatus.CANCELLED -> R.string.booking_status_cancelled
        }
        binding.tvStatus.setText(statusRes)

        binding.tvTotalPrice.text = getString(R.string.price_format, booking.totalPrice)

        binding.btnCancel.isVisible = booking.status == BookingStatus.ACTIVE
    }

    private fun setupListeners(booking: Booking) {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnCancel.setOnClickListener {
            bookingRepository.cancelBooking(booking.id)
            Toast.makeText(context, R.string.booking_cancelled_success, Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}