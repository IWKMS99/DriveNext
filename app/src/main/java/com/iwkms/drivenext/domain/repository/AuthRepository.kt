package com.iwkms.drivenext.domain.repository

interface AuthRepository {
    suspend fun logOut()
}