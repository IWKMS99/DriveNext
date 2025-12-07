package com.iwkms.drivenext.domain.repository

import com.iwkms.drivenext.domain.model.Booking

interface BookingRepository {
    fun getMyBookings(): List<Booking>
    fun getBookingById(id: String): Booking?
    fun createBooking(booking: Booking)
    fun cancelBooking(id: String)
}