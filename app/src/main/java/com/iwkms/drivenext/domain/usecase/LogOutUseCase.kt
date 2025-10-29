package com.iwkms.drivenext.domain.usecase

import com.iwkms.drivenext.domain.repository.AuthRepository

class LogOutUseCase(private val authRepository: AuthRepository) {
    suspend fun execute() {
        authRepository.logOut()
    }
}