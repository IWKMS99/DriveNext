package com.iwkms.drivenext.domain.model

enum class BookingStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED
}

data class Booking(
    val id: String,
    val carId: Int,
    val carName: String,
    val carImage: String,
    val startDate: Long,
    val endDate: Long,
    val totalPrice: Int,
    val status: BookingStatus
)