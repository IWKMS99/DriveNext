package com.iwkms.drivenext.data.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth

object SupabaseClient {

    private const val SUPABASE_URL = "https://vwmvzlqrqmbrlyocxugl.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZ3bXZ6bHFycW1icmx5b2N4dWdsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjE3NTc4ODQsImV4cCI6MjA3NzMzMzg4NH0.28eFzQSWdJZvP4V-4s3wW18vSMqLCLLTBnN76srIltw"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth)
    }

    val auth: Auth
        get() = client.auth
}