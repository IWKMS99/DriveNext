package com.iwkms.drivenext.domain.model

data class Car(
    val id: Int,
    val brand: String,
    val model: String,
    val pricePerDay: Int,
    val transmission: String,
    val fuelType: String,
    val description: String,
    val address: String,
    val rating: Double,
    val reviewCount: Int,
    val mainImage: String,
    val images: List<String>,
    var isFavorite: Boolean = false
)