package com.iwkms.drivenext.presentation.main.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iwkms.drivenext.data.repository.SessionRepositoryProvider
import com.iwkms.drivenext.databinding.FragmentMyBookingsBinding
import com.iwkms.drivenext.presentation.common.util.applyStatusBarPadding
import com.iwkms.drivenext.presentation.main.settings.adapter.BookingsAdapter

class MyBookingsFragment : Fragment() {
    private var _binding: FragmentMyBookingsBinding? = null
    private val binding get() = _binding!!

    private val bookingRepository by lazy { SessionRepositoryProvider.getBookingRepository() }
    private lateinit var adapter: BookingsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMyBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.applyStatusBarPadding()

        setupRecyclerView()

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onResume() {
        super.onResume()
        loadBookings()
    }

    private fun setupRecyclerView() {
        adapter = BookingsAdapter { booking ->
            val action = MyBookingsFragmentDirections.actionMyBookingsFragmentToBookingDetailsFragment(booking.id)
            findNavController().navigate(action)
        }
        binding.rvBookings.adapter = adapter
    }

    private fun loadBookings() {
        val bookings = bookingRepository.getMyBookings()
        adapter.submitList(bookings)
        binding.rvBookings.isVisible = bookings.isNotEmpty()
        binding.tvEmpty.isVisible = bookings.isEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}