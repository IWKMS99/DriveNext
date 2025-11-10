package com.iwkms.drivenext.domain.repository

import com.iwkms.drivenext.domain.model.Car

interface CarRepository {
    fun getCars(): List<Car>
    fun searchCars(query: String): List<Car>
    fun getCarById(id: Int): Car?
}
