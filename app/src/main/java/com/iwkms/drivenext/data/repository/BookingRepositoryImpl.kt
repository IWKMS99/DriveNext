package com.iwkms.drivenext.data.repository

import com.iwkms.drivenext.data.storage.MockDatabase
import com.iwkms.drivenext.domain.model.Booking
import com.iwkms.drivenext.domain.model.BookingStatus
import com.iwkms.drivenext.domain.repository.BookingRepository

class BookingRepositoryImpl : BookingRepository {

    override fun getMyBookings(): List<Booking> {
        return MockDatabase.bookings.sortedByDescending { it.startDate }
    }

    override fun getBookingById(id: String): Booking? {
        return MockDatabase.bookings.find { it.id == id }
    }

    override fun createBooking(booking: Booking) {
        MockDatabase.bookings.add(0, booking)
    }

    override fun cancelBooking(id: String) {
        val index = MockDatabase.bookings.indexOfFirst { it.id == id }
        if (index != -1) {
            val oldBooking = MockDatabase.bookings[index]
            MockDatabase.bookings[index] = oldBooking.copy(status = BookingStatus.CANCELLED)
        }
    }
}