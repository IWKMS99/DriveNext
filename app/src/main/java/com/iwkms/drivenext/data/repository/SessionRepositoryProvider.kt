package com.iwkms.drivenext.data.repository

import android.content.Context
import com.iwkms.drivenext.domain.repository.BookingRepository
import com.iwkms.drivenext.domain.repository.SessionRepository

object SessionRepositoryProvider {

    @Volatile
    private var sessionInstance: SessionRepository? = null

    @Volatile
    private var bookingInstance: BookingRepository? = null

    fun get(context: Context): SessionRepository {
        return sessionInstance ?: synchronized(this) {
            sessionInstance ?: SessionRepositoryImpl(context.applicationContext).also { sessionInstance = it }
        }
    }

    fun getBookingRepository(): BookingRepository {
        return bookingInstance ?: synchronized(this) {
            bookingInstance ?: BookingRepositoryImpl().also { bookingInstance = it }
        }
    }
}