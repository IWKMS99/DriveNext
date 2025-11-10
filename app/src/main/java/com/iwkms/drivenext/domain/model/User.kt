package com.iwkms.drivenext.domain.model

data class User(
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val joinedDate: String
)