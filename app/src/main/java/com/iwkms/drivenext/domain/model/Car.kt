package com.iwkms.drivenext.domain.model

data class Car(
    val id: Int,
    val imageUrl: String, // Пока будет ссылка на drawable, потом на URL
    val model: String,
    val brand: String,
    val pricePerDay: String,
    val transmission: String,
    val fuelType: String
)