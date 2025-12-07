package com.iwkms.drivenext.data.repository

import com.iwkms.drivenext.data.storage.MockDatabase
import com.iwkms.drivenext.domain.model.Car
import com.iwkms.drivenext.domain.repository.CarRepository

class FakeCarRepository : CarRepository {

    override fun getCars(): List<Car> {
        return MockDatabase.cars.toList()
    }

    override fun searchCars(query: String): List<Car> {
        if (query.isBlank()) return emptyList()
        return MockDatabase.cars.filter { car ->
            car.brand.contains(query, ignoreCase = true) ||
                    car.model.contains(query, ignoreCase = true)
        }
    }

    override fun getCarById(id: Int): Car? {
        return MockDatabase.cars.firstOrNull { it.id == id }
    }

    override fun getFavorites(): List<Car> {
        return MockDatabase.cars.filter { it.isFavorite }
    }

    override fun toggleFavorite(carId: Int): Boolean {
        val index = MockDatabase.cars.indexOfFirst { it.id == carId }
        if (index == -1) return false

        val currentCar = MockDatabase.cars[index]

        val updatedCar = currentCar.copy(isFavorite = !currentCar.isFavorite)

        MockDatabase.cars[index] = updatedCar

        return updatedCar.isFavorite
    }

    override fun addCar(car: Car) {
        MockDatabase.cars.add(0, car)
    }
}