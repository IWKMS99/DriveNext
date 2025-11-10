package com.iwkms.drivenext.data.repository

import android.content.Context
import com.iwkms.drivenext.domain.repository.SessionRepository

object SessionRepositoryProvider {

    @Volatile
    private var instance: SessionRepository? = null

    fun get(context: Context): SessionRepository {
        return instance ?: synchronized(this) {
            instance ?: SessionRepositoryImpl(context.applicationContext).also { instance = it }
        }
    }
}
