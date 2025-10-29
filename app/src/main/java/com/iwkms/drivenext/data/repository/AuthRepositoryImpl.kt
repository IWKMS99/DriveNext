package com.iwkms.drivenext.data.repository

import com.iwkms.drivenext.data.supabase.SupabaseClient
import com.iwkms.drivenext.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    override suspend fun logOut() {
        try {
            SupabaseClient.auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}